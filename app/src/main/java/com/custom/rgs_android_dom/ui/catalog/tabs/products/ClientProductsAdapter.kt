package com.custom.rgs_android_dom.ui.catalog.tabs.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ItemCatalogMyProductBinding
import com.custom.rgs_android_dom.domain.catalog.models.ClientProductModel
import com.custom.rgs_android_dom.utils.*

class ClientProductsAdapter(
    private val onProductClick: (ClientProductModel) -> Unit = {}
) : RecyclerView.Adapter<ClientProductsAdapter.CatalogMyProductViewHolder>() {

    private var products: List<ClientProductModel> = listOf()

    override fun onBindViewHolder(holder: CatalogMyProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogMyProductViewHolder {
        val binding = ItemCatalogMyProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogMyProductViewHolder(binding, onProductClick)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setItems(products: List<ClientProductModel>){
        this.products = products
        notifyDataSetChanged()
    }

    inner class CatalogMyProductViewHolder(
        private val binding: ItemCatalogMyProductBinding,
        private val onProductClick: (ClientProductModel) -> Unit = {}) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ClientProductModel) {
            binding.productNameTextView.text = model.productName

            binding.validityTextView.goneIf(model.validityFrom == null && model.validityTo == null)

            if (model.validityFrom != null && model.validityFrom.isAfterNow){
                binding.validityTextView.text = "Действует с ${model.validityFrom.formatTo(DATE_PATTERN_DATE_ONLY)}"
            } else if (model.validityTo != null && model.validityTo.isAfterNow){
                binding.validityTextView.text = "Действует до ${model.validityTo.formatTo(DATE_PATTERN_DATE_ONLY)}"
            }

            GlideApp.with(binding.root.context)
                .load(model.productIcon)
                .transform(RoundedCorners(6.dp(binding.root.context)))
                .error(R.drawable.rectangle_filled_secondary_100_radius_16dp)
                .into(binding.logoSmallImageView)

            GlideApp.with(binding.root.context)
                .load(model.logoLarge)
                .transform(RoundedCorners(16.dp(binding.root.context)))
                .error(R.drawable.rectangle_filled_secondary_100_radius_16dp)
                .into(binding.logoBigImageView)

            binding.root.setOnDebouncedClickListener {
                onProductClick(model)
            }
        }

    }
}