package com.example.proyecto_final_de_onboarding.checkoutscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.proyecto_final_de_onboarding.ScreenListItem
import com.example.proyecto_final_de_onboarding.data.CartRepository
import com.example.proyecto_final_de_onboarding.data.CartRepository.cart
import com.example.proyecto_final_de_onboarding.data.ItemRepository
import com.example.proyecto_final_de_onboarding.data.ItemRepository.storeItems

class CheckoutScreenViewModel : ViewModel() {

    val screenList: LiveData<List<ScreenListItem.ScreenItem>> =
        Transformations.map(cart) { getScreenList() }

    val showCheckoutButton: LiveData<Boolean> =
        Transformations.map(cart) { it.isNotEmpty() }

    val totalAmount: LiveData<Int> =
        Transformations.map(cart) {
            it.sumOf { item -> item.cant * storeItems.value!!.find { storeItem -> storeItem.id == item.itemId }!!.price }
        }


    private fun getScreenList(): List<ScreenListItem.ScreenItem> {
        val cartList = cart.value
        val screenList = mutableListOf<ScreenListItem.ScreenItem>()
        for (item in cartList!!) {
            ItemRepository.itemList.find { it.id == item.itemId }
                ?.let { ScreenListItem.ScreenItem(it, item.cant) }?.let { screenList.add(it) }
        }
        return screenList
    }

    fun getCheckout(): Int {
        return cart.value!!.sumOf { cartItem ->
            cartItem.cant * ItemRepository.itemList.find { it.id == cartItem.itemId }!!.price
        }

    }


    fun cleanCart() {
        CartRepository.cleanCart()
    }

    fun itemQtyChanged(itemId: Int, newQty: Int) {
        CartRepository.editQuantity(itemId, newQty)
    }

    fun getQty(itemId: Int): Int? {
        return cart.value?.find { it.itemId == itemId }?.cant
    }

}