package com.example.proyecto_final_de_onboarding.checkoutScreen


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.databinding.FragmentCheckoutScreenBinding

/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckoutScreenFragment : Fragment() {
    private val viewModel: CheckoutScreenViewModel by lazy {
        ViewModelProvider(this).get(CheckoutScreenViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentCheckoutScreenBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_checkout_screen, container, false
        )
        binding.backButton.setOnClickListener {
            this.findNavController().popBackStack()
        }
        binding.lifecycleOwner = this
        var cart = listOf<CartItem>()
        binding.cartItemsList.layoutManager = GridLayoutManager(activity, 2)
        val adapter = CheckoutScreenAdapter(
            CheckoutScreenAdapter.EntireItemListener { itemId ->
                val builder = AlertDialog.Builder(context)
                val numberPicker = NumberPicker(context)
                numberPicker.minValue = 0
                numberPicker.maxValue = 500
                builder.setPositiveButton(getString(R.string.confirm)) { dialog, i ->
                    viewModel.itemQantChanged(itemId, numberPicker.value)

                }
                builder.setNegativeButton(getString(R.string.cancel)) { dialog, i -> }
                builder.setTitle("Edit quantity")
                builder.setView(numberPicker)
                builder.show()

            }
        )

        binding.cartItemsList.adapter = adapter
        // Inflate the layout for this fragment
        //adapter.submitList(viewModel.getScreenList())
        binding.totalAmount.setText("$" + viewModel.getCheckout().toString())
        viewModel.cart.observe(viewLifecycleOwner, Observer {
            it?.let {
                cart = it
                adapter.submitList(viewModel.getScreenList())
                binding.totalAmount.text = "$" + viewModel.getCheckout().toString()
            }

        })
        binding.checkoutButton.setOnClickListener {
            val message = "Total is " + viewModel.getCheckout().toString()
            Toast.makeText( context, message, Toast.LENGTH_SHORT).show()
            viewModel.cleanCart()
            this.findNavController().popBackStack()
        }
        return binding.root//inflater.inflate(R.layout.fragment_checkout_screen, container, false)
    }

}

