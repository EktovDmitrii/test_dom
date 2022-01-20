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

class EditPurchaseServicePropertyAdapter(
    private val onPropertyClick: (PropertyItemModel) -> Unit,
) : RecyclerView.Adapter<EditPurchaseServicePropertyAdapter.EditPurchaseServicePropertyViewHolder>() {

    private var propertyItem = mutableListOf<PropertyItemModel>()

    private var currentPropertyItemModel: PropertyItemModel? = null

    override fun onBindViewHolder(holder: EditPurchaseServicePropertyViewHolder, position: Int) {
        (holder).bind(propertyItem[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditPurchaseServicePropertyViewHolder {
        val binding = ItemPurchaseServicePropertyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EditPurchaseServicePropertyViewHolder(binding, onPropertyClick)
    }

    override fun getItemCount(): Int {
        return propertyItem.size
    }

    fun setItems(
        propertyList: List<PropertyItemModel>,
        selectedPropertyItemModel: PropertyItemModel
    ) {
        propertyItem.clear()
        propertyItem.addAll(propertyList)
        currentPropertyItemModel = selectedPropertyItemModel
        notifyDataSetChanged()
    }

    inner class EditPurchaseServicePropertyViewHolder(
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
                    binding.propertyTypeImageView.setImageResource(R.drawable.ic_type_apartment)
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
