package com.example.Store.presentation.mainscreen

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val updateProductsUseCase: UpdateProductsUseCase,
    private val addProductToCart: AddProductToCartUseCase,
    private val removeProductFromCart: RemoveProductFromCartUseCase,
    private val getCartUseCase: GetCartUseCase
) : ViewModel(), DefaultLifecycleObserver {

    private val _cart = MutableStateFlow<List<CartItem>>(listOf())
    private val _query = MutableStateFlow("")

    val products: StateFlow<List<Product>> = flow {
        emit(refreshData())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = listOf()
    )

    val showCartCircle: Flow<Boolean> = _cart.map { it.isNotEmpty() }

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val screenItemsList: Flow<List<ScreenListItem.ScreenItem>> =
        combine(products, _cart, _query) { products, cart, query ->
            products.filter {
                it.matchesQuery(query)
            }.map { item ->
                ScreenListItem.ScreenItem(
                    item, cart.getItemQty(item.id)
                )
            }
        }

    val displayList: Flow<List<ScreenListItem>> = screenItemsList.map { list ->
        list.groupBy { item -> item.product.kind }.entries.map { kind ->
            listOf<ScreenListItem>(ScreenListItem.ScreenHeader(kind.key)) + kind.value.sortedBy { it.product.name }
        }.flatten()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        getCart()
    }

    fun onAddItem(productId: Int) {
        viewModelScope.launch {
            addProductToCart(AddProductToCartUseCase.Params(productId = productId)).let {
                if (it is Result.Success) {
                    _cart.value = it.value
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
                    _cart.value = it.value
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
            _error.value = (response as Result.Error).message ?: "Error"
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

    private fun List<CartItem>.getItemQty(itemId: Int): Int {
        return this.find { it.productId == itemId }?.quantity ?: 0
    }
}