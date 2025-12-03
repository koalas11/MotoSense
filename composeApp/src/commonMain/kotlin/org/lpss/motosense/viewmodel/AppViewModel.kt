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
import org.lpss.motosense.model.DynamicColorMode
import org.lpss.motosense.model.Settings
import org.lpss.motosense.model.ThemeMode
import org.lpss.motosense.repository.SettingsRepository
import org.lpss.motosense.ui.navigation.ScreenRoute
import org.lpss.motosense.util.ResultError

class AppViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    private var _themeSettingsMutableState: MutableStateFlow<Pair<DynamicColorMode, ThemeMode>> = MutableStateFlow(
        Pair(DynamicColorMode.ENABLED, ThemeMode.SYSTEM_DEFAULT)
    )
    val themeSettingsState: StateFlow<Pair<DynamicColorMode, ThemeMode>> = _themeSettingsMutableState.asStateFlow()

    private var _settingsMutableState: MutableStateFlow<AppState> = MutableStateFlow(AppState.Loading)
    val settingsState: StateFlow<AppState> = _settingsMutableState.asStateFlow()

    private var _backStack: MutableStateFlow<MutableList<ScreenRoute>> = MutableStateFlow(mutableListOf(
        ScreenRoute.Home))
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
                        if (settings.dynamicColorMode != _themeSettingsMutableState.value.first ||
                            settings.themeMode != _themeSettingsMutableState.value.second
                        ) {
                            _themeSettingsMutableState.value =
                                Pair(settings.dynamicColorMode, settings.themeMode)
                        }
                        _settingsMutableState.value = AppState.Success(settings)
                    }
                }
                .onError { error ->
                    _settingsMutableState.value = AppState.Error(error)
                }
        }
    }

    fun navigateTo(route: ScreenRoute) {
        if (_backStack.value.lastOrNull() == route) {
            return
        }
        val newBackStack = _backStack.value.toMutableList()
        val oldIndex = newBackStack.indexOf(route)
        if (oldIndex != -1) {
            newBackStack.removeAt(oldIndex)
        }
        newBackStack.add(route)
        _backStack.value = newBackStack
    }

    fun navigateBack() {
        val newBackStack = _backStack.value.toMutableList()
        newBackStack.removeLast()
        if (newBackStack.isEmpty()) {
            newBackStack.add(ScreenRoute.Home)
        }
        _backStack.value = newBackStack
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
