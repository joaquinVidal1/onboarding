package com.example.proyecto_final_de_onboarding.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemDao{
    @Query("select * from databaseitem")
    fun getItems(): LiveData<List<DataBaseItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( items: List<DataBaseItem>)
}

@Database(entities = [DataBaseItem::class], version = 1)
abstract class ItemsDatabase: RoomDatabase(){
    abstract val itemDao: ItemDao
}

private lateinit var INSTANCE: ItemsDatabase

fun getDatabase(context: Context): ItemsDatabase{
    synchronized(ItemsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                ItemsDatabase::class.java,
                "videos").build()
        }
    }
    return INSTANCE
}