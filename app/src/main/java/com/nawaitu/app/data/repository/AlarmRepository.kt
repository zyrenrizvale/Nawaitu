package com.nawaitu.app.data.repository

import com.nawaitu.app.data.local.NawaitDatabase
import com.nawaitu.app.data.model.AlarmItem
import kotlinx.coroutines.flow.Flow

class AlarmRepository(private val database: NawaitDatabase) {
    fun getAllAlarms(userId: Long): Flow<List<AlarmItem>> =
        database.alarmDao().getAllByUser(userId)

    suspend fun getAllAlarmsOnce(userId: Long): List<AlarmItem> =
        database.alarmDao().getAllByUserOnce(userId)

    suspend fun addAlarm(alarm: AlarmItem): Long = database.alarmDao().insert(alarm)

    suspend fun toggleAlarm(id: Long, currentEnabled: Boolean) {
        database.alarmDao().updateEnabled(id, !currentEnabled)
    }

    suspend fun deleteAlarm(alarm: AlarmItem) = database.alarmDao().delete(alarm)

    suspend fun updateAlarm(alarm: AlarmItem) = database.alarmDao().update(alarm)
}
