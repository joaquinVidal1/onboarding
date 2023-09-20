package com.example.proyecto_final_de_onboarding.presentation.mainscreen

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.compose.material.MaterialTheme
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.databinding.FragmentMainScreenBinding
import com.example.proyecto_final_de_onboarding.domain.CarrouselPage
import com.example.proyecto_final_de_onboarding.presentation.mainscreen.carrousel.FeatureCarrouselFragment
import com.example.proyecto_final_de_onboarding.presentation.mainscreen.components.ProductsCarrousel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    companion object {
        private const val NUM_PAGES = 4
    }

    private val viewModel: MainScreenViewModel by viewModels()
    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var adapter: MainScreenAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        setUpObservers()
        setUpListeners()
        setUpCarrousel(getCarrouselPages())
    }

    private fun setUpListeners() {
        binding.itemSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.onQueryChanged(binding.itemSearch.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.itemSearch.setOnKeyListener { _, keyCode, keyEvent -> //If the keyEvent is a key-down event on the "enter" button
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                true
            } else false
        }

        binding.cartButton.setOnClickListener {
            this.findNavController()
                .navigate(MainScreenFragmentDirections.actionMainScreenFragmentToCheckoutScreenFragment())
            binding.itemSearch.setText("")
        }
    }

    private fun setUpObservers() {
        viewModel.displayList.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        viewModel.products.observe(viewLifecycleOwner) {}

        viewModel.showCartCircle.observe(viewLifecycleOwner) {
            val cartDot = binding.cartDot
            val cartButton = binding.cartButton
            if (it) {
                cartDot.visibility = View.VISIBLE
                cartButton.isEnabled = true
            } else {
                cartDot.visibility = View.GONE
                cartButton.isEnabled = false
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()

        }

    }


    private fun setUpAdapter() {
        adapter = MainScreenAdapter(onAddUnitPressed = { product ->
            viewModel.onAddItem(product.id)
        }, onRemoveUnitPressed = { product -> viewModel.onRemoveItem(product.id) })
        binding.itemsList.adapter = adapter
        binding.itemsList.layoutManager = LinearLayoutManager(activity)
    }

    private fun hideKeyboard() {
        val manager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

//    private fun setUpCarrousel() {
//        val carrousel = binding.carrousel
//        carrousel.adapter = BannerSlidePagerAdapter(requireActivity())
//        binding.viewPageIndicator.setUpWithViewPager2(carrousel)
//        carrousel.setPageTransformer(ZoomOutPageTransformer())
//    }

    private fun setUpCarrousel(pages: List<CarrouselPage>) {
        binding.composeCarrousel.setContent {
            MaterialTheme {
                ProductsCarrousel(pages = pages)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main_screen, container, false
        )
        return binding.root
    }

    private fun getCarrouselPages(): List<CarrouselPage> {
        return listOf(
            CarrouselPage(
                title = getString(R.string.brazilian_bananas),
                subtitle = getString(R.string.chinese_grapefruits),
                image = R.drawable.banner_1
            ), CarrouselPage(
                title = getString(R.string.chinese_grapefruits),
                subtitle = getString(R.string.chinese_grapefruits),
                image = R.drawable.banner_2
            ), CarrouselPage(
                title = getString(R.string.uruguayan_cucumbers),
                subtitle = getString(R.string.chinese_grapefruits),
                image = R.drawable.banner_3
            ), CarrouselPage(
                title = getString(R.string.australian_kiwis),
                subtitle = getString(R.string.chinese_grapefruits),
                image = R.drawable.banner_4
            )
        )

    }

    private inner class BannerSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            val fragment = FeatureCarrouselFragment()
            fragment.arguments = Bundle().apply {
                putInt(
                    FeatureCarrouselFragment.ARG_DRAWABLE_ID, when (position) {
                        0 -> R.drawable.banner_1
                        1 -> R.drawable.banner_2
                        2 -> R.drawable.banner_3
                        else -> R.drawable.banner_4
                    }
                )

                putString(
                    FeatureCarrouselFragment.ARG_TITLE, when (position) {
                        0 -> getString(R.string.brazilian_bananas)
                        1 -> getString(R.string.chinese_grapefruits)
                        2 -> getString(R.string.uruguayan_cucumbers)
                        else -> getString(R.string.australian_kiwis)
                    }
                )

                putString(
                    FeatureCarrouselFragment.ARG_DESCRIPTION, getString(R.string.product_of_the_month)
                )
            }
            return fragment
        }
    }


}