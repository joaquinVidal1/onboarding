package com.example.proyecto_final_de_onboarding.presentation.checkoutscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final_de_onboarding.data.repository.CartRepositoryImpl
import com.example.proyecto_final_de_onboarding.data.repository.ProductsRepositoryImpl
import com.example.proyecto_final_de_onboarding.domain.model.CartItem
import com.example.proyecto_final_de_onboarding.domain.model.ScreenListItem
import com.example.proyecto_final_de_onboarding.domain.model.getRoundedPrice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutScreenViewModel @Inject constructor(

) : ViewModel() {

    private val _cart = MutableLiveData<List<CartItem>>()

    val screenList = _cart.map { cart ->
        cart.map { cartItem ->
            ScreenListItem.ScreenItem(
                itemsRepository.getItem(cartItem.productId),
                cant = cartItem.cant
            )
        }
    }

    val showCheckoutButton: LiveData<Boolean> = screenList.map { it.isNotEmpty() }

    val totalAmount: LiveData<Double> = screenList.map {
        it.sumOf { item -> item.cant * item.item.price }
    }

//    private fun getScreenList(): List<ScreenListItem.ScreenItem> {
//        val cartList = cart.value ?: listOf()
//        return cartList.map { cartItem ->
//            val repoItem = itemsRepository.getItem(cartItem.productId)
//            repoItem.let { ScreenListItem.ScreenItem(it, cartItem.cant) }
//        }
//    }

    fun getCheckout(): String {
        return getRoundedPrice(totalAmount.value!!)
    }

    fun cleanCart() {
        viewModelScope.launch {
            cartRepository.cleanCart()
        }
    }

    fun itemQtyChanged(itemId: Int, newQty: Int) {
        viewModelScope.launch {
//            cartRepository.editQuantity(itemId, newQty)
        }
    }

    fun getQty(itemId: Int): Int {
        return _cart.value?.find { it.productId == itemId }?.cant ?: 0
    }
}
