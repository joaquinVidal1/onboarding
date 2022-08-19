package com.example.proyecto_final_de_onboarding.mainScreen

import android.os.Bundle
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
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.databinding.FragmentMainScreenBinding



/**
 * A simple [Fragment] subclass.
 * Use the [MainScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainScreenFragment : Fragment() {

    companion object{
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
        binding.mainScreenViewModel = viewModel
        val adapter = MainScreenAdapter(MainScreenAdapter.AddUnitListener{itemId -> viewModel.onAddItem(itemId) }, MainScreenAdapter.RemoveUnitListener{itemId -> viewModel.onRemoveItem(itemId)})
        binding.itemsList.adapter = adapter
        adapter.submitList(viewModel.getScreenList())
        var cart = listOf<CartItem>()
        binding.mainScreenViewModel = viewModel
        viewModel.cart.observe(viewLifecycleOwner) {
            it?.let {
                cart = it
                adapter.submitList(viewModel.getScreenList())
            }

        }
        viewModel.showCartCircle.observe(viewLifecycleOwner) {
            if (it) {
                binding.cartDot.visibility = View.VISIBLE
            } else {
                binding.cartDot.visibility = View.GONE
            }
        }
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
        binding.itemSearch.queryHint = "Search"
        binding.cartButton.setOnClickListener { this.findNavController().navigate(MainScreenFragmentDirections.actionMainScreenFragmentToCheckoutScreenFragment())}//viewModel.onCartClicked() }
//        viewModel.navigateToCheckoutScreen.observe(viewLifecycleOwner, Observer { list ->
//            list?.let {
//                this.findNavController().navigate(MainScreenFragmentDirections.actionMainScreenFragmentToCheckoutScreenFragment())
//            }
//        })
        binding.carrousel.adapter = BannerSlidePagerAdapter(requireActivity())

//        val pageChangeCallback = object : ViewPager2.OnPageChangeCallback(){
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                si queres hacer algo dependiendo de la pagina va aca
//            }
//        }
//
//        binding.carrousel.registerOnPageChangeCallback(pageChangeCallback)

        binding.viewPageIndicator.setUpWithViewPager2(binding.carrousel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(
            inflater, R.layout.fragment_main_screen, container, false)

        return binding.root//inflater.inflate(R.layout.fragment_main_screen, container, false)
    }



    private inner class BannerSlidePagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            val fragment = FeatureCarrouselFragment()
            fragment.arguments = Bundle().apply {
                putInt(FeatureCarrouselFragment.ARG_DRAWABLE_ID, when(position){
                    0-> R.drawable.banner_1
                    1-> R.drawable.banner_2
                    2-> R.drawable.banner_3
                    else->R.drawable.banner_4
                })

                putString(FeatureCarrouselFragment.ARG_TITLE, when(position){
                    0-> getString(R.string.brazilian_bananas)
                    1-> getString(R.string.chinese_grapefruits)
                    2-> getString(R.string.uruguayan_cucumbers)
                    else-> getString(R.string.asutralian_kiwis)
                })

                putString(FeatureCarrouselFragment.ARG_DESCRIPTION, getString(R.string.product_of_the_month))
            }

            return fragment
        }
    }



}



