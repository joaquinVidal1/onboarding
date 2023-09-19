package com.example.proyecto_final_de_onboarding.presentation.checkoutscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.databinding.ListItemCheckoutScreenBinding
import com.example.proyecto_final_de_onboarding.domain.model.ScreenListItem
import com.example.proyecto_final_de_onboarding.domain.model.getRoundedPrice

class CheckoutScreenAdapter(
    private val onItemPressed: (ScreenListItem.ScreenItem) -> Unit,
) : ListAdapter<ScreenListItem.ScreenItem, RecyclerView.ViewHolder>(CheckoutScreenDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemCheckoutScreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val listItem = getItem(position) as ScreenListItem.ScreenItem
        if (holder is CartProductViewHolder) {
            holder.bind(listItem)
        }
    }

    class CheckoutScreenDiffCallback : DiffUtil.ItemCallback<ScreenListItem.ScreenItem>() {
        override fun areItemsTheSame(
            oldItem: ScreenListItem.ScreenItem, newItem: ScreenListItem.ScreenItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ScreenListItem.ScreenItem, newItem: ScreenListItem.ScreenItem
        ): Boolean {
            return oldItem.id == newItem.id

        }

    }

    inner class CartProductViewHolder(private val binding: ListItemCheckoutScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ScreenListItem.ScreenItem) {
            binding.entireItem.setOnClickListener { onItemPressed(item) }

            Glide.with(binding.entireItem.context).load(item.item.checkoutImage).placeholder(R.mipmap.main_placeholder)
                .centerCrop().into(binding.itemImage)

            binding.itemName.text = item.item.name

            val itemPrice = "$${getRoundedPrice(item.item.price)}"
            binding.itemPrice.text = itemPrice

            val itemCantText = if (item.cant == 1) "${item.cant} unit" else "${item.cant} units"
            binding.itemCant.text = itemCantText
        }

    }
}