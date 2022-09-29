package com.example.proyecto_final_de_onboarding.domain.entities

import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.Kind

sealed class ScreenListItem(val id: Int) {
    abstract fun getScreenItemKind(): Kind

    // TODO está bueno que todo tenga su id único, por eso kind.ordinal.times(-1)
    data class ScreenHeader(val kind: Kind) : ScreenListItem(kind.ordinal.times(-1)) {
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