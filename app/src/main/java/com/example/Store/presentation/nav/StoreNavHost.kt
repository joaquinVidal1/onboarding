package com.example.Store.presentation.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.Store.presentation.checkoutscreen.components.CheckoutScreen
import com.example.Store.presentation.mainscreen.components.MainScreen

@Composable
fun StoreNavHost(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = HomeDestination.route
    ) {

        composable(route = HomeDestination.route) {
            MainScreen(
                onCartPressed = { navController.navigateToCart() },
                viewModel = hiltViewModel()
            )
        }

        composable(route = CartDestination.route) {
            CheckoutScreen(
                onBackPressed = { navController.navigateUp() },
                viewModel = hiltViewModel()
            )
        }
    }
}

private fun NavHostController.navigateToCart() {
    this.navigate(route = CartDestination.route)
}