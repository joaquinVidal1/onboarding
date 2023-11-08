package com.example.Store.presentation.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.Store.presentation.checkoutscreen.CheckoutScreen
import com.example.Store.presentation.mainscreen.components.MainScreen

@Composable
fun StoreNavHost(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = HomeDestination.route
    ) {
        composable(HomeDestination.route) {
            MainScreen(onCartPressed = { navController.navigateToCart() })
        }

        composable(CartDestination.route) {
            CheckoutScreen(onBackPressed = {
                navController.navigateUp()
            })
        }
    }
}

fun NavHostController.navigateToCart() {
    navigate(CartDestination.route)
}