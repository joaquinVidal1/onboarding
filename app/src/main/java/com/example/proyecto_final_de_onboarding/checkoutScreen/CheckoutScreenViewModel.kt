package com.example.proyecto_final_de_onboarding.checkoutScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.ScreenListItem
import com.example.proyecto_final_de_onboarding.data.ItemRepository
import com.example.proyecto_final_de_onboarding.data.ItemRepository.cart
import com.example.proyecto_final_de_onboarding.data.ItemRepository.itemList

class CheckoutScreenViewModel: ViewModel() {
    private val _cart = MutableLiveData<List<CartItem>>(listOf())
    val cart: LiveData<List<CartItem>>
        get() = _cart
    fun onAddItem(itemId: Int) {
        _cart.value = ItemRepository.addItem(itemId)
    }

    fun onRemoveItem(itemId: Int) {
        _cart.value = ItemRepository.removeItem(itemId)
    }

    fun getScreenList(): List<ScreenListItem.ScreenItem>{
        val cartList = ItemRepository.cart
        val screenList = mutableListOf<ScreenListItem.ScreenItem>()
        for (item in cartList) {
            ItemRepository.itemList.find { it.id == item.itemId }
                ?.let { ScreenListItem.ScreenItem(it, item.cant) }?.let { screenList.add(it) }
        }
        return screenList
    }

    fun getCheckout(): Int {
        val checkoutCart = ItemRepository.cart
        return checkoutCart.sumOf { cartItem ->
            cartItem.cant * ItemRepository.itemList.find { it.id == cartItem.itemId }!!.price }

    }

    fun getScreenListItem(itemId: Int): ScreenListItem.ScreenItem {
        return ScreenListItem.ScreenItem(ItemRepository.itemList.find { it.id == itemId }!!, ItemRepository.cart.find { it.itemId == itemId }!!.cant)

    }
}