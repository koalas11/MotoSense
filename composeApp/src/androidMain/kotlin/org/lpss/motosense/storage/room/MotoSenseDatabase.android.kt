package org.lpss.motosense.storage.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.lpss.motosense.PlatformContext

actual fun getDatabaseBuilder(platformContext: PlatformContext): RoomDatabase.Builder<MotoSenseDatabase> {
    val context = requireNotNull(platformContext.context as Context)
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath(MOTO_SENSE_DB_NAME)
    return Room.databaseBuilder<MotoSenseDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
