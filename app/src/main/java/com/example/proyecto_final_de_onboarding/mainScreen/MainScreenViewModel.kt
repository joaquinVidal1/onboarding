package com.example.proyecto_final_de_onboarding.mainScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.data.ItemRepository

class MainScreenViewModel : ViewModel() {
    val itemList = ItemRepository.itemList
    private val _cart = MutableLiveData<List<CartItem>>(listOf())
    val cart: LiveData<List<CartItem>>
        get() = _cart

    fun onAddItem(itemId :Int) {
        var found = false
        _cart.value = _cart.value?.map {
            if (it.itemId == itemId){
                found = true
                it.increment()
            }else it
        }
        if (!found){
            _cart.value = _cart.value?.toMutableList()?.apply {
                add(CartItem(itemId, 1))
            }?.toList()
        }

    }
    fun onRemoveItem(itemId: Int) {
        _cart.value = _cart.value?.map {
            if (it.itemId == itemId) {
                it.decrement()
            } else it
        }
    }

}