package com.example.proyecto_final_de_onboarding.domain.usecase

import com.example.proyecto_final_de_onboarding.domain.model.CartItem
import com.example.proyecto_final_de_onboarding.domain.repository.CartRepository
import com.example.proyecto_final_de_onboarding.domain.usecase.base.CoroutineUseCase
import javax.inject.Inject

class AddProductToCartUseCase @Inject constructor(private val cartRepository: CartRepository) :
    CoroutineUseCase<AddProductToCartUseCase.Params, List<CartItem>>() {

    data class Params(val productId: Int)

    override suspend fun execute(params: Params): List<CartItem> {
        return cartRepository.addProduct(params.productId)
    }
}