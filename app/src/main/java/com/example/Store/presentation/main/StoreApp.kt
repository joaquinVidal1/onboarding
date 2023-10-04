package com.example.Store.presentation.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.Store.presentation.nav.StoreNavHost

@Composable
fun StoreApp() {
    val navController = rememberNavController()

    StoreNavHost(navController = navController)
}