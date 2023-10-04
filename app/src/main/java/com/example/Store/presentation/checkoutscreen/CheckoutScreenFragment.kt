package com.example.proyecto_final_de_onboarding.presentation.checkoutscreen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.Store.presentation.StoreTheme
import com.example.Store.presentation.checkoutscreen.components.CheckoutScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutScreenFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                StoreTheme {
                    CheckoutScreen(
                        onBackPressed = {
                            this.findNavController().navigateUp()
                        }
                    )

                }
            }
        }
    }
}



