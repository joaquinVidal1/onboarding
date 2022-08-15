package com.example.proyecto_final_de_onboarding.mainScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto_final_de_onboarding.R
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.ScreenListItem
import com.example.proyecto_final_de_onboarding.databinding.FragmentMainScreenBinding



/**
 * A simple [Fragment] subclass.
 * Use the [MainScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainScreenFragment : Fragment() {

    private val viewModel: MainScreenViewModel by lazy {
        ViewModelProvider(this).get(MainScreenViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMainScreenBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main_screen, container, false)

        binding.mainScreenViewModel = viewModel
        binding.lifecycleOwner = this
        val adapter = MainScreenAdapter(MainScreenAdapter.AddUnitListener{itemId -> viewModel.onAddItem(itemId) }, MainScreenAdapter.RemoveUnitListener{itemId -> viewModel.onRemoveItem(itemId)})
        binding.itemsList.adapter = adapter
        var cart = listOf<CartItem>()
        binding.mainScreenViewModel = viewModel
        viewModel.cart.observe(this, Observer {
            it?.let {
                cart = it
                adapter.submitList(viewModel.getScreenList())
            }

        })
        val manager = LinearLayoutManager(activity)
        binding.itemsList.layoutManager = manager
        binding.itemSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.submitList(viewModel.getScreenList(newText))
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.itemSearch.clearFocus()
                adapter.submitList(viewModel.getScreenList(query))
                return true
            }


        })
        binding.cartButton.setOnClickListener { this.findNavController().navigate(MainScreenFragmentDirections.actionMainScreenFragmentToCheckoutScreenFragment())}//viewModel.onCartClicked() }
        viewModel.navigateToCheckoutScreen.observe(this, Observer { list ->
            list?.let {
                this.findNavController().navigate(MainScreenFragmentDirections.actionMainScreenFragmentToCheckoutScreenFragment())
            }
        })
        Log.e("joaquin", "fin viewModel")
        return binding.root//inflater.inflate(R.layout.fragment_main_screen, container, false)
    }



}



