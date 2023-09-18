package com.example.proyecto_final_de_onboarding.data.db

import androidx.room.*
import com.example.proyecto_final_de_onboarding.domain.model.Product

@Dao
interface ProductsDao {
    @Query("select * from productsTable")
    fun getItems(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<Product>)

    @Query("DELETE FROM productsTable")
    fun emptyTable()

    @Transaction
    fun emptyAndInsert(products: List<Product>) {
        emptyTable()
        insertAll(products)
    }

    @Query("SELECT * FROM productsTable WHERE id = :productId")
    fun getItem(productId: Int): Product
}