package com.bloom.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.bloom.app.presentation.navigation.BloomNavGraph
import com.bloom.app.presentation.theme.BloomTheme

/**
 * MainActivity - Point d'entrée de l'application Bloom
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Active le mode edge-to-edge (plein écran moderne)
        enableEdgeToEdge()

        setContent {
            BloomTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BloomNavGraph()
                }
            }
        }
    }
}