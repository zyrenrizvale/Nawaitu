package com.nawaitu.app.data.model

data class PrayerTime(
    val name: String,
    val nameAr: String,
    val time: String,
    val isNext: Boolean = false,
    val isNotificationEnabled: Boolean = true
)

data class PrayerTimings(
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val date: String,
    val hijriDate: String,
    val location: String
)
