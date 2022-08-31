package com.example.proyecto_final_de_onboarding.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.Kind
import com.example.proyecto_final_de_onboarding.R

object ItemRepository {

    val itemList : List<Item> =
        listOf(
            Item(0, "Avocado", 30.5, R.drawable.avocado, Kind.Veggie, null),
            Item(1, "Cucumber", 33.8, R.drawable.cucumber, Kind.Veggie, null),
            Item(2, "Grapefruit", 45.2, R.drawable.grapefruit, Kind.Fruit, R.drawable.grapefruit_2),
            Item(3, "Kiwi", 30.2, R.drawable.kiwi, Kind.Fruit, R.drawable.kiwi_2),
            Item(4, "Watermelon", 45.3, R.drawable.watermelon, Kind.Fruit, R.drawable.watermelon_2),
        )

    private val _storeItems = MutableLiveData(itemList)
    val storeItems: LiveData<List<Item>> get() = _storeItems
}