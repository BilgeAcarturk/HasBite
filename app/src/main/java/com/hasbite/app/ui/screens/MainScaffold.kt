package com.hasbite.app.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hasbite.app.navigation.bottomNavItems
import androidx.compose.foundation.layout.padding

@Composable
fun MainScaffold(
    navController: NavHostController,
    content: @Composable (Modifier) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color(0xFFF3EEE9), // search bar ile uyumlu arka plan
                    tonalElevation = 0.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    navController.navigate(item.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                    }
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFE47A2E),
                                selectedTextColor = Color(0xFFE47A2E),
                                indicatorColor = Color(0xFFFFE7D3),
                                unselectedIconColor = Color(0xFF6B6B6B),
                                unselectedTextColor = Color(0xFF6B6B6B)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}