package com.nawaitu.app.ui.alarm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nawaitu.app.data.local.NawaitDatabase
import com.nawaitu.app.data.local.SessionManager
import com.nawaitu.app.data.model.AlarmItem
import com.nawaitu.app.data.repository.AlarmRepository
import com.nawaitu.app.service.AlarmScheduler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val database = NawaitDatabase.getDatabase(application)
    private val sessionManager = SessionManager(application)
    private val alarmRepository = AlarmRepository(database)
    private val alarmScheduler = AlarmScheduler(application)

    private val _userId = MutableStateFlow(-1L)

    val alarms: StateFlow<List<AlarmItem>> = _userId
        .flatMapLatest { uid ->
            if (uid > 0) alarmRepository.getAllAlarms(uid)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            sessionManager.currentUserId.collect { id ->
                _userId.value = id
            }
        }
    }

    fun addAlarm(label: String, hour: Int, minute: Int) {
        val userId = _userId.value
        if (userId < 0) return
        viewModelScope.launch {
            val alarm = AlarmItem(
                userId = userId,
                label = label.ifBlank { "Alarm" },
                hour = hour,
                minute = minute
            )
            val id = alarmRepository.addAlarm(alarm)
            alarmScheduler.scheduleAlarm(alarm.copy(id = id))
        }
    }

    fun toggleAlarm(alarm: AlarmItem) {
        viewModelScope.launch {
            alarmRepository.toggleAlarm(alarm.id, alarm.isEnabled)
            if (alarm.isEnabled) {
                alarmScheduler.cancelAlarm(alarm)
            } else {
                alarmScheduler.scheduleAlarm(alarm.copy(isEnabled = true))
            }
        }
    }

    fun deleteAlarm(alarm: AlarmItem) {
        viewModelScope.launch {
            alarmScheduler.cancelAlarm(alarm)
            alarmRepository.deleteAlarm(alarm)
        }
    }
}
