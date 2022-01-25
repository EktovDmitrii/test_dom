package com.custom.rgs_android_dom.ui.catalog.tabs.availableservices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ItemCatalogAvailableServiceBinding
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.ui.base.BaseViewHolder
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class AvailableServicesAdapter(
    private val onServiceClick: (CatalogSubCategoryModel) -> Unit = {}
) : RecyclerView.Adapter<AvailableServicesAdapter.ViewHolder>() {

    private var services: List<CatalogSubCategoryModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCatalogAvailableServiceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onServiceClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(services[position])
    }

    override fun getItemCount(): Int {
        return services.size
    }

    fun setItems(items: List<CatalogSubCategoryModel>) {
        this.services = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemCatalogAvailableServiceBinding,
        private val onServiceClick: (CatalogSubCategoryModel) -> Unit = {}
    ) : BaseViewHolder<CatalogSubCategoryModel>(binding.root) {

        override fun bind(item: CatalogSubCategoryModel) {
            GlideApp.with(binding.iconImageView.context)
                .load(item.logoMiddle)
                .transform(
                    CenterCrop(),
                    RoundedCorners(8f.dp(binding.iconImageView.context).toInt())
                )
                .error(R.drawable.rectangle_filled_secondary_100_radius_8dp)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.iconImageView)
            binding.serviceLayout.setOnDebouncedClickListener { onServiceClick(item) }
            binding.nameTextView.text = item.name
            binding.quantityTextView.text = item.products.size.toString()
        }
    }
}