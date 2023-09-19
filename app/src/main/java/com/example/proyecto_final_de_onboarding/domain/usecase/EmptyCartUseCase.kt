package com.example.proyecto_final_de_onboarding.domain.usecase

import com.example.proyecto_final_de_onboarding.domain.model.CartItem
import com.example.proyecto_final_de_onboarding.domain.repository.CartRepository
import com.example.proyecto_final_de_onboarding.domain.usecase.base.CoroutineUseCase
import javax.inject.Inject

class EmptyCartUseCase @Inject constructor(private val cartRepository: CartRepository) :
    CoroutineUseCase<Unit, Unit>() {

    override suspend fun execute(params: Unit): Unit {
       cartRepository.emptyCart()
    }
}