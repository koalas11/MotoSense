package org.lpss.motosense

import org.lpss.motosense.repository.SettingsRepository
import org.lpss.motosense.repository.SettingsRepositoryImpl
import org.lpss.motosense.repository.TripsRepository
import org.lpss.motosense.repository.TripsRepositoryImpl
import org.lpss.motosense.storage.datastore.getSettingsDataStore
import org.lpss.motosense.storage.room.MotoSenseDatabase

interface AppContainer {
    val settingsRepository: SettingsRepository

    val tripsRepository: TripsRepository
}

open class AppContainerImpl(
    private val platformContext: PlatformContext
): AppContainer {
    override val settingsRepository: SettingsRepository by lazy {
        val settingsDataStore = getSettingsDataStore(platformContext)
        SettingsRepositoryImpl(settingsDataStore)
    }

    override val tripsRepository: TripsRepository by lazy {
        val db = MotoSenseDatabase.getDatabase(platformContext)
        TripsRepositoryImpl(db.tripsDao())
    }
}
