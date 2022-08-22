package com.example.proyecto_final_de_onboarding.data

import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.Kind
import com.example.proyecto_final_de_onboarding.R

object ItemRepository {
    val itemList : List<Item> =
        listOf(
            Item(0, "Avocado", 30, R.drawable.avocado, Kind.veggie, null),
            Item(1, "Cucumber", 30, R.drawable.cucumber, Kind.veggie, null),
            Item(2, "Grapefruit", 45, R.drawable.grapefruit, Kind.fruit, R.drawable.grapefruit_2),
            Item(3, "Kiwi", 30, R.drawable.kiwi, Kind.fruit, R.drawable.kiwi_2),
            Item(4, "Watermelon", 45, R.drawable.watermelon, Kind.fruit, R.drawable.watermelon_2),
        )
}