package org.lpss.motosense.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.lpss.motosense.repository.TripsRepository
import org.lpss.motosense.storage.room.Trip

class TripDetailsViewModel(
    private val tripsRepository: TripsRepository,
): BaseOperationViewModel() {
    private var _tripDetailsUiMutableState: MutableStateFlow<TripsDetailsUiState> =
        MutableStateFlow(TripsDetailsUiState.Loading)
    val tripDetailsUiState: StateFlow<TripsDetailsUiState> = _tripDetailsUiMutableState.asStateFlow()

    private var _initialized = -1L

    @MainThread
    fun initialize(tripId: Long) {
        if (_initialized == tripId) return
        _initialized = tripId

        viewModelScope.coroutineContext.cancelChildren()
        viewModelScope.launch {
            tripsRepository.getTripByIdFlow(tripId)
                .onSuccess { tripFlow ->
                    tripFlow.collect { trip ->
                        if (trip != null) {
                            _tripDetailsUiMutableState.value = TripsDetailsUiState.Success(trip)
                        } else {
                            _tripDetailsUiMutableState.value = TripsDetailsUiState.Error(
                                message = "Trip not found"
                            )
                        }
                    }
                }
                .onError { error ->
                    _tripDetailsUiMutableState.value = TripsDetailsUiState.Error(
                        message = error.message
                    )
                }
        }
    }

    fun deleteTripById(tripId: Long) {
        startOperation()
        viewModelScope.launch {
            tripsRepository.deleteTripById(tripId)
                .onSuccess {
                    operationUiMutableState.value = OperationUiState.Success("Trip deleted successfully")
                }
                .onError { error ->
                    operationUiMutableState.value = OperationUiState.Error(error.message)
                }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val tripsRepository = motoSenseAppContainer().tripsRepository
                TripDetailsViewModel(
                    tripsRepository = tripsRepository,
                )
            }
        }
    }
}

sealed interface TripsDetailsUiState {
    object Loading: TripsDetailsUiState
    data class Success(
        val trip: Trip
    ): TripsDetailsUiState
    data class Error(
        val message: String
    ): TripsDetailsUiState
}
