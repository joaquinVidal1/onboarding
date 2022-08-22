package com.example.proyecto_final_de_onboarding.checkoutDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.checkoutScreen.CheckoutScreenViewModel
import com.example.proyecto_final_de_onboarding.databinding.DialogCheckoutScreenBinding

class EditQuantityDialog(val itemId: Int, val viewModel: CheckoutScreenViewModel) :
    DialogFragment() {
    private lateinit var binding: DialogCheckoutScreenBinding
    //private lateinit var builder: AlertDialog.Builder
    var cart = listOf<CartItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = viewModel.getScreenListItem(itemId)
        var cant = item.cant

        viewModel.cart.observe(this, Observer {
            it?.let {
                cart = it
            }

        })
        binding.numberPicker.minValue =0
        binding.numberPicker.maxValue = Int.MAX_VALUE
        binding.numberPicker.setOnValueChangedListener { itemId ->
            viewModel.onAddItem(itemId)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_checkout_screen, container, false
        )

        return binding.root
    }
}




