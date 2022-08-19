package com.example.proyecto_final_de_onboarding.mainScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.ScreenListItem
import com.example.proyecto_final_de_onboarding.databinding.ListItemMainScreenBinding
import com.example.proyecto_final_de_onboarding.databinding.ListKindMainScreenBinding


private const val ITEM_VIEW_TYPE_SECTION_CONTENT = 1
private const val ITEM_VIEW_TYPE_SECTION_HEADER = 0

class MainScreenAdapter(val addListener: AddUnitListener, val lessListener: RemoveUnitListener) :
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
                binding.buttonAddQuant.visibility = View.GONE
                binding.entireButtonAdd.visibility = View.VISIBLE
            }else{
                binding.entireButtonAdd.visibility = View.GONE
                binding.buttonAddQuant.visibility = View.VISIBLE
            }
            binding.entireButtonAdd.setOnClickListener { addClickListener.onClick(item.item) }
            binding.buttonAdd.setOnClickListener { addClickListener.onClick(item.item) }
            binding.itemImage.setImageResource(item.item.mainImage)
            binding.itemName.setText(item.item.name)
            binding.itemPrice.setText("$" + item.item.price.toString())
            binding.cantText.setText(item.cant.toString())
            binding.buttonRemove.setOnClickListener { removeClickListener.onClick(item.item) }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMainScreenBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

//    sealed class DataItem(){
//        abstract val id: Int
//        data class SectionContent(val item: ScreenListItem.ScreenItem): DataItem(){
//            override val id = item.item.id
//        }
//        data class SectionHeader(val kind: String): DataItem() {
//            override val id = Int.MAX_VALUE
//        }
//
//    }


    class TextViewHolder(val binding: ListKindMainScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(kind: String) {
            binding.kindHeader.setText(kind.toCharArray()[0].uppercase().toString() + kind.subSequence(1, kind.length) +"s")
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
            else -> throw ClassCastException("Unknown viewType ${viewType}")

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
            else -> throw IllegalArgumentException("Unknow ListItem class")
        }
    }

    class MainScreenDiffCallback : DiffUtil.ItemCallback<ScreenListItem>() {
        override fun areContentsTheSame(oldItem: ScreenListItem, newItem: ScreenListItem): Boolean {
            if (oldItem is ScreenListItem.ScreenHeader && newItem is ScreenListItem.ScreenHeader) {
                return oldItem.kind == newItem.kind
            } else {
                if (oldItem is ScreenListItem.ScreenItem && newItem is ScreenListItem.ScreenItem) {
                    return oldItem.id == newItem.id && oldItem.cant == newItem.cant
                } else return false //when one is header and other is item
            }
        }

        override fun areItemsTheSame(oldItem: ScreenListItem, newItem: ScreenListItem): Boolean {
            return oldItem == newItem
        }
    }

//    fun SubmitList(list: List<ScreenListItem>){
////        val newList = mutableListOf<ScreenListItem>()
////        newList.add(0, ScreenListItem.ScreenHeader(Kind.fruit.toString()))
////        Log.e("joaquin", "empieza a crear la lista")
////        //agrego las frutas abajo del header frutas
//////        for (cartItem in list){
//////            val item = viewModel.findItemById(cartItem.itemId)
//////            if (item?.kind == Kind.fruit){
//////                newList.add(ScreenListItem.ScreenItem(item!!, cartItem.cant))
//////            }
//////        }
////        //agrego las verduras
////        newList.add(ScreenListItem.ScreenHeader(Kind.veggie.toString()))
////        for (cartItem in list){
////            val item = viewModel.findItemById(cartItem.itemId)
////            if (item?.kind == Kind.veggie){
////                newList.add(ScreenListItem.ScreenItem(item, cartItem.cant))
////            }
////        }
//        submitList(list)
//    }
}