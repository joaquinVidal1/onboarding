package com.example.proyecto_final_de_onboarding.checkoutscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.ScreenListItem
import com.example.proyecto_final_de_onboarding.data.CartRepository
import com.example.proyecto_final_de_onboarding.data.ItemRepository

class CheckoutScreenViewModel: ViewModel() {
    private val _cart = MutableLiveData<List<CartItem>>(listOf())
    val cart: LiveData<List<CartItem>>
        get() = _cart


    fun getScreenList(): List<ScreenListItem.ScreenItem>{
        val cartList = CartRepository.cart
        val screenList = mutableListOf<ScreenListItem.ScreenItem>()
        for (item in cartList) {
            ItemRepository.itemList.find { it.id == item.itemId }
                ?.let { ScreenListItem.ScreenItem(it, item.cant) }?.let { screenList.add(it) }
        }
        return screenList
    }

    fun getCheckout(): Int {
        val checkoutCart = CartRepository.cart
        return checkoutCart.sumOf { cartItem ->
            cartItem.cant * ItemRepository.itemList.find { it.id == cartItem.itemId }!!.price }

    }


    fun cleanCart() {
        _cart.value = CartRepository.cleanCart()
    }

    fun itemQantChanged(itemId: Int, newQant: Int) {
        _cart.value = CartRepository.editQuantity(itemId, newQant)
    }

    fun getQant(itemId: Int): Int? {
        _cart.value = CartRepository.cart
        return _cart.value?.find { it.itemId == itemId }?.cant
    }

    fun updateCart() {
        _cart.value = CartRepository.cart
    }
}