package com.custom.rgs_android_dom.ui.catalog.subcategories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemCatalogSubcategoryDetailsBinding
import com.custom.rgs_android_dom.databinding.ItemCatalogSubcategoryWithSmallImageBinding
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.gone
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class CatalogSubcategoriesDetailsAdapter(
    private val onProductClick: (ProductShortModel) -> Unit = {}
) : RecyclerView.Adapter<CatalogSubcategoriesDetailsAdapter.CatalogSubcategoriesDetailsViewHolder>() {

    private var subCategories: List<CatalogSubCategoryModel> = listOf()

    override fun onBindViewHolder(holder: CatalogSubcategoriesDetailsViewHolder, position: Int) {
        holder.bind(subCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogSubcategoriesDetailsViewHolder {
        val binding = ItemCatalogSubcategoryDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogSubcategoriesDetailsViewHolder(binding, onProductClick)
    }

    override fun getItemCount(): Int {
        return subCategories.size
    }

    fun setItems(subCategories: List<CatalogSubCategoryModel>){
        this.subCategories = subCategories
        notifyDataSetChanged()
    }

    inner class CatalogSubcategoriesDetailsViewHolder(
        private val binding: ItemCatalogSubcategoryDetailsBinding,
        private val onProductClick: (ProductShortModel) -> Unit = {}) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CatalogSubCategoryModel) {
            binding.titleTextView.text = model.title
            binding.subtitleTextView.text = "${model.products.size} видов услуг"

            GlideApp.with(binding.root.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(model.icon))
                .transform(RoundedCorners(12.dp(binding.root.context)))
                .error(R.drawable.rectangle_filled_secondary_100_radius_12dp)
                .into(binding.logoImageView)

            if (model.products.isNotEmpty()){
                val adapter = CatalogSubcategoryProductsAdapter(){
                    onProductClick(it)
                }

                binding.productsRecyclerView.adapter = adapter
                adapter.setItems(model.products)

                binding.root.setOnDebouncedClickListener {
                    var recyclerVisible = (binding.productsRecyclerView.tag ?: false) as Boolean
                    recyclerVisible = !recyclerVisible

                    binding.productsRecyclerView.isVisible = recyclerVisible

                    binding.productsRecyclerView.tag = recyclerVisible
                }

            } else {
                binding.productsRecyclerView.gone()
            }
        }
    }
}