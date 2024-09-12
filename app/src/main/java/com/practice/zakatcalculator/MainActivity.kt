package com.practice.zakatcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.practice.zakatcalculator.navigation.NavigationGraph
import com.practice.zakatcalculator.presentation.NisabViewModel
import com.practice.zakatcalculator.ui.theme.ZakatCalculatorTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZakatCalculatorTheme {

                val viewModel by viewModels<NisabViewModel>()
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    val navController = rememberNavController()
                    val nisabEvent = viewModel::event
                    val nisabState by viewModel.nisabState.collectAsState()
                    NavigationGraph(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        nisabState = nisabState,
                        nisabEvent = nisabEvent
                    )
                }
            }
        }
    }
}

