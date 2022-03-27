package com.custom.rgs_android_dom.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemMainPopularProductBinding
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.formatPrice
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class PopularProductsAdapter(private val onProductClick: (product: ProductShortModel) -> Unit = {}) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var products = mutableListOf<ProductShortModel>()

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

    fun setItems(products: List<ProductShortModel>) {
        this.products.addAll(products)
        notifyDataSetChanged()
    }

    inner class PopularProductsViewHolder(
        private val binding: ItemMainPopularProductBinding,
        private val onProductClick: (product: ProductShortModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ProductShortModel) {
            binding.root.setOnDebouncedClickListener { onProductClick(model) }
            binding.labelTextView.text = model.name
            binding.priceTextView.text = model.price.formatPrice()

            model.logoLarge.let {
                GlideApp.with(binding.root.context)
                    .load(GlideUrlProvider.makeHeadersGlideUrl(it))
                    .transform(RoundedCorners(20.dp(binding.root.context)))
                    .into(binding.largeLogoImageView)
            }

            model.icon.let {
                GlideApp.with(binding.root.context)
                    .load(GlideUrlProvider.makeHeadersGlideUrl(it))
                    .transform(RoundedCorners(6.dp(binding.root.context)))
                    .into(binding.smallLogoImageView)
            }

        }

    }

}
