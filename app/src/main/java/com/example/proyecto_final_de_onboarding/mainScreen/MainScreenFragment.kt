package com.example.proyecto_final_de_onboarding.mainScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto_final_de_onboarding.R
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
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

        viewModel.cart.observe(this, Observer {
            viewModel.onAddItem(0)
            //avisarle al adapter, si la cant > 0 mostrar un boton, sino el otro con la cant actualizada

        })
        val manager = LinearLayoutManager(activity)
        binding.itemsList.layoutManager = manager

        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }


}