package com.example.proyecto_final_de_onboarding.domain.usecase

import com.example.proyecto_final_de_onboarding.domain.repository.ProductsRepository
import com.example.proyecto_final_de_onboarding.domain.usecase.base.CoroutineUseCase
import javax.inject.Inject

class UpdateProductsUseCase @Inject constructor(private val productsRepository: ProductsRepository) :
    CoroutineUseCase<Unit, Unit>() {
    override suspend fun execute(params: Unit) {
        productsRepository.refreshProducts()
    }

}