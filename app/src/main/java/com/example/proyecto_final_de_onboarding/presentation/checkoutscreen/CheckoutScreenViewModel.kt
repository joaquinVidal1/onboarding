package com.example.proyecto_final_de_onboarding.presentation.checkoutscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.data.Result
import com.example.proyecto_final_de_onboarding.domain.model.CartItem
import com.example.proyecto_final_de_onboarding.domain.model.Product
import com.example.proyecto_final_de_onboarding.domain.model.ScreenListItem
import com.example.proyecto_final_de_onboarding.domain.model.getRoundedPrice
import com.example.proyecto_final_de_onboarding.domain.usecase.EmptyCartUseCase
import com.example.proyecto_final_de_onboarding.domain.usecase.GetCartUseCase
import com.example.proyecto_final_de_onboarding.domain.usecase.GetProductsUseCase
import com.example.proyecto_final_de_onboarding.domain.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Application,
    private val emptyCartUseCase: EmptyCartUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val getProductsUseCase: GetProductsUseCase,
) : AndroidViewModel(context), DefaultLifecycleObserver {

    private val cart = MutableLiveData<List<CartItem>>()

    private val _screenList = MediatorLiveData<List<ScreenListItem.ScreenItem>>()
    val screenList: LiveData<List<ScreenListItem.ScreenItem>> = _screenList

    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String> = _error

    private val products = liveData<List<Product>> {
        getProductsUseCase(Unit).let {
            if (it is Result.Success) it.value else {
                _error.value = context.getString(R.string.unable_to_get_products)
                listOf()
            }
        }
    }

    val showCheckoutButton: LiveData<Boolean> = screenList.map { it.isNotEmpty() }

    val totalAmount: LiveData<Double> = screenList.map {
        it.sumOf { item -> item.cant * item.item.price }
    }

    init {
        _screenList.addSource(cart) {
            _screenList.value = cart.value?.mapNotNull { it.getScreenItem() }
        }

        _screenList.addSource(products) {
            _screenList.value = cart.value?.mapNotNull { it.getScreenItem() }
        }
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
        return cart.value?.find { it.productId == itemId }?.quantity ?: 0
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        getCart()
    }

    private fun getCart() {
        viewModelScope.launch {
            cart.value = getCartUseCase(Unit).let { cart ->
                if (cart is Result.Success) {
                    cart.value
                } else {
                    _error.value = (cart as Result.Error).message ?: context.getString(R.string.unable_to_get_cart)
                    listOf()
                }
            }

        }
    }

    private fun CartItem.getScreenItem(): ScreenListItem.ScreenItem? {
        return products.value?.firstOrNull { it.id == this.productId }?.let {
            ScreenListItem.ScreenItem(item = it, cant = this.quantity)
        }
    }

    fun onProductPressed(product: ScreenListItem.ScreenItem) {

    }
}
