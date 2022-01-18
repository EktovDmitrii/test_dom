package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemCatalogSubcategoryWithSmallImageBinding
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class CatalogSubcategoriesWithSmallImageAdapter(
    private val onSubCategoryClick: (CatalogSubCategoryModel) -> Unit = {}
) : RecyclerView.Adapter<CatalogSubcategoriesWithSmallImageAdapter.CatalogSubCategoryWithSmallImageViewHolder>() {

    private var subCategories: List<CatalogSubCategoryModel> = listOf()

    override fun onBindViewHolder(holder: CatalogSubCategoryWithSmallImageViewHolder, position: Int) {
        holder.bind(subCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogSubCategoryWithSmallImageViewHolder {
        val binding = ItemCatalogSubcategoryWithSmallImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogSubCategoryWithSmallImageViewHolder(binding, onSubCategoryClick)
    }

    override fun getItemCount(): Int {
        return subCategories.size
    }

    fun setItems(subCategories: List<CatalogSubCategoryModel>){
        this.subCategories = subCategories
        notifyDataSetChanged()
    }

    inner class CatalogSubCategoryWithSmallImageViewHolder(
        private val binding: ItemCatalogSubcategoryWithSmallImageBinding,
        private val onSubCategoryClick: (CatalogSubCategoryModel) -> Unit = {}) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CatalogSubCategoryModel) {
            binding.titleTextView.text = model.title
            binding.subtitleTextView.text = "${model.products.size} видов услуг"

            GlideApp.with(binding.root.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(model.icon))
                .transform(RoundedCorners(8.dp(binding.root.context)))
                .error(R.drawable.rectangle_filled_secondary_100_radius_8dp)
                .into(binding.logoImageView)

            binding.root.setOnDebouncedClickListener {
                onSubCategoryClick(model)
            }
        }

    }
}