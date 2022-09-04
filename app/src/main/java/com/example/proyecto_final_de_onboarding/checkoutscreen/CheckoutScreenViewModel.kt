package com.example.proyecto_final_de_onboarding.checkoutscreen

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.ScreenListItem
import com.example.proyecto_final_de_onboarding.data.ItemsRepository
import com.example.proyecto_final_de_onboarding.data.getCartRepository
import com.example.proyecto_final_de_onboarding.database.getDatabase

class CheckoutScreenViewModel(application: Application) : ViewModel() {

    private val cartRepository = getCartRepository(getDatabase(application))
    private val itemsRepository = ItemsRepository(getDatabase(application), cartRepository)

    private val storeItems =
        Transformations.map(itemsRepository.storeItems) { repository ->
            repository.sortedBy { it.kind }
        }

    private val _cart = Transformations.map(cartRepository.cart){ it }
    private val cart: LiveData<List<CartItem>>
        get() = _cart

    val screenList: LiveData<List<ScreenListItem.ScreenItem>> =
        Transformations.map(cart) { getScreenList() }

    val showCheckoutButton: LiveData<Boolean> =
        Transformations.map(cart) { it.isNotEmpty() }

    private fun getItemPrice(itemId: Int) = storeItems.value?.find { it.id == itemId }!!.price

    val totalAmount: LiveData<Double> =
        Transformations.map(cart) {
            it.sumOf { item -> item.cant * getItemPrice(item.itemId) }
        }


    private fun getScreenList(): List<ScreenListItem.ScreenItem> {
        val cartList = cart.value
        val screenList = mutableListOf<ScreenListItem.ScreenItem>()
        for (item in cartList!!) {
            itemsRepository.storeItems.value?.find { it.id == item.itemId }
                ?.let { ScreenListItem.ScreenItem(it, item.cant) }?.let { screenList.add(it) }
        }
        return screenList
    }

    fun getCheckout(): Double {
        return totalAmount.value!!
    }

    fun cleanCart() {
        cartRepository.cleanCart()
    }

    fun itemQtyChanged(itemId: Int, newQty: Int) {
        cartRepository.editQuantity(itemId, newQty)
    }

    fun getQty(itemId: Int): Int? {
        return cart.value?.find { it.itemId == itemId }?.cant
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CheckoutScreenViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CheckoutScreenViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
