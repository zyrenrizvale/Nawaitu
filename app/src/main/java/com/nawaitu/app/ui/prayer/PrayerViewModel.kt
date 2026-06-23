package com.nawaitu.app.ui.prayer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nawaitu.app.data.model.PrayerTime
import com.nawaitu.app.data.model.PrayerTimings
import com.nawaitu.app.data.repository.PrayerRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PrayerViewModel(application: Application) : AndroidViewModel(application) {
    private val prayerRepository = PrayerRepository()

    private val _timings = MutableStateFlow<PrayerTimings?>(null)
    val timings: StateFlow<PrayerTimings?> = _timings

    private val _prayerList = MutableStateFlow<List<PrayerTime>>(emptyList())
    val prayerList: StateFlow<List<PrayerTime>> = _prayerList

    private val _nextPrayerName = MutableStateFlow("")
    val nextPrayerName: StateFlow<String> = _nextPrayerName

    private val _nextPrayerTime = MutableStateFlow("")
    val nextPrayerTime: StateFlow<String> = _nextPrayerTime

    private val _countdown = MutableStateFlow("--")
    val countdown: StateFlow<String> = _countdown

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _currentCity = MutableStateFlow("Jakarta")
    val currentCity: StateFlow<String> = _currentCity

    init {
        loadPrayerTimes()
        startCountdownUpdater()
    }

    fun loadPrayerTimes(city: String = "Jakarta") {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _currentCity.value = city
            val result = prayerRepository.getPrayerTimesByCity(city)
            if (result.isSuccess) {
                val t = result.getOrNull()!!
                _timings.value = t
                _prayerList.value = prayerRepository.getPrayerList(t)
                updateCountdown(t)
            } else {
                _error.value = "Gagal memuat jadwal sholat. Periksa koneksi internet."
            }
            _isLoading.value = false
        }
    }

    private fun startCountdownUpdater() {
        viewModelScope.launch {
            while (true) {
                delay(60_000)
                _timings.value?.let { updateCountdown(it) }
            }
        }
    }

    private fun updateCountdown(t: PrayerTimings) {
        _prayerList.value = prayerRepository.getPrayerList(t)
        val (name, time, countdown) = prayerRepository.getCountdownToNextPrayer(t)
        _nextPrayerName.value = name
        _nextPrayerTime.value = time
        _countdown.value = countdown
    }
}
