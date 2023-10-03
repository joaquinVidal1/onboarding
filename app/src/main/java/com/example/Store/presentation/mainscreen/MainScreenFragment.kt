package com.example.proyecto_final_de_onboarding.presentation.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.Store.presentation.StoreTheme
import com.example.Store.presentation.mainscreen.components.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                StoreTheme {
                    MainScreen(onCartPressed = {
                        this@MainScreenFragment.findNavController()
                            .navigate(MainScreenFragmentDirections.actionMainScreenFragmentToCheckoutScreenFragment())
                    })
                }
            }
        }
    }


}