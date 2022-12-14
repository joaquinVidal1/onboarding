package com.example.proyecto_final_de_onboarding.mainscreen

import androidx.lifecycle.*
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.data.CartRepository
import com.example.proyecto_final_de_onboarding.data.ItemsRepository
import com.example.proyecto_final_de_onboarding.domain.entities.CartItem
import com.example.proyecto_final_de_onboarding.domain.entities.ScreenListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val cart = cartRepository.cart
    // TODO esto que está comentado era innecesario, la separación entre cart y _cart se hace cuando vos tenés que exponer ese valor hacia afuera del ViewModel
//    private val _cart = Transformations.map(cartRepository.cart) { it }
//    private val cart: LiveData<List<CartItem>>
//        get() = _cart

    private val _query = MutableLiveData("")

    private val itemList =
        Transformations.map(itemsRepository.storeItems) {
            it.sortedBy { item -> item.kind }
        }

    val showCartCircle: LiveData<Boolean> =
        Transformations.map(cart) { it.isNotEmpty() }

    val networkError: LiveData<Boolean> =
        Transformations.map(itemsRepository.networkError) { it }

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
        refreshDataFromRepository()
    }

    val displayList: LiveData<List<ScreenListItem>> = Transformations.map(screenItemsList) { list ->
        val resultList = mutableListOf<ScreenListItem>()
        val mapByKind =
            list.groupBy { item -> item.item.kind }
        mapByKind.keys.forEach { kind ->
            resultList.add(ScreenListItem.ScreenHeader(kind))
            resultList.addAll(mapByKind[kind]?.sortedBy { it.item.name }.orEmpty())
        }
        resultList
    }


    private fun shouldBeAdded(query: String, item: Item) =
        item.kind.name.lowercase().contains(query) || item.name.lowercase()
            .contains(query)

    private fun getItemQty(itemId: Int) =
        cart.value?.find { cartItem -> itemId == cartItem.itemId }?.cant ?: 0

    private fun onInputChanged(): List<ScreenListItem.ScreenItem> {
        // TODO personalmente no me gusta usar force unwraps a menos que sea aboslutamente necesario, perfectamente podés hacer _query.value?.let{ ... }
        val query: String = _query.value!!
        val entireList = itemList.value ?: listOf()

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
        viewModelScope.launch {
            cartRepository.addItem(itemId)
        }
    }

    fun onRemoveItem(itemId: Int) {
        viewModelScope.launch {
            cartRepository.removeItem(itemId)
        }
    }

    fun onQueryChanged(query: String) {
        _query.value = query
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            itemsRepository.refreshItems()
        }
    }

    fun networkErrorHandled() {
        itemsRepository.networkErrorHandled()
    }

}




