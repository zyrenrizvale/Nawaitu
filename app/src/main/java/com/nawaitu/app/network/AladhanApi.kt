package com.nawaitu.app.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface AladhanApi {
    @GET("v1/timingsByCity")
    suspend fun getPrayerTimesByCity(
        @Query("city") city: String,
        @Query("country") country: String,
        @Query("method") method: Int = 11 // Kemenag Indonesia
    ): AladhanResponse

    @GET("v1/timings")
    suspend fun getPrayerTimesByCoords(
        @Query("latitude") lat: Double,
        @Query("longitude") lng: Double,
        @Query("method") method: Int = 11
    ): AladhanResponse
}

data class AladhanResponse(
    val code: Int,
    val status: String,
    val data: AladhanData
)

data class AladhanData(
    val timings: AladhanTimings,
    val date: AladhanDate,
    val meta: AladhanMeta
)

data class AladhanTimings(
    @SerializedName("Fajr") val fajr: String,
    @SerializedName("Sunrise") val sunrise: String,
    @SerializedName("Dhuhr") val dhuhr: String,
    @SerializedName("Asr") val asr: String,
    @SerializedName("Sunset") val sunset: String,
    @SerializedName("Maghrib") val maghrib: String,
    @SerializedName("Isha") val isha: String,
    @SerializedName("Imsak") val imsak: String,
    @SerializedName("Midnight") val midnight: String
)

data class AladhanDate(
    val readable: String,
    val timestamp: String,
    val hijri: HijriDate,
    val gregorian: GregorianDate
)

data class HijriDate(
    val date: String,
    val day: String,
    val month: HijriMonth,
    val year: String
)

data class HijriMonth(
    val number: Int,
    val en: String,
    val ar: String
)

data class GregorianDate(
    val date: String,
    val day: String,
    val month: GregorianMonth,
    val year: String
)

data class GregorianMonth(
    val number: Int,
    val en: String
)

data class AladhanMeta(
    val latitude: Double,
    val longitude: Double,
    val timezone: String
)
