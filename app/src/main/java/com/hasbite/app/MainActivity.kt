package com.hasbite.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.hasbite.app.navigation.AppNavGraph
import com.hasbite.app.ui.theme.HasBiteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HasBiteTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}