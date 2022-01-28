package com.custom.rgs_android_dom.ui.catalog.subcategories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemCatalogPrimaryBigProductBinding
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class GridVerticalPrimaryProductsAdapter(
    private val onProductClick: (ProductShortModel) -> Unit = {}
) : RecyclerView.Adapter<GridVerticalPrimaryProductsAdapter.CatalogPrimarySubcategoryViewHolder>() {

    private var products: List<ProductShortModel> = listOf()

    override fun onBindViewHolder(holder: CatalogPrimarySubcategoryViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogPrimarySubcategoryViewHolder {
        val binding = ItemCatalogPrimaryBigProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogPrimarySubcategoryViewHolder(binding, onProductClick)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setItems(products: List<ProductShortModel>){
        this.products = products
        notifyDataSetChanged()
    }

    inner class CatalogPrimarySubcategoryViewHolder(
        private val binding: ItemCatalogPrimaryBigProductBinding,
        private val onProductClick: (ProductShortModel) -> Unit = {}) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ProductShortModel) {
            binding.productNameTextView.text = model.name
            binding.priceTextView.text = "${model.price} ₽"

            GlideApp.with(binding.root.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(model.icon))
                .transform(RoundedCorners(16f.dp(binding.root.context).toInt()))
                .into(binding.iconImageView)

            binding.root.setOnDebouncedClickListener {
                onProductClick(model)
            }
        }

    }

}
