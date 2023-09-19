package com.example.proyecto_final_de_onboarding.presentation.checkoutscreen

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final_de_onboarding.data.Result
import com.example.proyecto_final_de_onboarding.domain.model.CartItem
import com.example.proyecto_final_de_onboarding.domain.model.ScreenListItem
import com.example.proyecto_final_de_onboarding.domain.model.getRoundedPrice
import com.example.proyecto_final_de_onboarding.domain.usecase.EmptyCartUseCase
import com.example.proyecto_final_de_onboarding.domain.usecase.GetCartUseCase
import com.example.proyecto_final_de_onboarding.domain.usecase.GetProductUseCase
import com.example.proyecto_final_de_onboarding.domain.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutScreenViewModel @Inject constructor(
    private val emptyCartUseCase: EmptyCartUseCase, private val getCartUseCase: GetCartUseCase,
    private val getProductUseCase: GetProductUseCase
) : ViewModel(), DefaultLifecycleObserver {

    private val _cart = MutableLiveData<List<CartItem>>()

    val screenList = _cart.map { cart ->
        cart.map { cartItem ->
            ScreenListItem.ScreenItem(
                (getProductUseCase(GetProductUseCase.Params(cartItem.productId)) as Result.Success).value,
                cant = cartItem.cant
            )
        }
    }

    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String> = _error

    val showCheckoutButton: LiveData<Boolean> = screenList.map { it.isNotEmpty() }

    val totalAmount: LiveData<Double> = screenList.map {
        it.sumOf { item -> item.cant * item.item.price }
    }

    fun getCheckout(): String {
        return getRoundedPrice(totalAmount.value ?: 0.0)
    }

    fun cleanCart() {
        viewModelScope.launch {
            emptyCartUseCase(Unit)
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

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        getCart()
    }

    private fun getCart() {
        viewModelScope.launch {
            _cart.value = getCartUseCase(Unit).let { cart ->
                if (cart is Result.Success) {
                    cart.value
                } else {
                    _error.value = (cart as Result.Error).message ?: "Error"
                    listOf()
                }
            }

        }
    }
}
