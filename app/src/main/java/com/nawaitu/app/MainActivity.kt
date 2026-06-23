package com.nawaitu.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nawaitu.app.ui.auth.AuthViewModel
import com.nawaitu.app.ui.navigation.NawaitNavGraph
import com.nawaitu.app.ui.theme.NawaituTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NawaituTheme {
                val authViewModel: AuthViewModel = viewModel()
                val authState by authViewModel.uiState.collectAsState()
                NawaitNavGraph(
                    authViewModel = authViewModel,
                    isLoggedIn = authState.isLoggedIn
                )
            }
        }
    }
}
