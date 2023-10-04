package com.example.Store.presentation.checkoutscreen

import androidx.lifecycle.ViewModel
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutScreenViewModel @Inject constructor(
    private val emptyCartUseCase: EmptyCartUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val editQuantityUseCase: EditQuantityUseCase
) : ViewModel() {

    private val cart = MutableStateFlow<List<CartItem>>(listOf())

    private val products = flow<List<Product>> {
        getProductsUseCase(Unit).let {
            emit(
                if (it is Result.Success) it.value else {
                    _error.emit(R.string.unable_to_get_products)
                    listOf()
                }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = listOf(),
        started = SharingStarted.WhileSubscribed(500)
    )

    val screenList: Flow<List<ScreenListItem.ScreenItem>> =
        combine(cart, products) { cart, products ->
            cart.mapNotNull { it.getScreenItem(products) }
        }

    private val _error = MutableSharedFlow<Int>()
    val error: Flow<Int> = _error

    private val _showEditQtyDialog = MutableSharedFlow<CartItem>()
    val showEditQtyDialog: Flow<CartItem> = _showEditQtyDialog

    val showCheckoutButton: Flow<Boolean> = screenList.map { it.isNotEmpty() }

    val totalAmount: StateFlow<String> = screenList.map {
        it.sumOf { item -> item.quantity * item.product.price }.getRoundedPrice()
    }.stateIn(
        scope = viewModelScope,
        initialValue = "0.0",
        started = SharingStarted.WhileSubscribed(500)
    )

    init {
        getCart()
    }

    fun getCheckout(): String {
        return totalAmount.value
    }

    fun cleanCart() {
        viewModelScope.launch {
            emptyCartUseCase(Unit)
        }
    }

    fun itemQtyChanged(productId: Int, newQty: Int) {
        viewModelScope.launch {
            editQuantityUseCase(
                EditQuantityUseCase.Params(
                    newQty = newQty, productId = productId
                )
            ).let {
                if (it is Result.Success) {
                    cart.value = it.value
                } else {
                    _error.emit(R.string.error_updating_cart)
                }
            }

        }
    }

    private fun getCart() {
        viewModelScope.launch {
            cart.value = getCartUseCase(Unit).let { cart ->
                if (cart is Result.Success) {
                    cart.value
                } else {
                    _error.emit(R.string.unable_to_get_cart)
                    listOf()
                }
            }

        }
    }

    private fun CartItem.getScreenItem(products: List<Product>): ScreenListItem.ScreenItem? {
        return products.firstOrNull { it.id == this.productId }?.let {
            ScreenListItem.ScreenItem(product = it, quantity = this.quantity)
        }
    }

}
