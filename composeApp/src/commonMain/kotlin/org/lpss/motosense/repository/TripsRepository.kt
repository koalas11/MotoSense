package org.lpss.motosense.repository

import kotlinx.coroutines.flow.Flow
import org.lpss.motosense.storage.room.Trip
import org.lpss.motosense.storage.room.TripsDao
import org.lpss.motosense.util.Log
import org.lpss.motosense.util.Result

interface TripsRepository {
    fun getTripsFlow(): Result<Flow<Trip>>

    suspend fun insertTrip(trip: Trip): Result<Unit>
}

class TripsRepositoryImpl(
    private val tripsDao: TripsDao,
): TripsRepository {

    override fun getTripsFlow(): Result<Flow<Trip>> {
        return try {
            Result.Success(tripsDao.getTripsFlow())
        } catch (e: Exception) {
            Log.e(TAG, "An unknown error occurred", e)
            Result.Error(RepositoryError.UnknownError("An unknown error occurred"))
        }
    }

    override suspend fun insertTrip(trip: Trip): Result<Unit> {
        return try {
            tripsDao.insertTrip(trip)
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "An unknown error occurred", e)
            Result.Error(RepositoryError.UnknownError("An unknown error occurred"))
        }
    }

    companion object {
        private const val TAG = "Trips Repository"
    }
}
