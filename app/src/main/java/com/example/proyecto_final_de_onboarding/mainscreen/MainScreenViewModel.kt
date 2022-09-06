package com.example.proyecto_final_de_onboarding.mainscreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.Kind
import com.example.proyecto_final_de_onboarding.ScreenListItem
import com.example.proyecto_final_de_onboarding.data.ItemsRepository
import com.example.proyecto_final_de_onboarding.data.getCartRepository
import com.example.proyecto_final_de_onboarding.database.getItemsDatabase
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat

class MainScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val cartRepository = getCartRepository(getItemsDatabase(application))
    private val itemsRepository = ItemsRepository(getItemsDatabase(application))

    private val _cart = Transformations.map(cartRepository.cart) { it }
    private val cart: LiveData<List<CartItem>>
        get() = _cart

    private val _networkError = MutableLiveData<Boolean>(false)
    val networkError: LiveData<Boolean>
        get() = _networkError

    private val _query = MutableLiveData("")

    private val itemList =
        Transformations.map(itemsRepository.storeItems) {
            it.sortedBy { item -> item.kind }
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
        refreshDataFromRepository()
    }

    val displayList: LiveData<List<ScreenListItem>> = Transformations.map(screenItemsList) { list ->
        val entireList = itemList.value ?: listOf()
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

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainScreenViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainScreenViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                itemsRepository.refreshItems()
            } catch (networkError: Exception) {
                Log.e("refreshItems", "error", networkError)
            }
        }
    }

    fun getRoundedPrice(price: Double): String{
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        return df.format(price)
    }

}




