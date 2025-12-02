package com.ifpe.recife.marcapagina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ifpe.recife.marcapagina.ui.nav.BottomNavItem
import com.ifpe.recife.marcapagina.ui.nav.BottomNavBar
import com.ifpe.recife.marcapagina.ui.nav.MainNavHost
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val items = listOf(
                BottomNavItem.HomeButton,
                BottomNavItem.SearchButton,
                BottomNavItem.ProfileButton
            )

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Marca Página") },
                        actions = {
                            IconButton(onClick = {
                                FirebaseAuth.getInstance().signOut()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = "Sair"
                                )
                            }
                        }
                    )
                },
                bottomBar = {
                    BottomNavBar(navController = navController, items = items)
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    MainNavHost(navController = navController)
                }
            }
        }
    }
}