package com.example.proyecto_final_de_onboarding.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.proyecto_final_de_onboarding.Item

@Dao
interface ItemDao {
    @Query("select * from itemsTable")
    fun getItems(): LiveData<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<Item>)

    @Query("DELETE FROM itemsTable")
    fun emptyTable()

    @Transaction
    fun emptyAndInsert(items: List<Item>) {
        emptyTable()
        insertAll(items)
    }

}