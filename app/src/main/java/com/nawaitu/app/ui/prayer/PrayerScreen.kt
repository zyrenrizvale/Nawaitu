package com.nawaitu.app.ui.prayer

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nawaitu.app.data.model.PrayerTime
import com.nawaitu.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PrayerScreen(
    viewModel: PrayerViewModel = viewModel()
) {
    val timings by viewModel.timings.collectAsState()
    val prayerList by viewModel.prayerList.collectAsState()
    val nextPrayerName by viewModel.nextPrayerName.collectAsState()
    val nextPrayerTime by viewModel.nextPrayerTime.collectAsState()
    val countdown by viewModel.countdown.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentCity by viewModel.currentCity.collectAsState()
    val scrollState = rememberScrollState()

    val today = remember {
        SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id")).format(Date())
    }

    // Pulse animation for countdown
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
    ) {
        // Hero countdown section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0A1F12), Color(0xFF0D1117))
                    )
                )
                .padding(horizontal = 20.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Page title + location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = NeonGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentCity,
                        color = NeonGreen,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = today,
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                timings?.let { t ->
                    Text(
                        text = t.hijriDate,
                        color = TextSecondary.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (isLoading) {
                    CircularProgressIndicator(color = NeonGreen)
                } else if (error != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.WifiOff,
                            contentDescription = null,
                            tint = DangerRed,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = error ?: "",
                            color = DangerRed,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        TextButton(onClick = { viewModel.loadPrayerTimes() }) {
                            Text("Coba Lagi", color = NeonGreen)
                        }
                    }
                } else {
                    Text(
                        text = "Menuju $nextPrayerName",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = nextPrayerTime,
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = countdown,
                        color = NeonGreen.copy(alpha = pulseAlpha),
                        fontSize = 52.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-1).sp
                    )
                    Text(
                        text = "lagi",
                        color = NeonGreen.copy(alpha = 0.6f),
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Prayer list
        if (!isLoading && error == null) {
            Text(
                text = "Jadwal Sholat",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            prayerList.forEach { prayer ->
                PrayerTimeCard(prayer = prayer)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Sunrise card
            timings?.let { t ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceCard)
                        .border(0.5.dp, SurfaceBorder, RoundedCornerShape(14.dp))
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.WbSunny,
                                contentDescription = null,
                                tint = WarningAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Syuruq",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Terbit Matahari",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Text(
                            text = t.sunrise,
                            color = WarningAmber,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PrayerTimeCard(prayer: PrayerTime) {
    val backgroundColor = if (prayer.isNext) {
        Brush.linearGradient(
            colors = listOf(Color(0xFF0A2518), Color(0xFF0F301E), SurfaceCard)
        )
    } else {
        Brush.linearGradient(colors = listOf(SurfaceCard, SurfaceCard))
    }

    val borderColor = if (prayer.isNext) NeonGreen.copy(alpha = 0.5f) else SurfaceBorder

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(brush = backgroundColor)
            .border(
                width = if (prayer.isNext) 1.dp else 0.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (prayer.isNext) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(NeonGreen)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                } else {
                    Box(modifier = Modifier.size(18.dp))
                }

                Column {
                    Text(
                        text = prayer.name,
                        color = if (prayer.isNext) NeonGreen else TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = if (prayer.isNext) FontWeight.Bold else FontWeight.Medium
                    )
                    Text(
                        text = prayer.nameAr,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = prayer.time,
                    color = if (prayer.isNext) NeonGreen else TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (prayer.isNext) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Berikutnya",
                            color = NeonGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
