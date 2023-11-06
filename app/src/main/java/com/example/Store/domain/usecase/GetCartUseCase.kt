package com.example.proyecto_final_de_onboarding.domain.usecase

import com.example.Store.domain.usecase.base.ObserveUseCase
import com.example.proyecto_final_de_onboarding.domain.model.CartItem
import com.example.proyecto_final_de_onboarding.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(private val cartRepository: CartRepository) :
    ObserveUseCase<List<CartItem>> {

    override fun invoke(): Flow<List<CartItem>> {
        return cartRepository.getCart()
    }
}