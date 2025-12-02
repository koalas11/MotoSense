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
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class DeviceViewModel(
    private val bluetoothLowEnergyManager: BluetoothLowEnergyManager,
    private val tripsRepository: TripsRepository,
) : ViewModel() {
    private var _deviceMutableState: MutableStateFlow<DeviceState> = MutableStateFlow(DeviceState.Idle)
    val deviceState: StateFlow<DeviceState> = _deviceMutableState.asStateFlow()

    private val tripHistory: MutableList<DeviceData> = mutableListOf()

    private var _rollAngleMutableState: MutableStateFlow<Short> = MutableStateFlow(0)
    val rollAngleState: StateFlow<Short> = _rollAngleMutableState.asStateFlow()
    private var _pitchAngleMutableState: MutableStateFlow<Short> = MutableStateFlow(0)
    val pitchAngleState: StateFlow<Short> = _pitchAngleMutableState.asStateFlow()
    private var _slopeAngleMutableState: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val slopeAngleState: StateFlow<Double> = _slopeAngleMutableState.asStateFlow()
    private var _perceivedAccelerationMutableState: MutableStateFlow<Float> = MutableStateFlow(0f)
    val perceivedAccelerationState: StateFlow<Float> = _perceivedAccelerationMutableState.asStateFlow()
    private var _accelerationDirectionMutableState: MutableStateFlow<Byte> = MutableStateFlow(0)
    val accelerationDirectionState: StateFlow<Byte> = _accelerationDirectionMutableState.asStateFlow()
    private var _altitudeMutableState: MutableStateFlow<Short> = MutableStateFlow(0)
    val altitudeState: StateFlow<Short> = _altitudeMutableState.asStateFlow()
    private var _speedMutableState: MutableStateFlow<UShort> = MutableStateFlow(0u)
    val speedState: StateFlow<UShort> = _speedMutableState.asStateFlow()
    private var _latitudeMutableState: MutableStateFlow<Float> = MutableStateFlow(0f)
    val latitudeState: StateFlow<Float> = _latitudeMutableState.asStateFlow()
    private var _longitudeMutableState: MutableStateFlow<Float> = MutableStateFlow(0f)
    val longitudeState: StateFlow<Float> = _longitudeMutableState.asStateFlow()
    private var _timestampMutableState: MutableStateFlow<Long> = MutableStateFlow(0L)
    val timestampState: StateFlow<Long> = _timestampMutableState.asStateFlow()

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
                it.rollAngle ?.let { rollAngle ->
                    _rollAngleMutableState.value = rollAngle
                }
                it.pitchAngle ?.let { pitchAngle ->
                    _pitchAngleMutableState.value = pitchAngle
                }
                it.slopeAngle ?.let { slopeAngle ->
                    _slopeAngleMutableState.value = slopeAngle
                }
                it.perceivedAcceleration ?.let { perceivedAcceleration ->
                    _perceivedAccelerationMutableState.value = perceivedAcceleration
                }
                it.accelerationDirection ?.let { accelerationDirection ->
                    _accelerationDirectionMutableState.value = accelerationDirection
                }
                it.altitude ?.let { altitude ->
                    _altitudeMutableState.value = altitude
                }
                it.speed ?.let { speed ->
                    _speedMutableState.value = speed
                }
                it.latitude ?.let { latitude ->
                    _latitudeMutableState.value = latitude
                }
                it.longitude ?.let { longitude ->
                    _longitudeMutableState.value = longitude
                }
                it.timestamp ?.let { timestamp ->
                    _timestampMutableState.value = timestamp
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
                    title = "Trip at ${Clock.System.now().toEpochMilliseconds()}",
                    history = tripHistory.toList()
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
