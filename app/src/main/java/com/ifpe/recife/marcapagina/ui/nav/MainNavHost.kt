package com.ifpe.recife.marcapagina.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ifpe.recife.marcapagina.ui.BookDetailsPage
import com.ifpe.recife.marcapagina.ui.HomePage
import com.ifpe.recife.marcapagina.ui.ProfilePage
import com.ifpe.recife.marcapagina.ui.SearchPage

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Route.Home.route) {
        composable(Route.Home.route) { HomePage() }

        composable(Route.Search.route) {
            SearchPage(navController = navController)
        }

        composable(Route.Profile.route) { ProfilePage() }

        composable(
            route = Route.Details.route,
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("author") { type = NavType.StringType },
                navArgument("coverId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: "Sem Título"
            val author = backStackEntry.arguments?.getString("author") ?: "Desconhecido"
            val coverId = backStackEntry.arguments?.getString("coverId") ?: "-1"

            BookDetailsPage(navController, title, author, coverId)
        }
    }
}