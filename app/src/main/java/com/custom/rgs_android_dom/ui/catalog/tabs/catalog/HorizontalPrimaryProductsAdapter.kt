package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemCatalogPrimaryMoreProductsBinding
import com.custom.rgs_android_dom.databinding.ItemCatalogPrimarySmallProductBinding
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.ui.base.BaseViewHolder
import com.custom.rgs_android_dom.utils.DigitsFormatter
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class HorizontalPrimaryProductsAdapter(
    private val onProductClick: (ProductShortModel) -> Unit = {},
    private val onMoreClick: () -> Unit
) : RecyclerView.Adapter<BaseViewHolder<ProductShortModel>>() {

    companion object {
        private const val TYPE_PRODUCT_ITEM = 0
        private const val TYPE_MORE_ITEM = 1
    }

    private var products: List<ProductShortModel> = listOf()

    override fun onBindViewHolder(holder: BaseViewHolder<ProductShortModel>, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (products.size - 1 == position) {
            TYPE_MORE_ITEM
        } else {
            TYPE_PRODUCT_ITEM
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ProductShortModel> {
        return if (viewType == TYPE_PRODUCT_ITEM) {
            val binding = ItemCatalogPrimarySmallProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            CatalogPrimaryProductViewHolder(binding, onProductClick)
        } else {
            val binding = ItemCatalogPrimaryMoreProductsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            CatalogMoreProductsViewHolder(binding, onMoreClick)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setItems(products: List<ProductShortModel>) {
        this.products = products
        notifyDataSetChanged()
    }

    inner class CatalogPrimaryProductViewHolder(
        private val binding: ItemCatalogPrimarySmallProductBinding,
        private val onProductClick: (ProductShortModel) -> Unit
    ) : BaseViewHolder<ProductShortModel>(binding.root) {

        override fun bind(model: ProductShortModel) {
            binding.productNameTextView.text = model.title
            binding.priceTextView.text = DigitsFormatter.priceFormat(model.price)

            GlideApp.with(binding.root.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(model.icon))
                .transform(RoundedCorners(16f.dp(binding.root.context).toInt()))
                .into(binding.iconImageView)

            binding.root.setOnDebouncedClickListener {
                onProductClick(model)
            }
        }
    }

    inner class CatalogMoreProductsViewHolder(
        private val binding: ItemCatalogPrimaryMoreProductsBinding,
        private val onMoreClick: () -> Unit
    ) : BaseViewHolder<ProductShortModel>(binding.root) {

        override fun bind(model: ProductShortModel) {
            binding.moreProductsFrameLayout.setOnDebouncedClickListener {
                onMoreClick()
            }
        }
    }
}