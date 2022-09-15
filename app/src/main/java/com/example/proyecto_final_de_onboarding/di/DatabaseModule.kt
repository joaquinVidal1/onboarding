package com.example.proyecto_final_de_onboarding.di

import android.content.Context
import com.example.proyecto_final_de_onboarding.database.CartDao
import com.example.proyecto_final_de_onboarding.database.ItemDao
import com.example.proyecto_final_de_onboarding.database.MyStoreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): MyStoreDatabase{
        return MyStoreDatabase.getMyStoreDatabase(appContext)
    }

    @Provides
    fun provideCartDao(database: MyStoreDatabase): CartDao{
        return database.cartDao
    }

    @Provides
    fun provideItemDao(database: MyStoreDatabase): ItemDao{
        return database.itemDao
    }
}