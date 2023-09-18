package com.example.proyecto_final_de_onboarding.data.repository

import com.example.proyecto_final_de_onboarding.data.db.CartDao
import com.example.proyecto_final_de_onboarding.data.db.ProductsDao
import com.example.proyecto_final_de_onboarding.data.network.ProductsService
import com.example.proyecto_final_de_onboarding.domain.model.Product
import com.example.proyecto_final_de_onboarding.domain.repository.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productsDao: ProductsDao, private val cartDao: CartDao, private val productsService: ProductsService
) : ProductsRepository {

    fun getItem(productId: Int): Product {
        return productsDao.getItem(productId)
    }

    override suspend fun getProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            productsDao.getItems()
        }
    }

    override suspend fun refreshProducts() {
        withContext(Dispatchers.IO) {
            val productsList = productsService.getProducts()
            productsDao.emptyAndInsert(productsList.map { it.asDomainModel() })
            cartDao.removeIfNotInStore()
        }
    }

}