package com.example.proyecto_final_de_onboarding.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.database.CartDao
import com.example.proyecto_final_de_onboarding.database.ItemDao
import com.example.proyecto_final_de_onboarding.network.ItemNetwork
import com.example.proyecto_final_de_onboarding.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ItemsRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val cartDao: CartDao
) {
    private val _networkError = MutableLiveData(false)
    val networkError: LiveData<Boolean>
        get() = _networkError

    suspend fun refreshItems() {
        withContext(Dispatchers.IO) {
            try {
                val itemList = ItemNetwork.items.getItems()
                itemDao.emptyAndInsert(itemList.map { it.asDomainModel() })
                cartDao.removeIfNotInStore()
            } catch (networkError: Exception) {
                _networkError.postValue(true)
            }
        }
    }

    val storeItems: LiveData<List<Item>> = Transformations.map(itemDao.getItems()) {
        it ?: listOf()
    }

    fun getItem(itemId: Int): Item? {
        return storeItems.value?.find { it.id == itemId }
    }

    fun networkErrorHandled() {
        _networkError.value = false
    }

}