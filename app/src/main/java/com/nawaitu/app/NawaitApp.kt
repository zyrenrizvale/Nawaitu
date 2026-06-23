package com.nawaitu.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.nawaitu.app.data.local.NawaitDatabase

class NawaitApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize database (triggers seeding of default data)
        NawaitDatabase.getDatabase(this)
        // Create notification channels
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)

            val alarmChannel = NotificationChannel(
                ALARM_CHANNEL_ID,
                "Alarm Nawaitu",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi untuk alarm harian"
                enableVibration(true)
            }

            val prayerChannel = NotificationChannel(
                PRAYER_CHANNEL_ID,
                "Waktu Sholat",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi untuk pengingat waktu sholat"
                enableVibration(true)
            }

            notificationManager.createNotificationChannels(listOf(alarmChannel, prayerChannel))
        }
    }

    companion object {
        const val ALARM_CHANNEL_ID = "nawaitu_alarm_channel"
        const val PRAYER_CHANNEL_ID = "nawaitu_prayer_channel"
    }
}
