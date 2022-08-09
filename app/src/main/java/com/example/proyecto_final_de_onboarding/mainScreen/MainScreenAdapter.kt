package com.example.proyecto_final_de_onboarding.mainScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.ScreenItem
import androidx.recyclerview.widget.ListAdapter
import com.example.proyecto_final_de_onboarding.Kind
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.databinding.FragmentMainScreenBinding
import com.example.proyecto_final_de_onboarding.databinding.ListItemMainScreenBinding
import com.example.proyecto_final_de_onboarding.databinding.ListKindMainScreenBinding


private const val ITEM_VIEW_TYPE_SECTION_CONTENT =1
private const val ITEM_VIEW_TYPE_SECTION_HEADER =0

class MainScreenAdapter(val addListener: AddUnitListener, val lessListener: RemoveUnitListener):
    ListAdapter<MainScreenAdapter.DataItem, RecyclerView.ViewHolder>(MainScreenDiffCallback()){

    class AddUnitListener(private val clickListener: (itemId: Int) -> Unit){
        fun onClick(item: Item) = clickListener(item.id)
    }

    class RemoveUnitListener(private val clickListener: (itemId: Int) -> Unit){
        fun onClick(item: Item) = clickListener(item.id)
    }


    class ViewHolder private constructor (val binding: ListItemMainScreenBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: ScreenItem, addClickListener: AddUnitListener, removeClickListener: RemoveUnitListener){
            binding.buttonAdd.setOnClickListener{addClickListener.onClick(item.item)}
            binding.itemImage.setImageResource(item.item.image)
            binding.itemName.setText(item.item.name)
            binding.itemPrice.setText(item.item.price)
            binding.buttonRemove.setOnClickListener { removeClickListener.onClick(item.item) }

        }
        companion object{
            fun from(parent: ViewGroup):ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMainScreenBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    sealed class DataItem(){
        abstract val id: Int
        data class SectionContent(val item: ScreenItem): DataItem(){
            override val id = item.item.id
        }
        data class SectionHeader(val kind: String): DataItem() {
            override val id = Int.MAX_VALUE
        }

    }
    class TextViewHolder(view: View, val binding: ListKindMainScreenBinding): RecyclerView.ViewHolder(view){
        fun bind(kind: String){
            binding.kindHeader.setText(kind)
        }

        companion object{
            fun from(parent: ViewGroup): TextViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_kind_main_screen ,parent, false)
                val binding = ListKindMainScreenBinding.inflate(layoutInflater, parent, false)
                return TextViewHolder(view, binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_TYPE_SECTION_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_SECTION_CONTENT -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is ViewHolder -> {
                val listItem = getItem(position) as DataItem.SectionContent
                holder.bind(listItem.item, addListener, lessListener)
            }
            is TextViewHolder -> {
                val listHeader = getItem(position) as DataItem.SectionHeader
                holder.bind(listHeader.kind)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)){
            is DataItem.SectionHeader -> ITEM_VIEW_TYPE_SECTION_HEADER
            is DataItem.SectionContent -> ITEM_VIEW_TYPE_SECTION_CONTENT
        }
    }

    class MainScreenDiffCallback : DiffUtil.ItemCallback<DataItem>(){
        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }
}