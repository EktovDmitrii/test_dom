package com.custom.rgs_android_dom.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemMainPopularProductBinding
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.formatPrice
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class PopularProductsAdapter(private val onProductClick: (productId: String) -> Unit = {}) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var products = mutableListOf<ProductModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding = ItemMainPopularProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PopularProductsViewHolder(binding, onProductClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PopularProductsViewHolder).bind(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setItems(products: List<ProductModel>) {
        this.products.addAll(products)
        notifyDataSetChanged()
    }

    inner class PopularProductsViewHolder(
        private val binding: ItemMainPopularProductBinding,
        private val onProductClick: (productId: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(productModel: ProductModel) {
            binding.root.setOnDebouncedClickListener { onProductClick(productModel.id) }
            binding.labelTextView.text = productModel.title
            binding.priceTextView.text = productModel.price?.amount?.formatPrice()

            GlideApp.with(binding.root.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(productModel.iconLink))
                .into(binding.smallLogoImageView)

            GlideApp.with(binding.root.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(productModel.iconLink))
                .into(binding.largeLogoImageView)
        }

    }

}
