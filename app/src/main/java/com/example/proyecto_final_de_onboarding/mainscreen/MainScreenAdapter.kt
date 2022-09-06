package com.example.proyecto_final_de_onboarding.mainscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.Kind
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.ScreenListItem
import com.example.proyecto_final_de_onboarding.databinding.ListItemMainScreenBinding
import com.example.proyecto_final_de_onboarding.databinding.ListKindMainScreenBinding
import java.math.RoundingMode
import java.text.DecimalFormat


private const val ITEM_VIEW_TYPE_SECTION_CONTENT = 1
private const val ITEM_VIEW_TYPE_SECTION_HEADER = 0

class MainScreenAdapter(
    private val addListener: AddUnitListener,
    private val lessListener: RemoveUnitListener
) :
    ListAdapter<ScreenListItem, RecyclerView.ViewHolder>(MainScreenDiffCallback()) {

    class AddUnitListener(private val clickListener: (itemId: Int) -> Unit) {
        fun onClick(item: Item) = clickListener(item.id)
    }

    class RemoveUnitListener(private val clickListener: (itemId: Int) -> Unit) {
        fun onClick(item: Item) = clickListener(item.id)
    }

    class ViewHolder private constructor(val binding: ListItemMainScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ScreenListItem.ScreenItem,
            addClickListener: AddUnitListener,
            removeClickListener: RemoveUnitListener
        ) {
            if (item.cant == 0) {
                binding.buttonAddQty.visibility = View.GONE
                binding.entireButtonAdd.visibility = View.VISIBLE
            } else {
                binding.entireButtonAdd.visibility = View.GONE
                binding.buttonAddQty.visibility = View.VISIBLE
            }
            binding.entireButtonAdd.setOnClickListener { addClickListener.onClick(item.item) }
            binding.buttonAdd.setOnClickListener { addClickListener.onClick(item.item) }
            Glide.with(binding.itemImage.context)
                .load(item.item.mainImage)
                .placeholder(R.mipmap.main_placeholder)
                .centerInside()
                .into(binding.itemImage)
            binding.itemName.text = item.item.name
            val roundedPrice = "$" + getRoundedPrice(item.item.price)
            binding.itemPrice.text = roundedPrice
            binding.cantText.text = item.cant.toString()
            binding.buttonRemove.setOnClickListener { removeClickListener.onClick(item.item) }
        }

        private fun getRoundedPrice(price: Double): String {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.DOWN
            df.minimumFractionDigits = 2
            return df.format(price)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMainScreenBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class TextViewHolder(val binding: ListKindMainScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {
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
            ITEM_VIEW_TYPE_SECTION_CONTENT -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val listItem = getItem(position) as ScreenListItem.ScreenItem
                holder.bind(listItem, addListener, lessListener)
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
        override fun areContentsTheSame(oldItem: ScreenListItem, newItem: ScreenListItem): Boolean =
            if (oldItem is ScreenListItem.ScreenHeader && newItem is ScreenListItem.ScreenHeader) {
                oldItem.kind == newItem.kind
            } else {
                if (oldItem is ScreenListItem.ScreenItem && newItem is ScreenListItem.ScreenItem) {
                    (oldItem.id == newItem.id) && (oldItem.cant == newItem.cant)
                } else {
                    false //when one is header and other is item
                }
            }

        override fun areItemsTheSame(oldItem: ScreenListItem, newItem: ScreenListItem): Boolean {
            return oldItem == newItem
        }
    }
}

