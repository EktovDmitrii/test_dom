package com.custom.rgs_android_dom.ui.catalog.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemCatalogSearchResultItemBinding
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class CatalogSearchResultsAdapter(
    private val onProductClick: (ProductShortModel) -> Unit = {}
) : RecyclerView.Adapter<CatalogSearchResultsAdapter.CatalogSearchResultViewHolder>() {

    private var products: List<ProductShortModel> = listOf()

    override fun onBindViewHolder(holder: CatalogSearchResultViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogSearchResultViewHolder {
        val binding = ItemCatalogSearchResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogSearchResultViewHolder(binding, onProductClick)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setItems(products: List<ProductShortModel>){
        this.products = products
        notifyDataSetChanged()
    }

    inner class CatalogSearchResultViewHolder(
        private val binding: ItemCatalogSearchResultItemBinding,
        private val onProductClick: (ProductShortModel) -> Unit = {}) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ProductShortModel) {
            binding.titleTextView.text = model.name

            GlideApp.with(binding.root.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(model.icon))
                .transform(RoundedCorners(12.dp(binding.root.context)))
                .error(R.drawable.rectangle_filled_secondary_100_radius_12dp)
                .into(binding.logoImageView)

            binding.root.setOnDebouncedClickListener {
                onProductClick(model)
            }
        }
    }
}