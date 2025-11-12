package org.lpss.motosense.repository

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import org.lpss.motosense.model.Settings
import org.lpss.motosense.util.Log
import org.lpss.motosense.util.Result
import org.lpss.motosense.util.ResultError

interface SettingsRepository {
    fun getSettingsFlow(): Result<Flow<Settings>>

    suspend fun updateSettings(settings: Settings): Result<Unit>
}

class SettingsRepositoryImpl(
    private val settingsDataStore: DataStore<Settings>
): SettingsRepository {

    override fun getSettingsFlow(): Result<Flow<Settings>> {
        return try {
            Result.Success(settingsDataStore.data)
        } catch (e: Exception) {
            Log.e(TAG, "An unknown error occurred", e)
            Result.Error(ResultError.UnknownError("An unknown error occurred"))
        }
    }

    override suspend fun updateSettings(settings: Settings): Result<Unit> {
        return try {
            settingsDataStore.updateData { settings }
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "An unknown error occurred", e)
            Result.Error(ResultError.UnknownError("An unknown error occurred"))
        }
    }

    companion object {
        private const val TAG = "SettingsRepository"
    }
}
