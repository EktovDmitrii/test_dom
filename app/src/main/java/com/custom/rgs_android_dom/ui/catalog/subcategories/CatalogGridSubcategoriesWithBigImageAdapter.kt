package com.custom.rgs_android_dom.ui.catalog.subcategories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemCatalogGridlayoutSubcategoryBinding
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class CatalogGridSubcategoriesWithBigImageAdapter(
    private val onSubCategoryClick: (CatalogSubCategoryModel) -> Unit = {}
) : RecyclerView.Adapter<CatalogGridSubcategoriesWithBigImageAdapter.CatalogSubCategoryWithBigImageViewHolder>() {

    private var subCategories: List<CatalogSubCategoryModel> = listOf()

    override fun onBindViewHolder(holder: CatalogSubCategoryWithBigImageViewHolder, position: Int) {
        holder.bind(subCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogSubCategoryWithBigImageViewHolder {
        val binding = ItemCatalogGridlayoutSubcategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogSubCategoryWithBigImageViewHolder(binding, onSubCategoryClick)
    }

    override fun getItemCount(): Int {
        return subCategories.size
    }

    fun setItems(subCategories: List<CatalogSubCategoryModel>){
        this.subCategories = subCategories
        notifyDataSetChanged()
    }

    inner class CatalogSubCategoryWithBigImageViewHolder(
        private val binding: ItemCatalogGridlayoutSubcategoryBinding,
        private val onSubCategoryClick: (CatalogSubCategoryModel) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CatalogSubCategoryModel) {
            binding.titleTextView.text = model.title

            GlideApp.with(binding.root.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(model.icon))
                .transform(RoundedCorners(16.dp(binding.root.context)))
                .into(binding.logoImageView)

            binding.root.setOnDebouncedClickListener {
                onSubCategoryClick(model)
            }
        }

    }
}