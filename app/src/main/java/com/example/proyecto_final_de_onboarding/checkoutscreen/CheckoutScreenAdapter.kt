package com.example.proyecto_final_de_onboarding.checkoutscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.ScreenListItem
import com.example.proyecto_final_de_onboarding.databinding.ListItemCheckoutScreenBinding

class CheckoutScreenAdapter(
    private val entireItemListener: EntireItemListener,
) : ListAdapter<ScreenListItem.ScreenItem, RecyclerView.ViewHolder>(CheckoutScreenDiffCallback()) {

    class CheckoutScreenDiffCallback : DiffUtil.ItemCallback<ScreenListItem.ScreenItem>() {
        override fun areItemsTheSame(
            oldItem: ScreenListItem.ScreenItem,
            newItem: ScreenListItem.ScreenItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ScreenListItem.ScreenItem,
            newItem: ScreenListItem.ScreenItem
        ): Boolean {
            return oldItem.id == newItem.id

        }

    }

    class EntireItemListener(private val clickListener: (itemId: Int) -> Unit) {
        fun onClick(item: Item) = clickListener(item.id)
    }


    class ViewHolder private constructor(val binding: ListItemCheckoutScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ScreenListItem.ScreenItem, entireItemClickListener: EntireItemListener) {
            binding.entireItem.setOnClickListener { entireItemClickListener.onClick(item.item) }
            if (item.item.checkoutImage == null) {
                binding.itemImage.setImageResource(item.item.mainImage)
            } else {
                binding.itemImage.setImageResource(item.item.checkoutImage)
            }
            binding.itemName.text = item.item.name
            val itemPriceText = "$" + item.item.price.toString()
            binding.itemPrice.text = itemPriceText
            var itemCantText = "${item.cant} unit"
            if (item.cant > 1) {
                itemCantText = "${item.cant} units"
            }
            binding.itemCant.text = itemCantText
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCheckoutScreenBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val listItem = getItem(position) as ScreenListItem.ScreenItem
        if (holder is ViewHolder) {
            holder.bind(listItem, entireItemListener)
        }
    }
}