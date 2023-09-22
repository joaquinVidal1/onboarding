package com.example.proyecto_final_de_onboarding.domain.usecase

import com.example.proyecto_final_de_onboarding.domain.model.CartItem
import com.example.proyecto_final_de_onboarding.domain.repository.CartRepository
import com.example.proyecto_final_de_onboarding.domain.usecase.base.CoroutineUseCase
import javax.inject.Inject

class RemoveProductFromCartUseCase @Inject constructor(private val cartRepository: CartRepository) :
    CoroutineUseCase<RemoveProductFromCartUseCase.Params, List<CartItem>>() {

    data class Params(val productId: Int)

    override suspend fun execute(params: Params): List<CartItem> {
        return cartRepository.removeProduct(params.productId)
    }
}