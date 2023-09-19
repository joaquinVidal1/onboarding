package com.example.proyecto_final_de_onboarding.presentation.checkoutscreen

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.data.Result
import com.example.proyecto_final_de_onboarding.domain.model.CartItem
import com.example.proyecto_final_de_onboarding.domain.model.Product
import com.example.proyecto_final_de_onboarding.domain.model.ScreenListItem
import com.example.proyecto_final_de_onboarding.domain.model.getRoundedPrice
import com.example.proyecto_final_de_onboarding.domain.usecase.EditQuantityUseCase
import com.example.proyecto_final_de_onboarding.domain.usecase.EmptyCartUseCase
import com.example.proyecto_final_de_onboarding.domain.usecase.GetCartUseCase
import com.example.proyecto_final_de_onboarding.domain.usecase.GetProductsUseCase
import com.example.proyecto_final_de_onboarding.domain.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutScreenViewModel @Inject constructor(
    private val emptyCartUseCase: EmptyCartUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val editQuantityUseCase: EditQuantityUseCase
) : ViewModel(), DefaultLifecycleObserver {

    private val cart = MutableLiveData<List<CartItem>>()

    private val _screenList = MediatorLiveData<List<ScreenListItem.ScreenItem>>()
    val screenList: LiveData<List<ScreenListItem.ScreenItem>> = _screenList

    private val _error = SingleLiveEvent<Int>()
    val error: LiveData<Int> = _error

    private val _showEditQtyDialog = SingleLiveEvent<CartItem>()
    val showEditQtyDialog: LiveData<CartItem> = _showEditQtyDialog

    private val products = liveData<List<Product>> {
        getProductsUseCase(Unit).let {
            emit(
                if (it is Result.Success) it.value else {
                    _error.value = R.string.unable_to_get_products
                    listOf()
                }
            )
        }
    }

    val showCheckoutButton: LiveData<Boolean> = screenList.map { it.isNotEmpty() }

    val totalAmount: LiveData<Double> = screenList.map {
        it.sumOf { item -> item.quantity * item.product.price }
    }

    init {
        _screenList.addSource(cart) {
            _screenList.value = cart.value?.mapNotNull { it.getScreenItem() } ?: listOf()
        }

        _screenList.addSource(products) {
            _screenList.value = cart.value?.mapNotNull { it.getScreenItem() } ?: listOf()
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

    fun itemQtyChanged(productId: Int, newQty: Int) {
        viewModelScope.launch {
            editQuantityUseCase(EditQuantityUseCase.Params(newQty = newQty, productId = productId)).let {
                if (it is Result.Success) {
                    cart.value = it.value
                } else {
                    _error.value = R.string.error_updating_cart
                }
            }

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
                    _error.value = R.string.unable_to_get_cart
                    listOf()
                }
            }

        }
    }

    private fun CartItem.getScreenItem(): ScreenListItem.ScreenItem? {
        return products.value?.firstOrNull { it.id == this.productId }?.let {
            ScreenListItem.ScreenItem(product = it, quantity = this.quantity)
        }
    }

    fun onProductPressed(product: ScreenListItem.ScreenItem) {
        _showEditQtyDialog.value = CartItem(productId = product.id, quantity = product.quantity)
    }

}
