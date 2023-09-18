package com.example.proyecto_final_de_onboarding.domain.usecase

import com.example.proyecto_final_de_onboarding.domain.model.CartItem
import com.example.proyecto_final_de_onboarding.domain.repository.CartRepository
import com.example.proyecto_final_de_onboarding.domain.usecase.base.CoroutineUseCase
import javax.inject.Inject

class GetCartUseCase @Inject constructor(private val cartRepository: CartRepository) :
    CoroutineUseCase<Unit, List<CartItem>>() {

    override suspend fun execute(params: Unit): List<CartItem> {
        return cartRepository.getCart()
    }
}