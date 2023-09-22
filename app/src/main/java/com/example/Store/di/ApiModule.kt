package com.example.proyecto_final_de_onboarding.di

import com.example.proyecto_final_de_onboarding.data.network.ProductsService
import com.example.proyecto_final_de_onboarding.data.network.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideProductsApi(): ProductsService {
        return RetrofitFactory.getBuilder().create(ProductsService::class.java)
    }
}