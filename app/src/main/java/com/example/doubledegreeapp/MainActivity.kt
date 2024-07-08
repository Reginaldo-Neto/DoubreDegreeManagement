package com.example.doubledegreeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.doubledegreeapp.ui.theme.DoubleDegreeAppTheme
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoubleDegreeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
