package org.lpss.motosense.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import org.lpss.motosense.model.DynamicColorMode
import org.lpss.motosense.model.MotorcycleIcon
import org.lpss.motosense.model.Settings
import org.lpss.motosense.model.ThemeMode
import org.lpss.motosense.repository.SettingsRepository

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
) : BaseOperationViewModel() {

    private fun updateSettings(modify: (Settings) -> Settings) {
        startOperation()
        viewModelScope.launch {
            settingsRepository.updateSettings(modify).onSuccess {
                operationUiMutableState.value = OperationUiState.Success(null)
            }.onError { error ->
                operationUiMutableState.value = OperationUiState.Error(error.message)
            }
        }
    }

    fun setDynamicColorMode(dynamicColorMode: DynamicColorMode) {
        updateSettings { currentSettings ->
            currentSettings.copy(
                dynamicColorMode = dynamicColorMode
            )
        }
    }

    fun setThemeMode(themeMode: ThemeMode) {
        updateSettings { currentSettings ->
            currentSettings.copy(
                themeMode = themeMode
            )
        }
    }

    fun setBiggerText(biggerText: Boolean) {
        updateSettings { currentSettings ->
            currentSettings.copy(
                biggerText = biggerText
            )
        }
    }

    fun setImmersiveMode(immersiveMode: Boolean) {
        updateSettings { currentSettings ->
            currentSettings.copy(
                immersiveMode = immersiveMode
            )
        }
    }

    fun setMotorcycleIcon(icon: MotorcycleIcon) {
        updateSettings { currentSettings ->
            currentSettings.copy(
                motorcycleIcon = icon
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val settingsRepository = motoSenseAppContainer().settingsRepository
                SettingsViewModel(
                    settingsRepository = settingsRepository,
                )
            }
        }

        /**
         * A sealed interface that represents the success notifications for managing transactions.
         */
        sealed interface SettingsSuccessNotifications : SuccessNotifications {
        }
    }
}

