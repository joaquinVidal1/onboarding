package com.example.proyecto_final_de_onboarding.mainscreen

import androidx.lifecycle.*
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.Kind
import com.example.proyecto_final_de_onboarding.ScreenListItem
import com.example.proyecto_final_de_onboarding.data.CartRepository
import com.example.proyecto_final_de_onboarding.data.CartRepository.cart
import com.example.proyecto_final_de_onboarding.data.ItemRepository

class MainScreenViewModel : ViewModel() {
    val _query = MutableLiveData<String>("")
    val query: LiveData<String>
        get() = _query

    val displayList = listOf<ScreenListItem>()

    private val itemList =
        Transformations.map(ItemRepository.storeItems) { repository -> repository.sortedBy { it.kind } }

    val showCartCircle: LiveData<Boolean> =
        Transformations.map(cart) { it.isNotEmpty() }

    val queriedCart: LiveData<List<CartItem>> =
        Transformations.map(cart) { it.queriedCart(query.value) }

    val screenList = MediatorLiveData<List<ScreenListItem>>()

    init {
        screenList.addSource(cart) { screenList.value = onInputChanged() }
        screenList.addSource(query) { screenList.value = onInputChanged() }
    }

    private fun onInputChanged(): List<ScreenListItem> {
        return itemList.value?.filter {
            query.value?.let { it1 ->
                it.kind.name.contains(it1) || it.name.contains(
                    query.value.toString()
                )
            }
        }!!.map { item ->
            ScreenListItem.ScreenItem(
                item,
                cart.value?.find { cartItem -> item.id == cartItem.itemId }?.cant ?: 0
            )
        }
    }

    fun onAddItem(itemId: Int) {
        CartRepository.addItem(itemId)
    }

    fun onRemoveItem(itemId: Int) {
        CartRepository.removeItem(itemId)
    }

    //returns the list that should be displayed when there has been a query made
    private fun getScreenListQuery(cartList: List<CartItem>, query: String): List<ScreenListItem> {
        val screenList = mutableListOf<ScreenListItem>()
        for (kind in Kind.values()) {
            if (kind.toString().lowercase().contains(query)) {
                screenList.add(ScreenListItem.ScreenHeader(kind))
                screenList.addAll(getFilteredScreenItems(cartList, kind))
            }
        }
        return addByName(cartList, query, screenList)

    }

    //returns a list with all the ScreenItems that match the query and had not been added previously to screenList
    private fun addByName(
        cartList: List<CartItem>,
        query: String,
        screenList: MutableList<ScreenListItem>
    ): List<ScreenListItem> {
        val added = Array(itemList.value!!.size) { false }
        for (item in screenList) {
            if (item is ScreenListItem.ScreenItem)
                added[item.id] = true
        }

        itemList.value?.forEach { item ->
            if ((item.name.lowercase().contains(query)) && (!added[item.id])) {
                val headerIndex =
                    screenList.indexOfFirst { it is ScreenListItem.ScreenHeader && it.kind == item.kind }
                val pos: Int = if (headerIndex < 0) {
                    //add header and item
                    screenList.add(ScreenListItem.ScreenHeader(item.kind))
                    screenList.lastIndex + 1
                } else {
                    headerIndex + 1
                }
                screenList.add(
                    pos, ScreenListItem.ScreenItem(
                        item,
                        cartList.find { it.itemId == item.id }?.cant ?: 0
                    )
                )
            }
        }
        return screenList

    }

    private fun getFilteredScreenItems(cartList: List<CartItem>, kind: Kind): List<ScreenListItem> {
        return itemList.value?.filter { item -> item.kind == kind }
            ?.map { item ->
                ScreenListItem.ScreenItem(item, cartList.find { it.itemId == item.id }?.cant ?: 0)
            } ?: listOf()
    }

    fun onQueryChanged(query: String) {
        _query.value = query

    }

    //gets the final list that should be displayed by the adapter
//    fun getScreenList(): List<ScreenListItem> {
//        val cartList = cart.value
//        val screenList = mutableListOf<ScreenListItem>().apply {
//            add(ScreenListItem.ScreenHeader(Kind.Fruit))
//            addAll(getFilteredScreenItems(cartList, Kind.Fruit))
//            add(ScreenListItem.ScreenHeader(Kind.Veggie))
//            addAll(getFilteredScreenItems(cartList, Kind.Veggie))
//        }
//        return when (query) {
//            null -> {
//                //no query
//                screenList.add(ScreenListItem.ScreenHeader(Kind.Fruit))
//                screenList.addAll(getFilteredScreenItems(cartList, Kind.Fruit))
//                screenList.add(ScreenListItem.ScreenHeader(Kind.Veggie))
//                screenList.addAll(getFilteredScreenItems(cartList, Kind.Veggie))
//                screenList
//            }
//            else -> {
//                getScreenListQuery(cartList, query!!)
//            }
//        }
//    }
}

private fun List<CartItem>.queriedCart(query: String? = null): List<CartItem> {
    if (query != null) {
        this.filter { item ->
            ItemRepository.itemList.find { item.itemId == it.id }?.name?.contains(
                query,
                true
            ) == true ||
                    ItemRepository.itemList.find { item.itemId == it.id }?.kind?.name?.contains(
                        query,
                        true
                    ) == true
        }
    }
    return this
}



