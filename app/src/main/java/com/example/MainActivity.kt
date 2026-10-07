package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.GlobeScreen
import com.example.ui.GlobeViewModel
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SpaceBlack

class MainActivity : ComponentActivity() {
    private val globeViewModel: GlobeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = SpaceBlack
                ) {
                    GlobeScreen(viewModel = globeViewModel)
                }
            }
        }
    }
}
