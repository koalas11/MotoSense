package org.lpss.motosense.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.lpss.motosense.model.Settings
import org.lpss.motosense.util.ResultError
import org.lpss.motosense.repository.SettingsRepository
import org.lpss.motosense.ui.navigation.ScreenRoute

class AppViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    private var _dynamicThemeMutableStateFlow : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val dynamicThemeStateFlow: StateFlow<Boolean> = _dynamicThemeMutableStateFlow.asStateFlow()

    private var _settingsMutableStateFlow: MutableStateFlow<AppState> = MutableStateFlow(AppState.Loading)
    val settingsStateFlow: StateFlow<AppState> = _settingsMutableStateFlow.asStateFlow()

    private var _backStack: MutableStateFlow<MutableList<ScreenRoute>> =
        MutableStateFlow(mutableListOf(ScreenRoute.Home))
    val backStack: StateFlow<List<ScreenRoute>> = _backStack.asStateFlow()

    private var _isInitialized = false

    @MainThread
    fun initialize() {
        if (_isInitialized)
            return
        _isInitialized = true
        viewModelScope.launch {
            settingsRepository.getSettingsFlow()
                .onSuccess { flow ->
                    flow.collect { settings ->
                        _dynamicThemeMutableStateFlow.value = settings.enableDynamicTheme
                        _settingsMutableStateFlow.value = AppState.Success(settings)
                    }
                }
                .onError { error ->
                    _settingsMutableStateFlow.value = AppState.Error(error)
                }
        }
    }

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

    fun updateSettings(value: Boolean) {
        viewModelScope.launch {
            val settings = Settings(value)
            settingsRepository.updateSettings(settings).onSuccess {
                _dynamicThemeMutableStateFlow.value = settings.enableDynamicTheme
            }
        }
    }

    fun navigateTo(route: ScreenRoute) {
        val existingIndex = _backStack.value.indexOf(route)
        if (existingIndex != -1) {
            val tempList = _backStack.value.toMutableList()
            val element = tempList.removeAt(existingIndex)
            tempList.add(element)
            _backStack.value = tempList
        }
    }

    fun navigateBack() {
        val tempList = _backStack.value.toMutableList()
        val element = tempList.removeLastOrNull()
        if (element == null) {
            tempList.add(ScreenRoute.Home)
        }
        _backStack.value = tempList
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
                val settingsRepository = motoSenseAppContainer().settingsRepository
                AppViewModel(
                    settingsRepository = settingsRepository,
                )
            }
        }
    }
}

sealed interface AppState {
    object Loading: AppState
    data class Success(
        val settings: Settings
    ): AppState
    data class Error(
        val error: ResultError
    ): AppState
}
