package com.example.proyecto_final_de_onboarding.domain.usecase

import com.example.proyecto_final_de_onboarding.domain.model.CartItem
import com.example.proyecto_final_de_onboarding.domain.repository.CartRepository
import com.example.proyecto_final_de_onboarding.domain.usecase.base.CoroutineUseCase
import javax.inject.Inject

class EditQuantityUseCase @Inject constructor(private val cartRepository: CartRepository) :
    CoroutineUseCase<EditQuantityUseCase.Params, List<CartItem>>() {

    data class Params(
        val newQty: Int, val productId: Int
    )

    override suspend fun execute(params: Params): List<CartItem> {
        return cartRepository.editProductQuantity(productId = params.productId, qty = params.newQty)
    }

}