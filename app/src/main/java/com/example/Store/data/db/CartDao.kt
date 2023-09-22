package com.example.proyecto_final_de_onboarding.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.proyecto_final_de_onboarding.domain.model.CartItem

@Dao
interface CartDao {
    @Query("select * from cartTable")
    fun getCartItems(): List<CartItem>

    @Query("SELECT * FROM cartTable WHERE productId = :productId")
    fun getCartItem(productId: Int): CartItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<CartItem>)

    @Query("DELETE FROM cartTable WHERE productId = :productId")
    fun removeFromCartDB(productId: Int)

    @Query("DELETE FROM cartTable")
    fun emptyTable()

    @Query("DELETE FROM cartTable WHERE cartTable.productId NOT IN (SELECT id FROM productsTable)")
    fun removeIfNotInStore()

    @Query("SELECT COUNT(*) > 0 FROM cartTable WHERE productId = :productId")
    fun isProductInCart(productId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(productId: CartItem)

    @Transaction
    suspend fun increaseProductQuantity(productId: Int): List<CartItem> {
        val product = getCartItem(productId)
        product?.let {
            insertProduct(CartItem(productId = productId, quantity = it.quantity + 1))
        } ?: insertProduct(CartItem(productId = productId, quantity = 1))
        return getCartItems()
    }

    @Transaction
    suspend fun decreaseProductQuantity(productId: Int): List<CartItem> {
        getCartItem(productId)?.quantity?.let { qty ->
            if (qty == 1) {
                removeFromCartDB(productId)
            } else {
                insertProduct(CartItem(productId = productId, quantity = qty - 1))
            }
        }
        return getCartItems()
    }

    @Transaction
    suspend fun editProductQuantity(cartItem: CartItem): List<CartItem> {
        if (cartItem.quantity > 0) {
            insertProduct(cartItem)
        } else {
            removeFromCartDB(cartItem.productId)
        }
        return getCartItems()
    }
}
