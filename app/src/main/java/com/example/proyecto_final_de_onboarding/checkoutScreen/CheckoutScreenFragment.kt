package com.example.proyecto_final_de_onboarding.checkoutScreen


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto_final_de_onboarding.R
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyecto_final_de_onboarding.databinding.FragmentCheckoutScreenBinding
import com.example.proyecto_final_de_onboarding.data.ItemRepository
import com.example.proyecto_final_de_onboarding.mainScreen.MainScreenAdapter

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
            inflater, R.layout.fragment_checkout_screen, container, false)
        binding.backButton.setOnClickListener { this.findNavController().navigate(CheckoutScreenFragmentDirections.actionCheckoutScreenFragmentToMainScreenFragment()) }
        binding.lifecycleOwner = this
        binding.cartItemsList.layoutManager = GridLayoutManager(activity, 2)
        val adapter = CheckoutScreenAdapter(CheckoutScreenAdapter.AddUnitListener{itemId -> viewModel.onAddItem(itemId)}, CheckoutScreenAdapter.RemoveUnitListener{itemId -> viewModel.onRemoveItem(itemId) })
        binding.cartItemsList.adapter = adapter
        // Inflate the layout for this fragment
        adapter.submitList(viewModel.getScreenList())
        binding.totalAmount.setText("$" + viewModel.getCheckout().toString())
        return binding.root//inflater.inflate(R.layout.fragment_checkout_screen, container, false)
    }

}