package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemCatalogSubcategoryWithBigImageBinding
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp

class CatalogSubcategoriesWithBigImageAdapter(
    private val onSubCategoryClick: (CatalogSubCategoryModel) -> Unit = {}
) : RecyclerView.Adapter<CatalogSubcategoriesWithBigImageAdapter.CatalogSubCategoryWithBigImageViewHolder>() {

    private var subCategories: List<CatalogSubCategoryModel> = listOf()

    override fun onBindViewHolder(holder: CatalogSubCategoryWithBigImageViewHolder, position: Int) {
        holder.bind(subCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogSubCategoryWithBigImageViewHolder {
        val binding = ItemCatalogSubcategoryWithBigImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogSubCategoryWithBigImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return subCategories.size
    }

    fun setItems(subCategories: List<CatalogSubCategoryModel>){
        this.subCategories = subCategories
        notifyDataSetChanged()
    }

    inner class CatalogSubCategoryWithBigImageViewHolder(
        private val binding: ItemCatalogSubcategoryWithBigImageBinding,
        private val onSubCategoryClick: (CatalogSubCategoryModel) -> Unit = {}) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CatalogSubCategoryModel) {
            binding.titleTextView.text = model.title

            GlideApp.with(binding.root.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(model.icon))
                .transform(RoundedCorners(16.dp(binding.root.context)))
                .error(R.drawable.ic_call_consultant)
                .into(binding.subcategoryImageView)
        }

    }
}