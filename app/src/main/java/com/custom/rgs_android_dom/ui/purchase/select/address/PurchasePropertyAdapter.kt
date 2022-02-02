package com.custom.rgs_android_dom.ui.purchase.select.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ItemPurchaseServicePropertyBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visibleIf

class PurchasePropertyAdapter(
    private val onPropertyClick: (PropertyItemModel) -> Unit,
) : RecyclerView.Adapter<PurchasePropertyAdapter.PurchasePropertyViewHolder>() {

    private var propertyItems = mutableListOf<PropertyItemModel>()
    private var currentPropertyItem: PropertyItemModel? = null

    override fun onBindViewHolder(holder: PurchasePropertyViewHolder, position: Int) {
        (holder).bind(propertyItems[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PurchasePropertyViewHolder {
        val binding = ItemPurchaseServicePropertyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PurchasePropertyViewHolder(binding, onPropertyClick)
    }

    override fun getItemCount(): Int {
        return propertyItems.size
    }

    fun setItems(selectedProperty: PropertyItemModel?, propertyList: List<PropertyItemModel>) {
        propertyItems.clear()
        propertyItems.addAll(propertyList)
        currentPropertyItem = selectedProperty
        notifyDataSetChanged()
    }

    inner class PurchasePropertyViewHolder(
        private val binding: ItemPurchaseServicePropertyBinding,
        private val onPropertyClick: (PropertyItemModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(property: PropertyItemModel) {
            binding.propertyAddressTextView.text = property.address?.address
            when (property.type) {
                PropertyType.HOUSE -> {
                    binding.propertyTypeImageView.setImageResource(R.drawable.ic_type_home)
                    binding.propertyTypeTextView.text = "Дом"
                }
                PropertyType.APARTMENT -> {
                    binding.propertyTypeImageView.setImageResource(R.drawable.ic_type_apartment_334px)
                    binding.propertyTypeTextView.text = "Квартира"
                }
            }
            binding.checkImageView.visibleIf(property == currentPropertyItem)

            binding.root.setOnDebouncedClickListener {
                onPropertyClick(property)
            }
        }

    }
}
