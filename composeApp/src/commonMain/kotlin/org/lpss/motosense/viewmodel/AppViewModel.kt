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

class AppViewModel(

) : ViewModel() {
    private var _altitudeValue: MutableStateFlow<Float> = MutableStateFlow(0f)
    val altitudeValue: StateFlow<Float> = _altitudeValue.asStateFlow()

    private var _leanAngleValue: MutableStateFlow<Float> = MutableStateFlow(0f)
    val leanAngleValue: StateFlow<Float> = _leanAngleValue.asStateFlow()

    private var _speedValue: MutableStateFlow<Float> = MutableStateFlow(0f)
    val speedValue: StateFlow<Float> = _speedValue.asStateFlow()

    private var _slopeValue: MutableStateFlow<Float> = MutableStateFlow(0f)
    val slopeValue: StateFlow<Float> = _slopeValue.asStateFlow()

    fun setAltitude(value: Float) {
        _altitudeValue.value = value
    }

    fun setLeanAngle(value: Float) {
        _leanAngleValue.value = value
    }

    fun setSpeed(value: Float) {
        _speedValue.value = value
    }

    fun setSlope(value: Float) {
        _slopeValue.value = value
    }

    fun startSimulation() {
        val steps = arrayOf(-30, -23, -10, 0, 10, 15, 23, 29, 35, 37, 40, 42, 45, 47, 50, 52, 55, 57)
        viewModelScope.launch {
            for (i in steps) {
                setLeanAngle(i.toFloat())
                kotlinx.coroutines.delay(500)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AppViewModel()
            }
        }
    }
}