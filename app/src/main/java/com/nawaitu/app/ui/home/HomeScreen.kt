package com.nawaitu.app.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nawaitu.app.R
import com.nawaitu.app.data.model.Priority
import com.nawaitu.app.data.model.TodoItem
import com.nawaitu.app.ui.auth.AuthViewModel
import com.nawaitu.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    onNavigate: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()
    val nextPrayerName by viewModel.nextPrayerName.collectAsState()
    val nextPrayerTime by viewModel.nextPrayerTime.collectAsState()
    val nextPrayerCountdown by viewModel.nextPrayerCountdown.collectAsState()
    val recentTodos by viewModel.recentTodos.collectAsState()
    val scrollState = rememberScrollState()

    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when (hour) {
            in 4..10 -> "Selamat Pagi"
            in 11..14 -> "Selamat Siang"
            in 15..17 -> "Selamat Sore"
            else -> "Selamat Malam"
        }
    }

    val today = remember {
        SimpleDateFormat("EEEE, dd MMM yyyy", Locale("id")).format(Date())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
    ) {
        // Top header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0A1A0E), DarkBackground)
                    )
                )
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "$greeting,",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = authState.userName.ifEmpty { "Pengguna" },
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = today,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Logo small
                    Image(
                        painter = painterResource(id = R.drawable.logo_nobg),
                        contentDescription = "Logo",
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )
                    // Logout button
                    IconButton(
                        onClick = { authViewModel.logout() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SurfaceCard)
                    ) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Prayer countdown card (hero)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF0A2518),
                            Color(0xFF0F301E),
                            SurfaceCard
                        )
                    )
                )
                .border(1.dp, NeonGreen.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Sholat Berikutnya",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Text(
                        text = nextPrayerName.ifEmpty { "Memuat..." },
                        color = NeonGreen,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = nextPrayerTime,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Countdown",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Text(
                        text = nextPrayerCountdown.ifEmpty { "--" },
                        color = NeonGreen,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "lagi",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Fitur
        Text(
            text = "Fitur",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Feature cards grid
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HomeFeatureCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.AccessTime,
                title = "Sholat",
                subtitle = "Jadwal & Adzan",
                accentColor = NeonGreen,
                onClick = { onNavigate("prayer") }
            )
            HomeFeatureCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Alarm,
                title = "Alarm",
                subtitle = "Pengingat Harian",
                accentColor = Color(0xFF3498DB),
                onClick = { onNavigate("alarm") }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HomeFeatureCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.TaskAlt,
                title = "To-Do",
                subtitle = "Daftar Tugas",
                accentColor = Color(0xFFFFA502),
                onClick = { onNavigate("todo") }
            )
            HomeFeatureCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Groups,
                title = "Komunitas",
                subtitle = "Feed Berbagi",
                accentColor = Color(0xFFE91E8C),
                onClick = { onNavigate("community") }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Recent Todos
        if (recentTodos.isNotEmpty()) {
            Text(
                text = "Tugas Aktif",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            recentTodos.forEach { todo ->
                HomeTodoItem(todo = todo)
            }

            TextButton(
                onClick = { onNavigate("todo") },
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Text(
                    text = "Lihat semua tugas",
                    color = NeonGreen,
                    fontSize = 13.sp
                )
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = NeonGreen,
                    modifier = Modifier.size(16.dp).padding(start = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun HomeFeatureCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceCard)
            .border(0.5.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = TextSecondary, fontSize = 11.sp)
        }
    }
}

@Composable
private fun HomeTodoItem(todo: TodoItem) {
    val priorityColor = when (todo.priority) {
        Priority.HIGH -> PriorityHigh
        Priority.MEDIUM -> PriorityMedium
        Priority.LOW -> PriorityLow
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceCard)
            .border(0.5.dp, SurfaceBorder, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(priorityColor)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = todo.title,
                color = TextPrimary,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
