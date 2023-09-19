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
import com.example.proyecto_final_de_onboarding.domain.model.getRoundedPrice
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutScreenFragment : Fragment() {

    private val viewModel: CheckoutScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentCheckoutScreenBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_checkout_screen, container, false
        )
        val backButton = binding.backButton
        val checkoutButton = binding.checkoutButton
        val totalAmount = binding.totalAmount
        val cartItemsList = binding.cartItemsList

        backButton.setOnClickListener {
            this.findNavController().popBackStack()
        }
        binding.lifecycleOwner = this
        cartItemsList.layoutManager = GridLayoutManager(activity, 2)
        val adapter = CheckoutScreenAdapter(
            CheckoutScreenAdapter.EntireItemListener { itemId ->
                val builder = AlertDialog.Builder(context)
                val numberPicker = NumberPicker(context)
                numberPicker.minValue = 0
                numberPicker.maxValue = 500
                numberPicker.wrapSelectorWheel = false
                numberPicker.value = viewModel.getQty(itemId)!!
                builder.setPositiveButton(getString(R.string.confirm)) { _, _ ->
                    viewModel.itemQtyChanged(itemId, numberPicker.value)

                }
                builder.setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                builder.setTitle(getString(R.string.dialog_title))
                builder.setView(numberPicker)
                builder.show()
            }
        )

        cartItemsList.adapter = adapter
        viewModel.screenList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.totalAmount.observe(viewLifecycleOwner) {
            val roundedPrice = "$" + getRoundedPrice(it)
            totalAmount.text = roundedPrice
        }

        viewModel.showCheckoutButton.observe(viewLifecycleOwner) {
            checkoutButton.isEnabled = it
        }
        binding.checkoutButton.setOnClickListener {
            val message = "Total is " + viewModel.getCheckout()
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.cleanCart()
            this.findNavController().popBackStack()
        }
        return binding.root
    }

}

