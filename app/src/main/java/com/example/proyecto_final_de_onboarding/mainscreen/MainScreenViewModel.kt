package com.example.proyecto_final_de_onboarding.mainscreen

import androidx.lifecycle.*
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.Kind
import com.example.proyecto_final_de_onboarding.ScreenListItem
import com.example.proyecto_final_de_onboarding.data.CartRepository
import com.example.proyecto_final_de_onboarding.data.CartRepository.cart
import com.example.proyecto_final_de_onboarding.data.ItemRepository.storeItems

class MainScreenViewModel : ViewModel() {
    private val _query = MutableLiveData("")

    private val itemList =
        Transformations.map(storeItems) { repository ->
            repository.sortedBy { it.kind }
        }

    val showCartCircle: LiveData<Boolean> =
        Transformations.map(cart) { it.isNotEmpty() }

    private val screenItemsList = MediatorLiveData<List<ScreenListItem.ScreenItem>>()

    init {
        screenItemsList.addSource(itemList) {
            screenItemsList.value = onInputChanged()
        }
        screenItemsList.addSource(cart) {
            screenItemsList.value = onInputChanged()
        }
        screenItemsList.addSource(_query) {
            screenItemsList.value = onInputChanged()
        }
    }

    val displayList: LiveData<List<ScreenListItem>> = Transformations.map(screenItemsList) { list ->
        val entireList = itemList.value!!
        val flattenedList: MutableList<ScreenListItem> =
            list.groupBy { item -> entireList.find { storeItem -> storeItem.id == item.id }!!.kind }.values.toList()
                .flatten().toMutableList()
        Kind.values().forEach { kind ->
            val index =
                list.indexOfFirst { item -> entireList.find { storeItem -> storeItem.id == item.id }!!.kind == kind }
            if (index >= 0) {
                flattenedList.add(index, ScreenListItem.ScreenHeader(kind))
            }
        }
        flattenedList
    }

    private fun shouldBeAdded(query: String, item: Item) =
        item.kind.name.lowercase().contains(query) || item.name.lowercase()
            .contains(query)

    private fun getItemQty(itemId: Int) =
        cart.value?.find { cartItem -> itemId == cartItem.itemId }?.cant ?: 0

    private fun onInputChanged(): List<ScreenListItem.ScreenItem> {
        val query: String = _query.value!!
        val entireList = itemList.value!!

        return entireList.filter { item ->
            when (query) {
                "" -> true
                else -> shouldBeAdded(query, item)
            }
        }.map { item ->
            ScreenListItem.ScreenItem(
                item,
                getItemQty(item.id)
            )
        }

    }

    fun onAddItem(itemId: Int) {
        CartRepository.addItem(itemId)
    }

    fun onRemoveItem(itemId: Int) {
        CartRepository.removeItem(itemId)
    }

    fun onQueryChanged(query: String) {
        _query.value = query
    }

}




