package com.hasbite.app.navigation

import androidx.compose.material.icons.Icons
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
    BottomNavItem(Routes.Home.route, "Home", Icons.Filled.Home),
    BottomNavItem(Routes.Explore.route, "Explore", Icons.Filled.Search),
    BottomNavItem(Routes.Favorites.route, "Favorites", Icons.Filled.Favorite),
    BottomNavItem(Routes.Profile.route, "Profile", Icons.Filled.Person),
)