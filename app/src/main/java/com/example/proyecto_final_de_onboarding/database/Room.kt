package com.example.proyecto_final_de_onboarding.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemDao{
    @Query("select * from databaseitem")
    fun getItems(): LiveData<List<DatabaseItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll( items: List<DatabaseItem>): List<Long>

    @Update
    fun update(items: List<DatabaseItem>)

    @Transaction
    fun upsert(items: List<DatabaseItem>){
        val insertResult: List<Long> = insertAll(items)
        val updatedList = mutableListOf<DatabaseItem>()
        for ((index, res) in insertResult.withIndex()){
            if (res.toInt() == -1){
                updatedList.add(items[index])
            }
        }
        if (updatedList.isNotEmpty()){
            update(updatedList)
        }
    }
}

@Database(entities = [DatabaseItem::class], version = 1)
abstract class ItemsDatabase: RoomDatabase(){
    abstract val itemDao: ItemDao
}

private lateinit var INSTANCE: ItemsDatabase

fun getDatabase(context: Context): ItemsDatabase{
    synchronized(ItemsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                ItemsDatabase::class.java,
                "items").build()
        }
    }
    return INSTANCE
}