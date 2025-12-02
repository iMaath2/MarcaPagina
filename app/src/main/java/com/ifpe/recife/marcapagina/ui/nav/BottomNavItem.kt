package com.ifpe.recife.marcapagina.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Route(val route: String) {
    object Home : Route("home")
    object Search : Route("search")
    object Profile : Route("profile")
}

sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    object HomeButton : BottomNavItem("Início", Icons.Default.Home, Route.Home.route)
    object SearchButton : BottomNavItem("Buscar", Icons.Default.Search, Route.Search.route)
    object ProfileButton : BottomNavItem("Perfil", Icons.Default.Person, Route.Profile.route)
}