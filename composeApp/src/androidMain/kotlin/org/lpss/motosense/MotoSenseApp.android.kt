package org.lpss.motosense

import android.app.Application

class MotoSenseAppAndroid: MotoSenseApp, Application() {
    override lateinit var appContainer: PlatformAppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AndroidAppContainer(this)
    }
}
