package org.lpss.motosense.repository

import kotlinx.coroutines.flow.Flow
import org.lpss.motosense.storage.room.Trip
import org.lpss.motosense.storage.room.TripsDao
import org.lpss.motosense.util.Log
import org.lpss.motosense.util.Result
import org.lpss.motosense.util.ResultError

interface TripsRepository {
    fun getTripsFlow(): Result<Flow<List<Trip>>>

    fun getTripByIdFlow(tripId: Long): Result<Flow<Trip?>>

    suspend fun insertTrip(trip: Trip): Result<Unit>

    suspend fun deleteTripById(tripId: Long): Result<Unit>
}

class TripsRepositoryImpl(
    private val tripsDao: TripsDao,
): TripsRepository {

    override fun getTripsFlow(): Result<Flow<List<Trip>>> {
        return try {
            Result.Success(tripsDao.getTripsFlow())
        } catch (e: Exception) {
            Log.e(TAG, "An unknown error occurred", e)
            Result.Error(ResultError.UnknownError("An unknown error occurred"))
        }
    }

    override fun getTripByIdFlow(tripId: Long): Result<Flow<Trip?>> {
        return try {
            Result.Success(tripsDao.getTripByIdFlow(tripId))
        } catch (e: Exception) {
            Log.e(TAG, "An unknown error occurred", e)
            Result.Error(ResultError.UnknownError("An unknown error occurred"))
        }
    }

    override suspend fun insertTrip(trip: Trip): Result<Unit> {
        return try {
            tripsDao.insertTrip(trip)
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "An unknown error occurred", e)
            Result.Error(ResultError.UnknownError("An unknown error occurred"))
        }
    }

    override suspend fun deleteTripById(tripId: Long): Result<Unit> {
        return try {
            tripsDao.deleteTripById(tripId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "An unknown error occurred", e)
            Result.Error(ResultError.UnknownError("An unknown error occurred"))
        }
    }

    companion object {
        private const val TAG = "Trips Repository"
    }
}
