package com.example.Store.presentation.nav

interface Destination {
    val route: String
}

object HomeDestination : Destination {
    override val route: String = "home"
}

object CartDestination : Destination {
    override val route: String = "cart"
}