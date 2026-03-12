package com.mlr.sportlink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mlr.sportlink.app.SportLinkApp
import com.mlr.sportlink.ui.theme.SportLinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SportLinkTheme(
                dynamicColor = false,
            ) {
                SportLinkApp()
            }
        }
    }
}
