package com.nawaitu.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nawaitu.app.data.local.NawaitDatabase
import com.nawaitu.app.data.local.SessionManager
import com.nawaitu.app.data.model.PrayerTimings
import com.nawaitu.app.data.model.TodoItem
import com.nawaitu.app.data.repository.PrayerRepository
import com.nawaitu.app.data.repository.TodoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val database = NawaitDatabase.getDatabase(application)
    private val sessionManager = SessionManager(application)
    private val prayerRepository = PrayerRepository()
    private val todoRepository = TodoRepository(database)

    private val _currentTime = MutableStateFlow("")
    val currentTime: StateFlow<String> = _currentTime

    private val _prayerTimings = MutableStateFlow<PrayerTimings?>(null)
    val prayerTimings: StateFlow<PrayerTimings?> = _prayerTimings

    private val _nextPrayerName = MutableStateFlow("")
    val nextPrayerName: StateFlow<String> = _nextPrayerName

    private val _nextPrayerTime = MutableStateFlow("")
    val nextPrayerTime: StateFlow<String> = _nextPrayerTime

    private val _nextPrayerCountdown = MutableStateFlow("")
    val nextPrayerCountdown: StateFlow<String> = _nextPrayerCountdown

    private val _userId = MutableStateFlow(-1L)

    val recentTodos: StateFlow<List<TodoItem>> = _userId
        .flatMapLatest { uid ->
            if (uid > 0) todoRepository.getAllTodos(uid)
            else flowOf(emptyList())
        }
        .map { todos -> todos.filter { !it.isDone }.take(3) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        startClock()
        loadPrayerTimes()
        viewModelScope.launch {
            sessionManager.currentUserId.collect { id ->
                _userId.value = id
            }
        }
    }

    private fun startClock() {
        viewModelScope.launch {
            while (true) {
                val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                _currentTime.value = sdf.format(Date())
                delay(30_000)
            }
        }
    }

    private fun loadPrayerTimes() {
        viewModelScope.launch {
            val result = prayerRepository.getPrayerTimesByCity()
            if (result.isSuccess) {
                val timings = result.getOrNull()!!
                _prayerTimings.value = timings
                updateNextPrayer(timings)
            }
        }
    }

    private fun updateNextPrayer(timings: PrayerTimings) {
        val (name, time, countdown) = prayerRepository.getCountdownToNextPrayer(timings)
        _nextPrayerName.value = name
        _nextPrayerTime.value = time
        _nextPrayerCountdown.value = countdown
    }
}
