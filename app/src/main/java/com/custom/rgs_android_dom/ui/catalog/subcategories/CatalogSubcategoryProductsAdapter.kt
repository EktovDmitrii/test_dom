package com.custom.rgs_android_dom.ui.catalog.subcategories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemCatalogSubcategoryDetailsProductBinding
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.utils.formatPrice
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class CatalogSubcategoryProductsAdapter(
    private val onProductClick: (ProductShortModel) -> Unit = {}
) : RecyclerView.Adapter<CatalogSubcategoryProductsAdapter.CatalogSubcategoryProductsViewHolder>() {

    private var products: List<ProductShortModel> = listOf()

    override fun onBindViewHolder(holder: CatalogSubcategoryProductsViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogSubcategoryProductsViewHolder {
        val binding = ItemCatalogSubcategoryDetailsProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogSubcategoryProductsViewHolder(binding, onProductClick)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setItems(products: List<ProductShortModel>){
        this.products = products
        notifyDataSetChanged()
    }

    inner class CatalogSubcategoryProductsViewHolder(
        private val binding: ItemCatalogSubcategoryDetailsProductBinding,
        private val onProductClick: (ProductShortModel) -> Unit = {}) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ProductShortModel) {
            binding.titleTextView.text = model.name
            binding.priceTextView.text = model.price.formatPrice()

            binding.root.setOnDebouncedClickListener {
                onProductClick(model)
            }
        }

    }
}