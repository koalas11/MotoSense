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
import org.lpss.motosense.repository.TripsRepository
import org.lpss.motosense.storage.room.Trip

class TripsViewModel(
    private val tripsRepository: TripsRepository,
): ViewModel() {
    private var _tripsUiMutableState: MutableStateFlow<TripsUiState> =
        MutableStateFlow(TripsUiState.Loading)
    val tripsUiState: StateFlow<TripsUiState> = _tripsUiMutableState.asStateFlow()

    private var _initialized = false

    @MainThread
    fun initialize() {
        if (_initialized) return
        _initialized = true

        viewModelScope.launch {
            tripsRepository.getTripsFlow()
                .onSuccess { tripsFlow ->
                    tripsFlow.collect { tripsList ->
                        _tripsUiMutableState.value = TripsUiState.Success(tripsList)
                    }
                }
                .onError { error ->
                    _tripsUiMutableState.value = TripsUiState.Error(
                        message = error.message
                    )
                }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val tripsRepository = motoSenseAppContainer().tripsRepository
                TripsViewModel(
                    tripsRepository = tripsRepository,
                )
            }
        }
    }
}

sealed interface TripsUiState {
    object Loading: TripsUiState
    data class Success(
        val trips: List<Trip>
    ): TripsUiState
    data class Error(
        val message: String,
    ): TripsUiState
}
