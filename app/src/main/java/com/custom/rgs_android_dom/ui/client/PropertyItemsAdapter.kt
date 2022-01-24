package com.custom.rgs_android_dom.ui.client

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ItemAddPropertyItemBinding
import com.custom.rgs_android_dom.databinding.ItemPropertyItemBinding
import com.custom.rgs_android_dom.domain.property.models.AddPropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.ui.base.BaseViewHolder
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class PropertyItemsAdapter(
    private val onPropertyItemClick: (PropertyItemModel) -> Unit = {},
    private val onAddPropertyClick: () -> Unit = {}
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    companion object {
        private const val TYPE_PROPERTY_ITEM = 1
        private const val TYPE_ADD_PROPERTY_ITEM = 2
    }

    private var propertyItems: List<Any> = listOf()

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = propertyItems[position]
        when (holder) {
            is PropertyItemViewHolder -> holder.bind(element as PropertyItemModel)
            is AddPropertyViewHolder -> holder.bind(element as AddPropertyItemModel)
            else -> throw IllegalArgumentException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_PROPERTY_ITEM -> {
                val binding = ItemPropertyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PropertyItemViewHolder(binding, onPropertyItemClick)
            }
            TYPE_ADD_PROPERTY_ITEM -> {
                val binding = ItemAddPropertyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AddPropertyViewHolder(binding, onAddPropertyClick)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (propertyItems[position]) {
            is PropertyItemModel -> TYPE_PROPERTY_ITEM
            is AddPropertyItemModel -> TYPE_ADD_PROPERTY_ITEM
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    override fun getItemCount(): Int {
        return propertyItems.size
    }

    fun setItems(items: List<Any>){
        this.propertyItems = items
        notifyDataSetChanged()
    }

    inner class PropertyItemViewHolder(
        private val binding: ItemPropertyItemBinding,
        private val onPropertyItemClick: (PropertyItemModel) -> Unit = {}) : BaseViewHolder<PropertyItemModel>(binding.root) {

        override fun bind(item: PropertyItemModel) {
            binding.nameTextView.text = item.name
            when (item.type){
                PropertyType.HOUSE -> {
                    binding.propertyImageView.setImageResource(R.drawable.ic_type_home)
                }
                PropertyType.APARTMENT -> {
                    binding.propertyImageView.setImageResource(R.drawable.ic_type_apartment_124x144px)
                }
            }
            binding.containerFrameLayout.setOnDebouncedClickListener {
                onPropertyItemClick(item)
            }
        }
    }

    inner class AddPropertyViewHolder(
        private val binding: ItemAddPropertyItemBinding,
        private val onAddPropertyClick: () -> Unit = {}) : BaseViewHolder<AddPropertyItemModel>(binding.root) {

        override fun bind(item: AddPropertyItemModel) {
            binding.addPropertyFrameLayout.setOnDebouncedClickListener {
                onAddPropertyClick()
            }
        }
    }

}