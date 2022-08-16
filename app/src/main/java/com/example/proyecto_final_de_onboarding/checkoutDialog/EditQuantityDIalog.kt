package com.example.proyecto_final_de_onboarding.checkoutDialog

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.checkoutScreen.CheckoutScreenViewModel
import com.example.proyecto_final_de_onboarding.databinding.DialogCheckoutScreenBinding

class EditQuantityDialog(val itemId: Int, val viewModel: CheckoutScreenViewModel) :
    DialogFragment() {
    var cart = listOf<CartItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        val builder = AlertDialog.Builder(activity)
        //val view = layoutInflater.inflate(R.layout.dialog_checkout_screen, null)
        //builder.setView(view)
        val binding: DialogCheckoutScreenBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_checkout_screen, null, false
        )
        builder.setView(binding.root)
        val item = viewModel.getScreenListItem(itemId)
        binding.itemName.text = item.item.name
        var cant = item.cant
        binding.itemCant.text = item.cant.toString()
        binding.itemPrice.text = item.item.price.toString()
        if (item.item.checkoutImage != null) {
            binding.itemImage.setImageResource(item.item.checkoutImage)
        } else {
            binding.itemImage.setImageResource(item.item.mainImage)
        }
        viewModel.cart.observe(this, Observer {
            it?.let {
                cart = it
            }

        })
        val dialog = builder.create()
        binding.buttonAdd.setOnClickListener {
            viewModel.onAddItem(itemId)
            cant++
            binding.itemCant.text = cant.toString()
        }
        binding.buttonRemove.setOnClickListener {
            viewModel.onRemoveItem(itemId)
            cant--
            binding.itemCant.text = cant.toString()
            if (cant <= 0) {
                dialog.hide()
            }
        }
        super.onCreate(savedInstanceState)
        super.dismiss()
        dialog.show()
    }
}
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        val binding: DialogCheckoutScreenBinding = DataBindingUtil.setContentView(
//            activity!!, R.layout.dialog_checkout_screen)
//        val item = viewModel.getScreenListItem(itemId)
//        binding.itemName.text = item.item.name
//        var cant = item.cant
//        binding.itemCant.text = item.cant.toString()
//        binding.itemPrice.text = item.item.price.toString()
//        if (item.item.checkoutImage != null) {
//            binding.itemImage.setImageResource(item.item.checkoutImage)
//        } else {
//            binding.itemImage.setImageResource(item.item.mainImage)
//        }
//        viewModel.cart.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                cart = it
//            }
//
//        })
//        binding.buttonAdd.setOnClickListener {
//            viewModel.onAddItem(itemId)
//            cant++
//            binding.itemCant.text = cant.toString()
//        }
//        binding.buttonRemove.setOnClickListener {
//            viewModel.onRemoveItem(itemId)
//            cant--
//            binding.itemCant.text = cant.toString()
//        }
//        return inflater.inflate(R.layout.dialog_checkout_screen, container, false)//binding.root
//    }



