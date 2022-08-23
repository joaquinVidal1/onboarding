package com.example.proyecto_final_de_onboarding.mainscreen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.databinding.FragmentMainScreenBinding
import com.example.proyecto_final_de_onboarding.mainscreen.MainScreenFragmentDirections.actionMainScreenFragmentToCheckoutScreenFragment


/**
 * A simple [Fragment] subclass.
 * Use the [MainScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainScreenFragment : Fragment() {

    companion object {
        private const val NUM_PAGES = 4
    }

    private lateinit var viewPager: ViewPager2
    private lateinit var binding: FragmentMainScreenBinding

    private val viewModel: MainScreenViewModel by lazy {
        ViewModelProvider(this).get(MainScreenViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshCart()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = MainScreenAdapter(MainScreenAdapter.AddUnitListener { itemId ->
            viewModel.onAddItem(itemId)
        }, MainScreenAdapter.RemoveUnitListener { itemId -> viewModel.onRemoveItem(itemId) })
        binding.itemsList.adapter = adapter
        adapter.submitList(viewModel.getScreenList())
        viewModel.queriedCart.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(viewModel.getScreenList())
            }

        }
        viewModel.showCartCircle.observe(viewLifecycleOwner) {
            if (it) {
                binding.cartDot.visibility = View.VISIBLE
                binding.cartButton.isEnabled = true
            } else {
                binding.cartDot.visibility = View.GONE
                binding.cartButton.isEnabled = false
            }
        }
        val manager = LinearLayoutManager(activity)
        binding.itemsList.layoutManager = manager
        binding.itemSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.query = binding.itemSearch.text.toString()
                adapter.submitList(viewModel.getScreenList())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.cartButton.setOnClickListener {
            this.findNavController()
                .navigate(actionMainScreenFragmentToCheckoutScreenFragment())
        }
        binding.carrousel.adapter = BannerSlidePagerAdapter(requireActivity())

        //funcion que se llama al cambiar de pagina en el banner
//        val pageChangeCallback = object : ViewPager2.OnPageChangeCallback(){
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                si queres hacer algo dependiendo de la pagina va aca
//            }
//        }
//
//        binding.carrousel.registerOnPageChangeCallback(pageChangeCallback)

        binding.viewPageIndicator.setUpWithViewPager2(binding.carrousel)
        binding.carrousel.setPageTransformer(ZoomOutPageTransformer())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main_screen, container, false
        )

        return binding.root
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
                        else -> getString(R.string.asutralian_kiwis)
                    }
                )

                putString(
                    FeatureCarrouselFragment.ARG_DESCRIPTION,
                    getString(R.string.product_of_the_month)
                )
            }

            return fragment
        }
    }


}



