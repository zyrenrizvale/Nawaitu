package com.nawaitu.app.data.repository

import com.nawaitu.app.data.model.PrayerTime
import com.nawaitu.app.data.model.PrayerTimings
import com.nawaitu.app.network.RetrofitClient
import java.util.Calendar

class PrayerRepository {
    private val api = RetrofitClient.aladhanApi

    suspend fun getPrayerTimesByCity(
        city: String = "Jakarta",
        country: String = "Indonesia"
    ): Result<PrayerTimings> {
        return try {
            val response = api.getPrayerTimesByCity(city, country)
            val timings = response.data.timings
            val date = response.data.date
            val hijri = date.hijri
            val hijriDate = "${hijri.day} ${hijri.month.en} ${hijri.year} H"

            Result.success(
                PrayerTimings(
                    fajr = cleanTime(timings.fajr),
                    sunrise = cleanTime(timings.sunrise),
                    dhuhr = cleanTime(timings.dhuhr),
                    asr = cleanTime(timings.asr),
                    maghrib = cleanTime(timings.maghrib),
                    isha = cleanTime(timings.isha),
                    date = date.readable,
                    hijriDate = hijriDate,
                    location = city
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPrayerTimesByCoords(lat: Double, lng: Double): Result<PrayerTimings> {
        return try {
            val response = api.getPrayerTimesByCoords(lat, lng)
            val timings = response.data.timings
            val date = response.data.date
            val hijri = date.hijri
            val hijriDate = "${hijri.day} ${hijri.month.en} ${hijri.year} H"

            Result.success(
                PrayerTimings(
                    fajr = cleanTime(timings.fajr),
                    sunrise = cleanTime(timings.sunrise),
                    dhuhr = cleanTime(timings.dhuhr),
                    asr = cleanTime(timings.asr),
                    maghrib = cleanTime(timings.maghrib),
                    isha = cleanTime(timings.isha),
                    date = date.readable,
                    hijriDate = hijriDate,
                    location = "Lokasi GPS"
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getPrayerList(timings: PrayerTimings): List<PrayerTime> {
        val currentMinutes = getCurrentMinutes()
        val prayers = listOf(
            PrayerTime("Subuh", "الفجر", timings.fajr),
            PrayerTime("Dzuhur", "الظهر", timings.dhuhr),
            PrayerTime("Ashar", "العصر", timings.asr),
            PrayerTime("Maghrib", "المغرب", timings.maghrib),
            PrayerTime("Isya", "العشاء", timings.isha)
        )

        val nextIndex = prayers.indexOfFirst { timeToMinutes(it.time) > currentMinutes }
        return prayers.mapIndexed { index, prayer ->
            prayer.copy(isNext = index == nextIndex)
        }
    }

    fun getCountdownToNextPrayer(timings: PrayerTimings): Triple<String, String, String> {
        val currentMinutes = getCurrentMinutes()
        val prayers = getPrayerList(timings)
        val nextPrayer = prayers.firstOrNull { it.isNext }

        return if (nextPrayer != null) {
            val nextMinutes = timeToMinutes(nextPrayer.time)
            val diff = nextMinutes - currentMinutes
            val hours = diff / 60
            val minutes = diff % 60
            Triple(
                nextPrayer.name,
                nextPrayer.time,
                if (hours > 0) "${hours}j ${minutes}m" else "${minutes}m"
            )
        } else {
            Triple("Subuh", timings.fajr, "Besok")
        }
    }

    private fun cleanTime(time: String): String = time.split(" ")[0]

    private fun timeToMinutes(time: String): Int {
        val parts = time.split(":")
        return if (parts.size >= 2) {
            (parts[0].toIntOrNull() ?: 0) * 60 + (parts[1].toIntOrNull() ?: 0)
        } else 0
    }

    private fun getCurrentMinutes(): Int {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
    }
}
