package com.custom.rgs_android_dom.ui.purchase.select.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemPurchaseServicePropertyBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
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
            binding.propertyNameTextView.text = property.name

            if (property.photoLink != null) {
                GlideApp.with(binding.propertyTypeImageView.context)
                    .load(GlideUrlProvider.makeHeadersGlideUrl(property.photoLink))
                    .transform(RoundedCorners(16.dp(binding.propertyTypeImageView.context)))
                    .into(binding.propertyTypeImageView)
            } else {
                when (property.type) {
                    PropertyType.HOUSE -> {
                        GlideApp.with(binding.propertyTypeImageView.context)
                            .load(R.drawable.ic_type_home)
                            .transform(RoundedCorners(16.dp(binding.propertyTypeImageView.context)))
                            .into(binding.propertyTypeImageView)
                    }
                    PropertyType.APARTMENT -> {
                        GlideApp.with(binding.propertyTypeImageView.context)
                            .load(R.drawable.ic_type_apartment_334px)
                            .transform(RoundedCorners(16.dp(binding.propertyTypeImageView.context)))
                            .into(binding.propertyTypeImageView)
                    }
                }
            }
            binding.checkImageView.visibleIf(property == currentPropertyItem)

            binding.root.setOnDebouncedClickListener {
                onPropertyClick(property)
            }
        }

    }
}
