package com.example.proyecto_final_de_onboarding.domain.model

sealed class ScreenListItem(val id: Int) {

    abstract fun getScreenItemKind(): Kind

    data class ScreenHeader(val kind: Kind) : ScreenListItem(Int.MAX_VALUE) {
        override fun getScreenItemKind(): Kind {
            return kind
        }
    }

    data class ScreenItem(val product: Product, val quantity: Int = 0) : ScreenListItem(product.id) {
        override fun getScreenItemKind(): Kind {
            return product.kind
        }
    }
}