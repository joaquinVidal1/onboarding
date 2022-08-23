package com.example.proyecto_final_de_onboarding.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.Kind
import com.example.proyecto_final_de_onboarding.ScreenListItem
import com.example.proyecto_final_de_onboarding.data.CartRepository
import com.example.proyecto_final_de_onboarding.data.ItemRepository

class MainScreenViewModel : ViewModel() {
    var query: String? = null
    val itemList = orderList(ItemRepository.itemList)
    private val _cart = MutableLiveData<List<CartItem>>(listOf())
    val cart: LiveData<List<CartItem>>
        get() = _cart

    val showCartCircle: LiveData<Boolean> =
        Transformations.map(cart) { it.isNotEmpty() }

    val queriedCart: LiveData<List<CartItem>> =
        Transformations.map(cart) { it.queriedCart(query) }


    fun onAddItem(itemId: Int) {
        _cart.value = CartRepository.addItem(itemId)
    }

    fun onRemoveItem(itemId: Int) {
        _cart.value = CartRepository.removeItem(itemId)
    }

    //orders the list, first fruits then veggies
    fun orderList(list: List<Item>): List<Item> {
        val newList = mutableListOf<Item>()
        for (item in list) {
            if (item.kind == Kind.fruit) {
                newList.add(item)
            }
        }
        for (item in list) {
            if (item.kind == Kind.veggie) {
                newList.add(item)
            }
        }
        return newList
    }


    //returns the list that should be displayed when there has been a query made
    private fun getScreenListQuery(cartList: List<CartItem>?, query: String): List<ScreenListItem> {
        val screenList = mutableListOf<ScreenListItem>()
        var text = "fruits"
        if (text.contains(query)) {
            screenList.add(ScreenListItem.ScreenHeader(Kind.fruit.toString()))
            screenList.addAll(addItems(cartList, Kind.fruit))
        } else {
            text = "veggie"
            if (text.contains(query)) {
                screenList.add(ScreenListItem.ScreenHeader(Kind.veggie.toString()))
                screenList.addAll(addItems(cartList, Kind.veggie))
            }
        }
        return addByName(cartList, query, screenList)

    }

    //returns a list with all the ScreenItems that match the query and had not been added previously to screenList
    private fun addByName(
        cartList: List<CartItem>?,
        query: String,
        screenList: MutableList<ScreenListItem>
    ): List<ScreenListItem> {
        var added = Array(itemList.size, { false })
        for (item in screenList) {
            if (item is ScreenListItem.ScreenItem)
                added[item.id] = true
        }


        for (item in itemList) {
            if ((item.name.lowercase().contains(query)) && (!added[item.id])) {
                val headerIndex =
                    screenList.indexOfFirst { it is ScreenListItem.ScreenHeader && it.kind == item.kind.toString() }
                if (headerIndex >= 0) {
                    screenList.add(headerIndex + 1, ScreenListItem.ScreenItem(item,
                        if (cartList?.find { it.itemId == item.id } != null)
                            cartList?.find { it.itemId == item.id }!!.cant
                        else 0
                    )
                    )
                } else {
                    //add header and item
                    screenList.add(ScreenListItem.ScreenHeader(item.kind.toString()))
                    screenList.add(ScreenListItem.ScreenItem(item,
                        if (cartList?.find { it.itemId == item.id } != null)
                            cartList?.find { it.itemId == item.id }!!.cant
                        else 0
                    )
                    )


                }

            }
        }
        return screenList

    }


    //returns all the ScreenItems that match the kind specified
    private fun addItems(cartList: List<CartItem>?, kind: Kind): List<ScreenListItem> {
        val screenList = mutableListOf<ScreenListItem>()
        for (item in itemList) {
            if (item.kind == kind) {
                screenList.add(
                    ScreenListItem.ScreenItem(
                        item,
                        cartList?.find { it.itemId == item.id }?.cant ?: 0
                    )
                )
            }
        }
        return screenList
    }

    //gets the final list that should be displayed by the adapter
    fun getScreenList(): List<ScreenListItem> {
        val cartList = CartRepository.cart.queriedCart(query)
        val screenList = mutableListOf<ScreenListItem>()
        if (query == null) {
            //no query
            screenList.add(ScreenListItem.ScreenHeader(Kind.fruit.toString()))
            screenList.addAll(addItems(cartList, Kind.fruit))
            screenList.add(ScreenListItem.ScreenHeader(Kind.veggie.toString()))
            screenList.addAll(addItems(cartList, Kind.veggie))
            return screenList
        } else {
            return getScreenListQuery(cartList, query!!)
        }
    }


    fun refreshCart() {
        _cart.value = CartRepository.cart
    }


}

private fun List<CartItem>.queriedCart(query: String? = null): List<CartItem> {
    if (query == null) {
        this
    } else {
        this.filter { item ->
            ItemRepository.itemList.find { item.itemId == it.id }?.name!!.contains(
                query
            )
        }
    }
    return this

}


