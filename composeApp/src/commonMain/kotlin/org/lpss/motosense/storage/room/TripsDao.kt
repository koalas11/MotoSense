package org.lpss.motosense.storage.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TripsDao {
    @Query("SELECT * from trip")
    fun getTripsFlow(): Flow<Trip>

    @Insert
    suspend fun insertTrip(trip: Trip)
}
