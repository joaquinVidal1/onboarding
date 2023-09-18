package com.example.proyecto_final_de_onboarding.presentation.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final_de_onboarding.data.Result
import com.example.proyecto_final_de_onboarding.domain.model.CartItem
import com.example.proyecto_final_de_onboarding.domain.model.Product
import com.example.proyecto_final_de_onboarding.domain.model.ScreenListItem
import com.example.proyecto_final_de_onboarding.domain.usecase.AddProductToCartUseCase
import com.example.proyecto_final_de_onboarding.domain.usecase.GetCartUseCase
import com.example.proyecto_final_de_onboarding.domain.usecase.GetProductsUseCase
import com.example.proyecto_final_de_onboarding.domain.usecase.RemoveProductFromCartUseCase
import com.example.proyecto_final_de_onboarding.domain.usecase.UpdateProductsUseCase
import com.example.proyecto_final_de_onboarding.domain.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val updateProductsUseCase: UpdateProductsUseCase,
    private val addProductToCart: AddProductToCartUseCase,
    private val removeProductFromCart: RemoveProductFromCartUseCase,
    private val getCartUseCase: GetCartUseCase
) : ViewModel() {

    private val _cart = MutableLiveData<List<CartItem>>()
    private val cart: LiveData<List<CartItem>>
        get() = _cart

    private val _query = MutableLiveData("")

    val products: LiveData<List<Product>> = liveData {
        emit(refreshData())
    }

    val showCartCircle: LiveData<Boolean> = cart.map { it.isNotEmpty() }

    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String> = _error

    private val screenItemsList = MediatorLiveData<List<ScreenListItem.ScreenItem>>()

    init {
        screenItemsList.addSource(products) {
            screenItemsList.value = onInputChanged()
        }
        screenItemsList.addSource(cart) {
            screenItemsList.value = onInputChanged()
        }
        screenItemsList.addSource(_query) {
            screenItemsList.value = onInputChanged()
        }

        getCart()
    }

    val displayList: LiveData<List<ScreenListItem>> = screenItemsList.map { list ->
        list.groupBy { item -> item.item.kind }.entries.map { kind ->
            listOf<ScreenListItem>(ScreenListItem.ScreenHeader(kind.key)) + kind.value.sortedBy { it.item.name }
        }.flatten()
    }

    private fun getItemQty(itemId: Int) = cart.value?.find { cartItem -> itemId == cartItem.productId }?.cant ?: 0

    private fun onInputChanged(): List<ScreenListItem.ScreenItem> = products.value?.filter {
        it.matchesQuery(_query.value)
    }?.map { item ->
        ScreenListItem.ScreenItem(
            item, getItemQty(item.id)
        )
    } ?: listOf()


    fun onAddItem(productId: Int) {
        viewModelScope.launch {
            addProductToCart(AddProductToCartUseCase.Params(productId = productId)).let {
                if (it is Result.Success) {
                    _cart.value = it.value ?: listOf()
                } else {
                    _error.value = (it as Result.Error).message ?: "Error"
                }
            }
        }
    }

    fun onRemoveItem(productId: Int) {
        viewModelScope.launch {
            removeProductFromCart(RemoveProductFromCartUseCase.Params(productId = productId)).let {
                if (it is Result.Success) {
                    _cart.value = it.value ?: listOf()
                } else {
                    _error.value = (it as Result.Error).message ?: "Error"
                }
            }
        }
    }

    fun onQueryChanged(query: String) {
        _query.value = query
    }

    private suspend fun refreshData(): List<Product> {
        val response = viewModelScope.async {
            updateProductsUseCase(Unit)
            getProductsUseCase(Unit)
        }.await()
        return if (response is Result.Success) {
            response.value
        } else {
            _error.postValue((response as Result.Error).message ?: "Error")
            listOf()
        }
    }

    private fun getCart() {
        viewModelScope.launch {
            _cart.value = getCartUseCase(Unit).let { cart ->
                if (cart is Result.Success) {
                    cart.value
                } else {
                    _error.value = (cart as Result.Error).message ?: "Error"
                    listOf()
                }
            }

        }
    }

}