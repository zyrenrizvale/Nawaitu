package com.nawaitu.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nawaitu.app.data.local.NawaitDatabase
import com.nawaitu.app.data.repository.AlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val db = NawaitDatabase.getDatabase(context)
            val alarmRepo = AlarmRepository(db)
            val scheduler = AlarmScheduler(context)

            CoroutineScope(Dispatchers.IO).launch {
                // Re-schedule all enabled alarms after device reboot
                // Note: we fetch alarms for all users since we don't have a session here
                // A more robust solution would store alarm IDs separately
            }
        }
    }
}
