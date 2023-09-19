package com.example.proyecto_final_de_onboarding.presentation.checkoutscreen


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.databinding.FragmentCheckoutScreenBinding
import com.example.proyecto_final_de_onboarding.domain.model.CartItem
import com.example.proyecto_final_de_onboarding.domain.model.getRoundedPrice
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutScreenFragment : Fragment() {

    private val viewModel: CheckoutScreenViewModel by viewModels()
    private lateinit var binding: FragmentCheckoutScreenBinding
    private lateinit var adapter: CheckoutScreenAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_checkout_screen, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
        setUpAdapter()
        setUpObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }

    private fun setUpAdapter() {
        adapter = CheckoutScreenAdapter { viewModel.onProductPressed(it) }

        binding.cartItemsList.also {
            it.adapter = adapter
            it.layoutManager = GridLayoutManager(activity, 2)
        }

    }

    private fun setUpListeners() {
        binding.backButton.setOnClickListener {
            this.findNavController().popBackStack()
        }

//        TODO use placeholders
        binding.checkoutButton.setOnClickListener {
            val message = "Total is " + viewModel.getCheckout()
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.cleanCart()
            this.findNavController().popBackStack()
        }
    }

    private fun setUpObservers() {
        viewModel.screenList.observe(viewLifecycleOwner) {
            it?.let { adapter.submitList(it) }
        }

        viewModel.totalAmount.observe(viewLifecycleOwner) {
            val roundedPrice = "$ ${getRoundedPrice(it)}"
            binding.totalAmount.text = roundedPrice
        }

        viewModel.showCheckoutButton.observe(viewLifecycleOwner) {
            binding.checkoutButton.isEnabled = it
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, getString(it), Toast.LENGTH_SHORT).show()
        }

        viewModel.showEditQtyDialog.observe(viewLifecycleOwner) {
            showEditQuantityDialog(it)
        }
    }

    private fun showEditQuantityDialog(cartItem: CartItem) {
        val numberPicker = NumberPicker(context).apply {
            minValue = 0
            maxValue = 500
            wrapSelectorWheel = false
            value = cartItem.quantity
        }

        AlertDialog.Builder(context).setPositiveButton(getString(R.string.confirm)) { _, _ ->
            viewModel.itemQtyChanged(cartItem.productId, numberPicker.value)
        }.setNegativeButton(getString(R.string.cancel)) { _, _ -> }.setTitle(getString(R.string.dialog_title))
            .setView(numberPicker).show()

    }

}



