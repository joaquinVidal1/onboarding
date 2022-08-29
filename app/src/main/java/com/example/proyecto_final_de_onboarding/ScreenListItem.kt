package com.example.proyecto_final_de_onboarding

sealed class ScreenListItem(val id: Int) {
    data class ScreenHeader(val kind: Kind) : ScreenListItem(Int.MAX_VALUE)

    data class ScreenItem(val item: Item, val cant: Int = 0) : ScreenListItem(item.id)
}