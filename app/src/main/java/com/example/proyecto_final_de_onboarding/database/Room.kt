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

//    @Update
//    fun update(items: List<Item>)
//
//    @Transaction
//    fun upsert(items: List<Item>){
//        val insertResult: List<Long> = insertAll(items)
//        val updatedList = mutableListOf<Item>()
//        for ((index, res) in insertResult.withIndex()){
//            if (res.toInt() == -1){
//                updatedList.add(items[index])
//            }
//        }
//        if (updatedList.isNotEmpty()){
//            update(updatedList)
//        }
//    }
}

@Database(entities = [Item::class], version = 1)
abstract class ItemsDatabase: RoomDatabase(){
    abstract val itemDao: ItemDao
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
    fun insertAll( items: List<CartItem>)

    @Query("DELETE FROM cartTable WHERE itemId = :itemId")
    fun removeFromCartDB(itemId: Int)

    @Query("DELETE FROM cartTable")
    fun emptyTable()

    @Query("DELETE FROM cartTable WHERE itemId NOT in (SELECT ItemId FROM itemsTable WHERE id=cartTable.itemId)")
    fun removeIfNotInStore()
}

@Database(entities = [CartItem::class], version = 1)
abstract class CartDatabase: RoomDatabase() {
    abstract val cartDao: CartDao
}

private lateinit var CARTDBINSTANCE: CartDatabase

fun getCartDatabase(context: Context): CartDatabase{
    synchronized(CartDatabase::class.java) {
        if (!::CARTDBINSTANCE.isInitialized) {
            CARTDBINSTANCE = Room.databaseBuilder(context.applicationContext,
                CartDatabase::class.java,
                "cartItem").build()
        }
    }
    return CARTDBINSTANCE
}