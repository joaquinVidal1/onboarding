package com.example.proyecto_final_de_onboarding.checkoutDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.checkoutScreen.CheckoutScreenViewModel
import com.example.proyecto_final_de_onboarding.databinding.DialogCheckoutScreenBinding

class EditQuantityDialog(val itemId: Int, val viewModel: CheckoutScreenViewModel) :
    NumberPicker {
    private lateinit var binding: DialogCheckoutScreenBinding
    //private lateinit var builder: AlertDialog.Builder
    var cart = listOf<CartItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = viewModel.getScreenListItem(itemId)
        binding.dialogItemName.text = item.item.name
        var cant = item.cant
        binding.dialogItemCant.text = item.cant.toString() + " units"
        binding.dialogItemPrice.text = "$" + item.item.price.toString()
        if (item.item.checkoutImage != null) {
            binding.dialogItemImage.setImageResource(item.item.checkoutImage)
        } else {
            binding.dialogItemImage.setImageResource(item.item.mainImage)
        }
        viewModel.cart.observe(this, Observer {
            it?.let {
                cart = it
            }

        })
        binding.dialogButtonAdd.setOnClickListener {
            viewModel.onAddItem(itemId)
            cant++
            binding.dialogItemCant.text = cant.toString() + " units"
        }
        binding.dialogButtonRemove.setOnClickListener {
            viewModel.onRemoveItem(itemId)
            cant--
            binding.dialogItemCant.text = cant.toString() + " units"
            if (cant <= 0) {
                dismiss()
            }
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




