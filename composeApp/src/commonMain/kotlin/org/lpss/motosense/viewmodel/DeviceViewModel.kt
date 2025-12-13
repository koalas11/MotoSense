package org.lpss.motosense.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.lpss.motosense.model.DeviceData
import org.lpss.motosense.module.BluetoothLowEnergyManager
import org.lpss.motosense.repository.TripsRepository
import org.lpss.motosense.storage.room.Trip
import org.lpss.motosense.util.Log
import org.lpss.motosense.util.ResultError
import org.lpss.motosense.util.getFormattedTimestamp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class DeviceViewModel(
    private val bluetoothLowEnergyManager: BluetoothLowEnergyManager,
    private val tripsRepository: TripsRepository,
) : ViewModel() {
    private var _deviceMutableState: MutableStateFlow<DeviceState> = MutableStateFlow(DeviceState.Idle)
    val deviceState: StateFlow<DeviceState> = _deviceMutableState.asStateFlow()

    private val tripHistory: MutableList<DeviceData> = mutableListOf()

    private var _rollAngleMutableState: MutableStateFlow<Short?> = MutableStateFlow(0)
    val rollAngleState: StateFlow<Short?> = _rollAngleMutableState.asStateFlow()
    private var _pitchAngleMutableState: MutableStateFlow<Short?> = MutableStateFlow(0)
    val pitchAngleState: StateFlow<Short?> = _pitchAngleMutableState.asStateFlow()
    private var _slopeAngleMutableState: MutableStateFlow<Double?> = MutableStateFlow(0.0)
    val slopeAngleState: StateFlow<Double?> = _slopeAngleMutableState.asStateFlow()
    private var _perceivedAccelerationMutableState: MutableStateFlow<Float?> = MutableStateFlow(0f)
    val perceivedAccelerationState: StateFlow<Float?> = _perceivedAccelerationMutableState.asStateFlow()
    private var _accelerationDirectionMutableState: MutableStateFlow<Byte?> = MutableStateFlow(0)
    val accelerationDirectionState: StateFlow<Byte?> = _accelerationDirectionMutableState.asStateFlow()
    private var _altitudeMutableState: MutableStateFlow<Short?> = MutableStateFlow(0)
    val altitudeState: StateFlow<Short?> = _altitudeMutableState.asStateFlow()
    private var _speedMutableState: MutableStateFlow<UShort?> = MutableStateFlow(0u)
    val speedState: StateFlow<UShort?> = _speedMutableState.asStateFlow()
    private var _latitudeMutableState: MutableStateFlow<Float?> = MutableStateFlow(0f)
    val latitudeState: StateFlow<Float?> = _latitudeMutableState.asStateFlow()
    private var _longitudeMutableState: MutableStateFlow<Float?> = MutableStateFlow(0f)
    val longitudeState: StateFlow<Float?> = _longitudeMutableState.asStateFlow()
    private var _timestampMutableState: MutableStateFlow<Long?> = MutableStateFlow(0L)
    val timestampState: StateFlow<Long?> = _timestampMutableState.asStateFlow()

    private var _distanceKmGpsMutableState: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val distanceKmGpsState: StateFlow<Double> = _distanceKmGpsMutableState.asStateFlow()
    private var _distanceKmMutableState: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val distanceKmState: StateFlow<Double> = _distanceKmMutableState.asStateFlow()
    private var _distanceKmAvgMutableState: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val distanceKmAvgState: StateFlow<Double> = _distanceKmAvgMutableState.asStateFlow()

    private var oldLat = Double.NaN
    private var oldLon = Double.NaN
    private val R = 6371000.0 // Earth radius in meters

    private fun haversine(newLat: Double, newLon: Double): Double {
        if (oldLat.isNaN() || oldLon.isNaN()) {
            oldLat = newLat
            oldLon = newLon
            return 0.0
        }
        val lat1 = oldLat
        val lon1 = oldLon
        val dLat = (newLat - lat1) * PI / 180
        val dLon = (newLon - lon1) * PI / 180
        val rLat1 = lat1 * PI / 180
        val rLat2 = newLat * PI / 180

        val a = sin(dLat / 2).pow(2.0) +
                cos(rLat1) * cos(rLat2) * sin(dLon / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = R * c
        oldLat = newLat
        oldLon = newLon
        return distance
    }

    fun startScanning() {
        require(_deviceMutableState.value == DeviceState.Idle)
        _deviceMutableState.value = DeviceState.Scanning
        viewModelScope.launch {
            bluetoothLowEnergyManager.startScanning {
                viewModelScope.launch {
                    Log.d("DeviceViewModel", "Scanning Callback: $it")
                    it.onSuccess { name ->
                        Log.d("DeviceViewModel", "Found Device: $name")
                        if (_deviceMutableState.value is DeviceState.Ready) {
                            val currentNames = (_deviceMutableState.value as DeviceState.Ready).deviceNames
                            if (!currentNames.contains(name)) {
                                _deviceMutableState.value = DeviceState.Ready(
                                    deviceNames = currentNames + name
                                )
                            }
                        } else {
                            _deviceMutableState.value = DeviceState.Ready(
                                deviceNames = listOf(name)
                            )
                        }
                    }
                    .onError { error ->
                        _deviceMutableState.value = DeviceState.Error(error)
                    }
                }
            }
        }
    }

    fun stopScanning() {
        require(_deviceMutableState.value == DeviceState.Scanning)
        viewModelScope.launch {
            bluetoothLowEnergyManager.stopScanning()
            _deviceMutableState.value = DeviceState.Idle
        }
    }

    fun startDataReadings(
        deviceName: String,
    ) {
        require(_deviceMutableState.value is DeviceState.Ready)
        viewModelScope.launch {
            tripHistory.clear()
            bluetoothLowEnergyManager.startDataReadings(deviceName) {
                Log.d("DeviceViewModel", "Data Reading Callback: $it")
                tripHistory.add(it)
                _rollAngleMutableState.value = it.rollAngle
                _pitchAngleMutableState.value = it.pitchAngle
                _slopeAngleMutableState.value = it.slopeAngle
                _perceivedAccelerationMutableState.value = it.perceivedAcceleration
                _accelerationDirectionMutableState.value = it.accelerationDirection
                _altitudeMutableState.value = it.altitude
                _speedMutableState.value = it.speed
                _latitudeMutableState.value = it.latitude
                _longitudeMutableState.value = it.longitude
                _timestampMutableState.value = it.timestamp

                var check1 = false
                var check2 = false
                if (it.latitude != null && it.longitude != null) {
                    val distanceIncrement = haversine(
                        newLat = it.latitude.toDouble(),
                        newLon = it.longitude.toDouble()
                    )
                    if (distanceIncrement > 0) {
                        _distanceKmGpsMutableState.value += distanceIncrement / 1000.0
                        check1 = true
                    }
                }

                if (it.pitchAngle != null && it.speed != null && it.perceivedAcceleration != null && it.accelerationDirection != null && it.timestamp != null) {
                    if (tripHistory.size >= 2) {
                        val prev = tripHistory[tripHistory.size - 2]
                        val previousTimestamp = prev.timestamp ?: it.timestamp
                        val timeSeconds = (it.timestamp - previousTimestamp).toDouble() / 1000.0

                        val currSpeedMps = it.speed.toDouble() / 3.6

                        val distanceIncrement = currSpeedMps * timeSeconds
                        if (distanceIncrement > 0) {
                            _distanceKmMutableState.value += distanceIncrement / 1000.0
                            check2 = true
                        }
                    }
                }

                if (check1 && check2) {
                    Log.d("DeviceViewModel", "Distance increments: GPS=${_distanceKmGpsMutableState.value} km, Calculated=${_distanceKmMutableState.value} km")
                    _distanceKmAvgMutableState.value = (_distanceKmGpsMutableState.value + _distanceKmMutableState.value) / 2.0
                }
            }
            _deviceMutableState.value = DeviceState.Running
        }
    }

    @OptIn(ExperimentalTime::class)
    fun stopDataReadings() {
        require(_deviceMutableState.value == DeviceState.Running)
        viewModelScope.launch {
            bluetoothLowEnergyManager.stopDataReadings()
            bluetoothLowEnergyManager.reset()
            tripsRepository.insertTrip(
                Trip(
                    title = "Trip at ${getFormattedTimestamp(Clock.System.now().toEpochMilliseconds())}",
                    history = tripHistory.toList(),
                    distanceTravelledKm = _distanceKmMutableState.value,
                    distanceTravelledGpsKm = _distanceKmGpsMutableState.value,
                    distanceTravelledAvgKm = _distanceKmAvgMutableState.value,
                )
            )
            _deviceMutableState.value = DeviceState.Idle
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val bluetoothLowEnergyManager = motoSenseAppContainer().bluetoothLowEnergyManager
                val tripsRepository = motoSenseAppContainer().tripsRepository
                DeviceViewModel(
                    bluetoothLowEnergyManager = bluetoothLowEnergyManager,
                    tripsRepository = tripsRepository,
                )
            }
        }
    }
}

sealed interface DeviceState {
    object Idle: DeviceState
    object Scanning: DeviceState
    data class Ready(
        val deviceNames: List<String> = emptyList()
    ): DeviceState
    object Running: DeviceState
    data class Error(val error: ResultError): DeviceState
}
