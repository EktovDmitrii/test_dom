package com.custom.rgs_android_dom.ui.catalog.subcategories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemCatalogPrimaryBigSubcategoryBinding
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class CatalogPrimarySubcategoriesAdapter(
    private val onSubCategoryClick: (CatalogSubCategoryModel) -> Unit = {}
) : RecyclerView.Adapter<CatalogPrimarySubcategoriesAdapter.CatalogPrimarySubcategoryViewHolder>() {

    private var subCategories: List<CatalogSubCategoryModel> = listOf()

    override fun onBindViewHolder(holder: CatalogPrimarySubcategoryViewHolder, position: Int) {
        holder.bind(subCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogPrimarySubcategoryViewHolder {
        val binding = ItemCatalogPrimaryBigSubcategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogPrimarySubcategoryViewHolder(binding, onSubCategoryClick)
    }

    override fun getItemCount(): Int {
        return subCategories.size
    }

    fun setItems(subCategories: List<CatalogSubCategoryModel>){
        this.subCategories = subCategories
        notifyDataSetChanged()
    }

    inner class CatalogPrimarySubcategoryViewHolder(
        private val binding: ItemCatalogPrimaryBigSubcategoryBinding,
        private val onSubCategoryClick: (CatalogSubCategoryModel) -> Unit = {}) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CatalogSubCategoryModel) {
            binding.productNameTextView.text = model.title
            binding.quantityTextView.text = "${model.products.size} услуг"

            GlideApp.with(binding.root.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(model.icon))
                .error(R.drawable.rectangle_filled_lime_radius_16dp)
                .transform(RoundedCorners(16f.dp(binding.root.context).toInt()))
                .into(binding.iconImageView)

            binding.root.setOnDebouncedClickListener {
                onSubCategoryClick(model)
            }
        }

    }

}
