package com.custom.rgs_android_dom.ui.catalog.subcategories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemCatalogSubcategoryDetailsProductBinding
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel

class CatalogSubcategoryProductsAdapter(
    private val productClick: (ProductShortModel) -> Unit = {}
) : RecyclerView.Adapter<CatalogSubcategoryProductsAdapter.CatalogSubcategoryProductsViewHolder>() {

    private var products: List<ProductShortModel> = listOf()

    override fun onBindViewHolder(holder: CatalogSubcategoryProductsViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogSubcategoryProductsViewHolder {
        val binding = ItemCatalogSubcategoryDetailsProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogSubcategoryProductsViewHolder(binding)
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
            binding.titleTextView.text = model.title
            binding.priceTextView.text = "от ${model.price} ₽/шт"
        }

    }
}