package com.example.proyecto_final_de_onboarding.checkoutscreen

import android.app.Application
import androidx.lifecycle.*
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.ScreenListItem
import com.example.proyecto_final_de_onboarding.data.ItemsRepository
import com.example.proyecto_final_de_onboarding.data.getCartRepository
import com.example.proyecto_final_de_onboarding.database.getItemsDatabase
import com.example.proyecto_final_de_onboarding.getRoundedPrice
import kotlinx.coroutines.launch

class CheckoutScreenViewModel(application: Application) : ViewModel() {

    private val cartRepository = getCartRepository(getItemsDatabase(application))
    private val itemsRepository = ItemsRepository(getItemsDatabase(application))

    private val storeItems =
        Transformations.map(itemsRepository.storeItems) {
            it ?: listOf()
        }

    private val _cart = Transformations.map(cartRepository.cart){ it }
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

//    private fun getDisplayPrice(price: Double) : String{
//        val df = DecimalFormat("#.##")
//        df.roundingMode = RoundingMode.DOWN
//        return df.format(price)
//    }

    val totalAmount: LiveData<Double> =
        Transformations.map(screenList) {
            it.sumOf { item -> item.cant * item.item.price}
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
