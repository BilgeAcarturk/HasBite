package com.hasbite.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        label = "AI",
        route = Routes.AI.route,
        icon = Icons.Default.AutoAwesome
    ),
    BottomNavItem(
        label = "Explore",
        route = Routes.Explore.route,
        icon = Icons.Default.Explore
    ),
    BottomNavItem(
        label = "Favorites",
        route = Routes.Favorites.route,
        icon = Icons.Default.Favorite
    ),
    BottomNavItem(
        label = "Profile",
        route = Routes.Profile.route,
        icon = Icons.Default.Person
    )
)