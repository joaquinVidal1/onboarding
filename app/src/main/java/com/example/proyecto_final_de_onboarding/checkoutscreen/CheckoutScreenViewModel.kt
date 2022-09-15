package com.example.proyecto_final_de_onboarding.checkoutscreen

import androidx.lifecycle.*
import com.example.proyecto_final_de_onboarding.data.CartRepository
import com.example.proyecto_final_de_onboarding.data.ItemsRepository
import com.example.proyecto_final_de_onboarding.domain.entities.CartItem
import com.example.proyecto_final_de_onboarding.domain.entities.ScreenListItem
import com.example.proyecto_final_de_onboarding.getRoundedPrice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutScreenViewModel @Inject constructor(private val itemsRepository: ItemsRepository, private val cartRepository: CartRepository) : ViewModel() {

    private val storeItems =
        Transformations.map(itemsRepository.storeItems) {
            it ?: listOf()
        }

    private val _cart = Transformations.map(cartRepository.cart) { it }
    private val cart: LiveData<List<CartItem>>
        get() = _cart

    val screenList = MediatorLiveData<List<ScreenListItem.ScreenItem>>()

    init {
        screenList.addSource(storeItems) {
            screenList.value = getScreenList()
        }
        screenList.addSource(cart) {
            screenList.value = getScreenList()
        }
    }

    val showCheckoutButton: LiveData<Boolean> =
        Transformations.map(screenList) { it.isNotEmpty() }

    val totalAmount: LiveData<Double> =
        Transformations.map(screenList) {
            it.sumOf { item -> item.cant * item.item.price }
        }

    private fun getScreenList(): List<ScreenListItem.ScreenItem> {
        val cartList = cart.value ?: listOf()
        return cartList.mapNotNull { cartItem ->
            val repoItem = itemsRepository.getItem(cartItem.itemId)
            repoItem?.let { ScreenListItem.ScreenItem(it, cartItem.cant) }
        }
    }

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
            cartRepository.editQuantity(itemId, newQty)
        }
    }

    fun getQty(itemId: Int): Int? {
        return cart.value?.find { it.itemId == itemId }?.cant
    }
}
