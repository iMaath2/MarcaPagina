package com.ifpe.recife.marcapagina.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ifpe.recife.marcapagina.ui.HomePage
import com.ifpe.recife.marcapagina.ui.ProfilePage
import com.ifpe.recife.marcapagina.ui.SearchPage

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Route.Home.route) {
        composable(Route.Home.route) { HomePage() }
        composable(Route.Search.route) { SearchPage() }
        composable(Route.Profile.route) { ProfilePage() }
    }
}