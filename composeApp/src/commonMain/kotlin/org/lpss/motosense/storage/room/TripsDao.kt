package org.lpss.motosense.storage.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TripsDao {
    @Query("SELECT * from trip")
    fun getTripsFlow(): Flow<List<Trip>>

    @Query("SELECT * from trip WHERE id = :tripId")
    fun getTripByIdFlow(tripId: Long): Flow<Trip?>

    @Insert
    suspend fun insertTrip(trip: Trip)

    @Query("DELETE from trip WHERE id = :tripId")
    suspend fun deleteTripById(tripId: Long)
}
