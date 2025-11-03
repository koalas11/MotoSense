package org.lpss.motosense.storage.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import okio.FileSystem
import okio.Path.Companion.toPath
import org.lpss.motosense.PlatformContext
import org.lpss.motosense.model.Settings

actual fun getSettingsDataStore(platformContext: PlatformContext): DataStore<Settings> {
    val context = requireNotNull(platformContext.context as Context)
    val producePath = { context.filesDir.resolve(SETTINGS_DATASTORE_NAME).absolutePath.toPath() }

    return createDataStore(fileSystem = FileSystem.SYSTEM, producePath = producePath)
}
