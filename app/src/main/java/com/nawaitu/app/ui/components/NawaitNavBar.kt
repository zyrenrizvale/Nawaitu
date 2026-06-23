package com.nawaitu.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nawaitu.app.ui.theme.*

sealed class NavDestination(val route: String, val label: String, val icon: ImageVector) {
    object Home : NavDestination("home", "Beranda", Icons.Default.Home)
    object Prayer : NavDestination("prayer", "Sholat", Icons.Default.AccessTime)
    object Alarm : NavDestination("alarm", "Alarm", Icons.Default.Alarm)
    object Todo : NavDestination("todo", "To-Do", Icons.Default.TaskAlt)
    object Community : NavDestination("community", "Komunitas", Icons.Default.Groups)
}

val navItems = listOf(
    NavDestination.Home,
    NavDestination.Prayer,
    NavDestination.Alarm,
    NavDestination.Todo,
    NavDestination.Community
)

@Composable
fun NawaitNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    Box {
        // Top border glow line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            NeonGreen.copy(alpha = 0.4f),
                            NeonGreen.copy(alpha = 0.7f),
                            NeonGreen.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )

        NavigationBar(
            containerColor = SurfaceCard,
            contentColor = NeonGreen,
            tonalElevation = 0.dp
        ) {
            navItems.forEach { destination ->
                val selected = currentRoute == destination.route

                val iconColor by animateColorAsState(
                    targetValue = if (selected) NeonGreen else TextSecondary,
                    animationSpec = spring(stiffness = Spring.StiffnessMedium),
                    label = "icon_color_${destination.route}"
                )

                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(destination.route) },
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = destination.label,
                            tint = iconColor
                        )
                    },
                    label = {
                        Text(
                            text = destination.label,
                            color = iconColor,
                            fontSize = 10.sp,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonGreen,
                        unselectedIconColor = TextSecondary,
                        selectedTextColor = NeonGreen,
                        unselectedTextColor = TextSecondary,
                        indicatorColor = NeonGreen.copy(alpha = 0.12f)
                    )
                )
            }
        }
    }
}
