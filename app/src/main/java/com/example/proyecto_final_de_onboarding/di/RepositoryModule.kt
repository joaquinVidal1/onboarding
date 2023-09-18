package com.example.proyecto_final_de_onboarding.di

import com.example.proyecto_final_de_onboarding.data.repository.CartRepositoryImpl
import com.example.proyecto_final_de_onboarding.data.repository.ProductsRepositoryImpl
import com.example.proyecto_final_de_onboarding.domain.repository.CartRepository
import com.example.proyecto_final_de_onboarding.domain.repository.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindItemsRepository(repositoryImpl: ProductsRepositoryImpl): ProductsRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(repositoryImpl: CartRepositoryImpl): CartRepository

}