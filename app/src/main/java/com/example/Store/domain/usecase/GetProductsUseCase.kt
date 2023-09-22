package com.example.proyecto_final_de_onboarding.domain.usecase

import com.example.proyecto_final_de_onboarding.domain.model.Product
import com.example.proyecto_final_de_onboarding.domain.repository.ProductsRepository
import com.example.proyecto_final_de_onboarding.domain.usecase.base.CoroutineUseCase
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val productsRepository: ProductsRepository) :
    CoroutineUseCase<Unit, List<Product>>() {
    override suspend fun execute(params: Unit): List<Product> {
        return productsRepository.getProducts()
    }

}