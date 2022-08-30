package com.example.proyecto_final_de_onboarding.checkoutscreen


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.databinding.FragmentCheckoutScreenBinding


class CheckoutScreenFragment : Fragment() {
    private val viewModel: CheckoutScreenViewModel by lazy {
        ViewModelProvider(this)[CheckoutScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel.updateCart()

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
        // Inflate the layout for this fragment
        viewModel.screenList.observe(viewLifecycleOwner) {
            adapter.submitList(it)

        }
        viewModel.totalAmount.observe(viewLifecycleOwner){
            totalAmount.text = "$"+ it.toString()
        }

        viewModel.showCheckoutButton.observe(viewLifecycleOwner) {
                checkoutButton.isEnabled = it
        }
        binding.checkoutButton.setOnClickListener {
            val message = "Total is " + viewModel.getCheckout().toString()
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.cleanCart()
            this.findNavController().popBackStack()
        }
        return binding.root
    }

}

