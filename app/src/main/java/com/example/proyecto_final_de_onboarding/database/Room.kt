package com.example.proyecto_final_de_onboarding.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.Item

@Dao
interface ItemDao{
    @Query("select * from itemsTable")
    fun getItems(): LiveData<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( items: List<Item>)//: List<Long>

    @Query("DELETE FROM itemsTable")
    fun emptyTable()
}

@Database(entities = [Item::class, CartItem::class], version = 1)
abstract class ItemsDatabase: RoomDatabase(){
    abstract val itemDao: ItemDao
    abstract val cartDao: CartDao
}

private lateinit var ITEMDBINSTANCE: ItemsDatabase

fun getItemsDatabase(context: Context): ItemsDatabase{
    synchronized(ItemsDatabase::class.java) {
        if (!::ITEMDBINSTANCE.isInitialized) {
            ITEMDBINSTANCE = Room.databaseBuilder(context.applicationContext,
                ItemsDatabase::class.java,
                "items").build()
        }
    }
    return ITEMDBINSTANCE
}

@Dao
interface CartDao{
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

