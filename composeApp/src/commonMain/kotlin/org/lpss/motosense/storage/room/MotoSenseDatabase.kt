package org.lpss.motosense.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import kotlinx.coroutines.runBlocking
import org.lpss.motosense.PlatformContext
import kotlin.concurrent.Volatile

@Database(entities = [Trip::class], version = 1)
abstract class MotoSenseDatabase : RoomDatabase() {
    abstract fun tripsDao(): TripsDao

    companion object {
        @Volatile
        private var Instance: MotoSenseDatabase? = null

        fun getDatabase(platformContext: PlatformContext): MotoSenseDatabase {
            return Instance ?: runBlocking {
                getDatabaseBuilder(platformContext)
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

const val MOTO_SENSE_DB_NAME = "MotoSense_DB"

expect fun getDatabaseBuilder(platformContext: PlatformContext): RoomDatabase.Builder<MotoSenseDatabase>
