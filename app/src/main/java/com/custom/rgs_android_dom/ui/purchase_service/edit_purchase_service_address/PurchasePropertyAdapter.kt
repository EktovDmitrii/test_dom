package com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_service_address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ItemPurchaseServicePropertyBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.utils.gone
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visible

class PurchasePropertyAdapter(
    private val onPropertyClick: (PropertyItemModel) -> Unit,
) : RecyclerView.Adapter<PurchasePropertyAdapter.PurchasePropertyViewHolder>() {

    private var propertyItems = mutableListOf<PropertyItemModel>()

    private var currentPropertyItemModel: PropertyItemModel? = null

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

    fun setItems(
        propertyList: List<PropertyItemModel>,
        selectedProperty: PropertyItemModel
    ) {
        propertyItems.clear()
        propertyItems.addAll(propertyList)
        currentPropertyItemModel = selectedProperty
        notifyDataSetChanged()
    }

    inner class PurchasePropertyViewHolder(
        private val binding: ItemPurchaseServicePropertyBinding,
        private val onPropertyClick: (PropertyItemModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(property: PropertyItemModel) {
            binding.propertyAddressTextView.text = property.address?.address
            binding.root.setOnDebouncedClickListener {
                onPropertyClick(property)
            }
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
            if (property == currentPropertyItemModel)
                binding.checkImageView.visible()
            else
                binding.checkImageView.gone()
        }

    }
}
