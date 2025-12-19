package org.lpss.motosense.repository

import kotlinx.coroutines.flow.Flow
import org.lpss.motosense.storage.room.Trip
import org.lpss.motosense.storage.room.TripsDao
import org.lpss.motosense.util.Log
import org.lpss.motosense.util.Result
import org.lpss.motosense.util.ResultError

/**
 * Repository interface for managing trips.
 */
interface TripsRepository {
    /**
     * Retrieves a flow of all trips.
     *
     * @return A Result containing a Flow of a list of Trips or an error.
     */
    fun getTripsFlow(): Result<Flow<List<Trip>>>

    /**
     * Retrieves a flow of a trip by its ID.
     *
     * @param tripId The ID of the trip to retrieve.
     * @return A Result containing a Flow of the Trip or null if not found, or an error.
     */
    fun getTripByIdFlow(tripId: Long): Result<Flow<Trip?>>

    /**
     * Inserts a new trip.
     *
     * @param trip The Trip object to insert.
     * @return A Result indicating success or failure.
     */
    suspend fun insertTrip(trip: Trip): Result<Unit>

    /**
     * Deletes a trip by its ID.
     *
     * @param tripId The ID of the trip to delete.
     * @return A Result indicating success or failure.
     */
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
