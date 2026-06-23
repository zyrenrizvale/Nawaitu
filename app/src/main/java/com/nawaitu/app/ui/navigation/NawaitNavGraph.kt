package com.nawaitu.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nawaitu.app.ui.alarm.AlarmScreen
import com.nawaitu.app.ui.auth.AuthViewModel
import com.nawaitu.app.ui.auth.LoginScreen
import com.nawaitu.app.ui.auth.RegisterScreen
import com.nawaitu.app.ui.community.CommunityScreen
import com.nawaitu.app.ui.components.NawaitNavBar
import com.nawaitu.app.ui.components.navItems
import com.nawaitu.app.ui.home.HomeScreen
import com.nawaitu.app.ui.prayer.PrayerScreen
import com.nawaitu.app.ui.todo.TodoScreen
import com.nawaitu.app.ui.theme.DarkBackground
import com.nawaitu.app.ui.theme.SurfaceCard

@Composable
fun NawaitNavGraph(
    authViewModel: AuthViewModel,
    isLoggedIn: Boolean
) {
    val navController = rememberNavController()
    val authState by authViewModel.uiState.collectAsState()

    LaunchedEffect(authState.isLoggedIn) {
        if (authState.isLoggedIn) {
            val current = navController.currentDestination?.route
            if (current == "login" || current == "register") {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        } else {
            val current = navController.currentDestination?.route
            if (current != "login" && current != "register" && current != null) {
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val showBottomBar = navItems.any { it.route == currentRoute }

    Scaffold(
        containerColor = DarkBackground,
        bottomBar = {
            if (showBottomBar) {
                NawaitNavBar(
                    currentRoute = currentRoute ?: "home",
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) "home" else "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("login") {
                LoginScreen(
                    uiState = authState,
                    onLogin = authViewModel::login,
                    onNavigateToRegister = { navController.navigate("register") },
                    onClearError = authViewModel::clearError
                )
            }
            composable("register") {
                RegisterScreen(
                    uiState = authState,
                    onRegister = authViewModel::register,
                    onNavigateBack = { navController.popBackStack() },
                    onClearError = authViewModel::clearError
                )
            }
            composable("home") {
                HomeScreen(
                    authViewModel = authViewModel,
                    onNavigate = { route -> navController.navigate(route) }
                )
            }
            composable("prayer") {
                PrayerScreen()
            }
            composable("alarm") {
                AlarmScreen(authViewModel = authViewModel)
            }
            composable("todo") {
                TodoScreen(authViewModel = authViewModel)
            }
            composable("community") {
                CommunityScreen(authViewModel = authViewModel)
            }
        }
    }
}
