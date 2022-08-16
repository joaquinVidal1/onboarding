package com.example.proyecto_final_de_onboarding.checkoutScreen

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.ScreenListItem
import com.example.proyecto_final_de_onboarding.databinding.ListItemCheckoutScreenBinding

class CheckoutScreenAdapter(
    val entireItemListener: CheckoutScreenAdapter.EntireItemListener,
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
    class EntireItemListener(private val clickListener: (itemId: Int) -> Unit){
        fun onClick(item: Item) = clickListener(item.id)
    }


    class ViewHolder private constructor (val binding: ListItemCheckoutScreenBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: ScreenListItem.ScreenItem, entireItemClickListener: EntireItemListener){
            binding.entireItem.setOnClickListener{entireItemClickListener.onClick(item.item)}
            if (item.item.checkoutImage == null) {
                binding.itemImage.setImageResource(item.item.mainImage)
            }else{
                binding.itemImage.setImageResource(item.item.checkoutImage)
            }
            binding.itemName.setText(item.item.name)
            binding.itemPrice.setText("$"+item.item.price.toString())
            binding.itemCant.setText(item.cant.toString() + " units")
        }
        companion object{
            fun from(parent: ViewGroup):ViewHolder{
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
        if (holder is ViewHolder){
            holder.bind(listItem, entireItemListener)
        }
    }
}