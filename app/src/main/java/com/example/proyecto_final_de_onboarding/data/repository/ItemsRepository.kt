package com.example.proyecto_final_de_onboarding.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.data.db.CartDao
import com.example.proyecto_final_de_onboarding.data.db.ItemDao
import com.example.proyecto_final_de_onboarding.data.network.ProductsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ItemsRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val cartDao: CartDao,
    private val productsService: ProductsService
) {
    private val _networkError = MutableLiveData(false)
    val networkError: LiveData<Boolean>
        get() = _networkError

    suspend fun refreshItems() {
        withContext(Dispatchers.IO) {
            try {
                val itemList = productsService.getItems()
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