package org.lpss.motosense.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.lpss.motosense.viewmodel.BaseOperationViewModel
import org.lpss.motosense.viewmodel.OperationUiState

@Composable
fun handleOperationState(
    viewModel: BaseOperationViewModel,
    onSuccess: () -> Unit = { viewModel.resetOperationState() },
    onError: () -> Unit = { viewModel.resetOperationState() },
): Boolean {
    val operationState by viewModel.operationUiState.collectAsStateWithLifecycle()
    val enabled = operationState is OperationUiState.Idle

    if (operationState is OperationUiState.Success) {
        if ((operationState as OperationUiState.Success).message != null) {
            fastUIActions.DisplayNotification(
                message = (operationState as OperationUiState.Success).message!!,
            )
        }

        onSuccess()
    } else if (operationState is OperationUiState.Error) {
        fastUIActions.DisplayNotification(
            message = (operationState as OperationUiState.Error).message,
        )
        onError()
    }

    return enabled
}
