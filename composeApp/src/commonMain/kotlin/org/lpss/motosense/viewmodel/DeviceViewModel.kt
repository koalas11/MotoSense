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
import org.lpss.motosense.module.BluetoothLowEnergyManager
import org.lpss.motosense.util.ResultError

class DeviceViewModel(
    private val bluetoothLowEnergyManager: BluetoothLowEnergyManager,
) : ViewModel() {

    private var _deviceMutableState: MutableStateFlow<DeviceState> = MutableStateFlow(DeviceState.Idle)
    val deviceState: StateFlow<DeviceState> = _deviceMutableState.asStateFlow()

    fun startScanning() {
        require(_deviceMutableState.value == DeviceState.Idle)
        _deviceMutableState.value = DeviceState.Scanning
        viewModelScope.launch {
            bluetoothLowEnergyManager.startScanning {
                launch {
                    it.onSuccess {
                        _deviceMutableState.value = DeviceState.Ready
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

    fun startDataReadings() {
        require(_deviceMutableState.value == DeviceState.Ready)
        viewModelScope.launch {
            bluetoothLowEnergyManager.startDataReadings()
            _deviceMutableState.value = DeviceState.Running
        }
    }

    fun stopDataReadings() {
        require(_deviceMutableState.value == DeviceState.Running)
        viewModelScope.launch {
            bluetoothLowEnergyManager.stopDataReadings()
            _deviceMutableState.value = DeviceState.Ready
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val bluetoothLowEnergyManager = motoSenseAppContainer().bluetoothLowEnergyManager
                DeviceViewModel(
                    bluetoothLowEnergyManager = bluetoothLowEnergyManager,
                )
            }
        }
    }
}

sealed interface DeviceState {
    object Idle: DeviceState
    object Scanning: DeviceState
    object Ready: DeviceState
    object Running: DeviceState
    data class Error(val error: ResultError): DeviceState
}
