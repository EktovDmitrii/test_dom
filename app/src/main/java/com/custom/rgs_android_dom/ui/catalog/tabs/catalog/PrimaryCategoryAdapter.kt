package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemCatalogPrimaryCategoryBinding
import com.custom.rgs_android_dom.databinding.ItemCatalogPrimaryMoreProductsBinding
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.mocks.MoreProductsItemModel
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.mocks.ProductItemModel
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener


class PrimaryCategoryAdapter(
    private val onProductClick: () -> Unit,
    private val onMoreProductsClick: () -> Unit
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_PRODUCT_ITEM = 1
        private const val TYPE_MORE_PRODUCTS_ITEM = 2
    }

    private val categories: ArrayList<Any> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PRODUCT_ITEM -> {
                val binding = ItemCatalogPrimaryCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ProductItemViewHolder(binding, onProductClick)
            }
            TYPE_MORE_PRODUCTS_ITEM -> {
                val binding = ItemCatalogPrimaryMoreProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MoreProductsViewHolder(binding, onMoreProductsClick)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = categories[position]
        when (holder) {
            is PrimaryCategoryAdapter.ProductItemViewHolder -> holder.bind(model as ProductItemModel)
            is PrimaryCategoryAdapter.MoreProductsViewHolder -> holder.bind()
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (categories[position]) {
            is ProductItemModel -> TYPE_PRODUCT_ITEM
            is MoreProductsItemModel -> TYPE_MORE_PRODUCTS_ITEM
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    fun setItems(items: List<Any>) {
        categories.addAll(items)
        notifyDataSetChanged()
    }

    inner class ProductItemViewHolder(
        private val binding: ItemCatalogPrimaryCategoryBinding,
        private val onProductClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ProductItemModel) {
            binding.productNameTextView.text = model.title
            binding.quantityTextView.text = model.servicesQuantity
            binding.productLinearLayout.setOnDebouncedClickListener {
                onProductClick()
            }
        }

    }

    inner class MoreProductsViewHolder(
        private val binding: ItemCatalogPrimaryMoreProductsBinding,
        private val onMoreProductsClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.moreProductsFrameLayout.setOnDebouncedClickListener {
                onMoreProductsClick()
            }
        }
    }

}
