package com.example.proyecto_final_de_onboarding.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.database.ItemsDatabase
import com.example.proyecto_final_de_onboarding.database.asDomainModel
import com.example.proyecto_final_de_onboarding.network.ItemNetwork
import com.example.proyecto_final_de_onboarding.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemsRepository(
    private val database: ItemsDatabase,
    private val cartRepository: CartRepository
) {

    suspend fun refreshItems() {
        withContext(Dispatchers.IO) {
            val itemList = ItemNetwork.items.getItems()
            val databaseList = itemList.asDatabaseModel()
            databaseList.forEach { dataBaseItem ->
                dataBaseItem.qty = cartRepository.getQty(dataBaseItem.id)
            }
            database.itemDao.upsert(databaseList)
        }
    }

    val storeItems: LiveData<List<Item>> = Transformations.map(database.itemDao.getItems()) {
        it?.asDomainModel() ?: listOf()
    }

}