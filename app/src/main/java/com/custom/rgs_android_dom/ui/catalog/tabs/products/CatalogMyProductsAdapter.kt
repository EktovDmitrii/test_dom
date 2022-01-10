package com.custom.rgs_android_dom.ui.catalog.tabs.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ItemCatalogMyProductBinding
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class CatalogMyProductsAdapter(
    private val onProductClick: (ProductShortModel) -> Unit = {}
) : RecyclerView.Adapter<CatalogMyProductsAdapter.CatalogMyProductViewHolder>() {

    private var myProducts: List<ProductShortModel> = listOf()

    override fun onBindViewHolder(holder: CatalogMyProductViewHolder, position: Int) {
        holder.bind(myProducts[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogMyProductViewHolder {
        val binding = ItemCatalogMyProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogMyProductViewHolder(binding, onProductClick)
    }

    override fun getItemCount(): Int {
        return myProducts.size
    }

    fun setItems(products: List<ProductShortModel>){
        this.myProducts = products
        notifyDataSetChanged()
    }

    inner class CatalogMyProductViewHolder(
        private val binding: ItemCatalogMyProductBinding,
        private val onProductClick: (ProductShortModel) -> Unit = {}) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ProductShortModel) {
            binding.productNameTextView.text = model.title

            GlideApp.with(binding.root.context)
                .load(R.drawable.rectangle_filled_lime_radius_16dp)
                .transform(RoundedCorners(16f.dp(binding.root.context).toInt()))
                .into(binding.iconImageView)

            binding.root.setOnDebouncedClickListener {
                onProductClick(model)
            }
        }

    }
}