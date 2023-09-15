package com.example.proyecto_final_de_onboarding.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyecto_final_de_onboarding.domain.model.CartItem

@Dao
interface CartDao {
    @Query("select * from cartTable")
    fun getCartItems(): LiveData<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<CartItem>)

    @Query("DELETE FROM cartTable WHERE itemId = :itemId")
    fun removeFromCartDB(itemId: Int)

    @Query("DELETE FROM cartTable")
    fun emptyTable()

    @Query("DELETE FROM cartTable WHERE cartTable.itemId NOT IN (SELECT id FROM itemsTable)")
    fun removeIfNotInStore()
}