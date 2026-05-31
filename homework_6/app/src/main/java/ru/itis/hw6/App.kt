package ru.itis.hw6

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.firebase.crashlytics.FirebaseCrashlytics
import ru.itis.hw6.di.AppComponent
import ru.itis.hw6.di.DaggerAppComponent
import java.util.UUID

class App : Application() {

    companion object {
        lateinit var instance: App
            private set

        const val PROMO_CHANNEL_ID = "promo_channel"
        const val AUTH_CHANNEL_ID = "auth_channel"
        const val DEFAULT_CHANNEL_ID = "default_channel"
    }

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.create()
        initCrashlytics()
        createNotificationChannels()
    }

    private fun initCrashlytics() {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val userId = prefs.getString("user_id", null) ?: UUID.randomUUID().toString().also {
            prefs.edit().putString("user_id", it).apply()
        }
        FirebaseCrashlytics.getInstance().setUserId(userId)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannels(
                listOf(
                    NotificationChannel(PROMO_CHANNEL_ID, "Акции и предложения", NotificationManager.IMPORTANCE_DEFAULT),
                    NotificationChannel(AUTH_CHANNEL_ID, "Безопасность", NotificationManager.IMPORTANCE_HIGH),
                    NotificationChannel(DEFAULT_CHANNEL_ID, "Общие уведомления", NotificationManager.IMPORTANCE_DEFAULT)
                )
            )
        }
    }
}
