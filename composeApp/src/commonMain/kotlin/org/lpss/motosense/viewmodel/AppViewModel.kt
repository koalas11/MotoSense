package org.lpss.motosense.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel(

) : ViewModel() {
    private var _altitudeValue: MutableStateFlow<Float> = MutableStateFlow(0f)
    val altitudeValue: StateFlow<Float> = _altitudeValue.asStateFlow()

    private var _leanAngleValue: MutableStateFlow<Float> = MutableStateFlow(0f)
    val leanAngleValue: StateFlow<Float> = _leanAngleValue.asStateFlow()

    private var _speedValue: MutableStateFlow<Float> = MutableStateFlow(0f)
    val speedValue: StateFlow<Float> = _speedValue.asStateFlow()


    fun setAltitude(value: Float) {
        _altitudeValue.value = value
    }

    fun setLeanAngle(value: Float) {
        _leanAngleValue.value = value
    }

    fun setSpeed(value: Float) {
        _speedValue.value = value
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AppViewModel()
            }
        }
    }
}