package com.example.proyecto_final_de_onboarding.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.database.CartDatabase
import com.example.proyecto_final_de_onboarding.database.ItemsDatabase
import com.example.proyecto_final_de_onboarding.network.ItemNetwork
import com.example.proyecto_final_de_onboarding.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemsRepository(
    private val database: ItemsDatabase,
    private val cartDatabase: CartDatabase
) {

    suspend fun refreshItems() {
        withContext(Dispatchers.IO) {
            database.itemDao.emptyTable()
            val itemList = ItemNetwork.items.getItems()
            database.itemDao.insertAll(itemList.map { it.asDomainModel()})
            cartDatabase.cartDao.removeIfNotInStore()
        }
    }

    val storeItems: LiveData<List<Item>> = Transformations.map(database.itemDao.getItems()) {
        it ?: listOf()
    }

}