package com.example.proyecto_final_de_onboarding.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyecto_final_de_onboarding.domain.model.Product
import com.example.proyecto_final_de_onboarding.domain.model.CartItem

@Database(entities = [Product::class, CartItem::class], version = 1, exportSchema = false)
abstract class MyStoreDatabase : RoomDatabase() {
    abstract val itemDao: ProductsDao
    abstract val cartDao: CartDao

    companion object {
        @Volatile
        private lateinit var MYSTOREDBINSTANCE: MyStoreDatabase

        fun getMyStoreDatabase(context: Context): MyStoreDatabase {
            synchronized(this) {
                if (!Companion::MYSTOREDBINSTANCE.isInitialized) {
                    MYSTOREDBINSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MyStoreDatabase::class.java,
                        "items"
                    ).build()
                }
            }
            return MYSTOREDBINSTANCE
        }
    }
}


