package com.example.proyecto_final_de_onboarding.domain.usecase

import com.example.proyecto_final_de_onboarding.domain.model.Product
import com.example.proyecto_final_de_onboarding.domain.repository.ProductsRepository
import com.example.proyecto_final_de_onboarding.domain.usecase.base.CoroutineUseCase
import javax.inject.Inject

class GetProductUseCase @Inject constructor(private val productsRepository: ProductsRepository) :
    CoroutineUseCase<GetProductUseCase.Params, Product>() {

    data class Params(val productId: Int)
    override suspend fun execute(params: Params): Product {
        return productsRepository.getProduct(productId = params.productId)
    }
}