package com.example.proyecto_final_de_onboarding.presentation.mainscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.databinding.ListItemMainScreenBinding
import com.example.proyecto_final_de_onboarding.databinding.ListKindMainScreenBinding
import com.example.proyecto_final_de_onboarding.domain.model.Kind
import com.example.proyecto_final_de_onboarding.domain.model.Product
import com.example.proyecto_final_de_onboarding.domain.model.ScreenListItem

class MainScreenAdapter(
    private val onAddUnitPressed: (Product) -> Unit, private val onRemoveUnitPressed: (Product) -> Unit
) : ListAdapter<ScreenListItem, RecyclerView.ViewHolder>(MainScreenDiffCallback()) {
    class ProductViewHolder private constructor(val binding: ListItemMainScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ScreenListItem.ScreenItem, addClickListener: (Product) -> Unit, removeClickListener: (Product) -> Unit
        ) {
            if (item.quantity == 0) {
                binding.buttonAddQty.visibility = View.GONE
                binding.entireButtonAdd.visibility = View.VISIBLE
            } else {
                binding.entireButtonAdd.visibility = View.GONE
                binding.buttonAddQty.visibility = View.VISIBLE
            }
            binding.entireButtonAdd.setOnClickListener { addClickListener(item.product) }
            binding.buttonAdd.setOnClickListener { addClickListener(item.product) }
            Glide.with(binding.itemImage.context).load(item.product.mainImage).placeholder(R.mipmap.main_placeholder)
                .centerInside().into(binding.itemImage)
            binding.itemName.text = item.product.name
            binding.itemPrice.text = binding.root.context.getString(R.string.price, item.product.roundedPrice)
            binding.cantText.text = item.quantity.toString()
            binding.buttonRemove.setOnClickListener { removeClickListener(item.product) }
        }

        companion object {
            fun from(parent: ViewGroup): ProductViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMainScreenBinding.inflate(layoutInflater, parent, false)
                return ProductViewHolder(binding)
            }
        }
    }

    class TextViewHolder(val binding: ListKindMainScreenBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(kind: Kind) {
            binding.kindHeader.text = kind.header
        }

        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListKindMainScreenBinding.inflate(layoutInflater, parent, false)
                return TextViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_SECTION_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_SECTION_CONTENT -> ProductViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProductViewHolder -> {
                val listItem = getItem(position) as ScreenListItem.ScreenItem
                holder.bind(listItem, onAddUnitPressed, onRemoveUnitPressed)
            }

            is TextViewHolder -> {
                val listHeader = getItem(position) as ScreenListItem.ScreenHeader
                holder.bind(listHeader.kind)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ScreenListItem.ScreenHeader -> ITEM_VIEW_TYPE_SECTION_HEADER
            is ScreenListItem.ScreenItem -> ITEM_VIEW_TYPE_SECTION_CONTENT
            else -> throw IllegalArgumentException("Unknown ListItem class")
        }
    }

    class MainScreenDiffCallback : DiffUtil.ItemCallback<ScreenListItem>() {
        override fun areContentsTheSame(oldItem: ScreenListItem, newItem: ScreenListItem): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: ScreenListItem, newItem: ScreenListItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    companion object {
        const val ITEM_VIEW_TYPE_SECTION_CONTENT = 1
        const val ITEM_VIEW_TYPE_SECTION_HEADER = 0
    }
}