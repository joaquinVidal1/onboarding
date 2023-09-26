package com.example.proyecto_final_de_onboarding.presentation.checkoutscreen


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.example.Store.presentation.checkoutscreen.CheckoutScreenViewModel
import com.example.Store.presentation.checkoutscreen.components.CheckoutScreen
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.databinding.FragmentCheckoutScreenBinding
import com.example.proyecto_final_de_onboarding.domain.model.CartItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckoutScreenFragment : Fragment() {

    private val viewModel: CheckoutScreenViewModel by viewModels()
    private lateinit var binding: FragmentCheckoutScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val cart by viewModel.screenList.collectAsState(
                    initial = listOf()
                )
                val totalAmount by viewModel.totalAmount.collectAsState(initial = "0.0")
                val enableCheckoutButton: Boolean by viewModel.showCheckoutButton.collectAsState(
                    initial = false
                )

                CheckoutScreen(cart = cart,
                    totalAmount = totalAmount,
                    onBackPressed = { this.findNavController().navigateUp() },
                    onCheckoutPressed = {
                        Toast.makeText(
                            context, getString(
                                R.string.checkout_message,
                                viewModel.getCheckout()
                            ), Toast.LENGTH_SHORT
                        ).show()
                        viewModel.cleanCart()
                        this.findNavController().popBackStack()
                    },
                    isCheckoutButtonEnabled = enableCheckoutButton,
                    onItemPressed = { item ->

                    })

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {

                launch {
                    viewModel.error.collect {
                        Toast.makeText(
                            context, getString(it), Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                launch {
                    viewModel.showEditQtyDialog.collect {
                        showEditQuantityDialog(it)
                    }
                }
            }
        }
    }

    private fun showEditQuantityDialog(cartItem: CartItem) {
        val numberPicker = NumberPicker(context).apply {
            minValue = 0
            maxValue = 500
            wrapSelectorWheel = false
            value = cartItem.quantity
        }

        AlertDialog.Builder(context)
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                viewModel.itemQtyChanged(cartItem.productId, numberPicker.value)
            }.setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .setTitle(getString(R.string.dialog_title)).setView(numberPicker)
            .show()

    }

}



