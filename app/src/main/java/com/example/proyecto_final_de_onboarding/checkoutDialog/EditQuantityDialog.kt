package com.example.proyecto_final_de_onboarding.checkoutDialog

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.proyecto_final_de_onboarding.checkoutScreen.CheckoutScreenViewModel

class EditQuantityDialog(val itemId: Int, val viewModel1: CheckoutScreenViewModel) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("aumentar cantidad?")
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { viewModel, id ->
                        viewModel1.onAddItem(itemId)
                    })
                .setNegativeButton(
                    "Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
                .setTitle("Modify quantity")
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

