package com.example.proyecto_final_de_onboarding.domain.model

import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.Kind

sealed class ScreenListItem(val id: Int) {
    abstract fun getScreenItemKind(): Kind

    data class ScreenHeader(val kind: Kind) : ScreenListItem(Int.MAX_VALUE) {
        override fun getScreenItemKind(): Kind {
            return kind
        }
    }

    data class ScreenItem(val item: Item, val cant: Int = 0) : ScreenListItem(item.id) {
        override fun getScreenItemKind(): Kind {
            return item.kind
        }
    }
}