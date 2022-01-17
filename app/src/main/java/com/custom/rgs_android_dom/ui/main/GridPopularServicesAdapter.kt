package com.custom.rgs_android_dom.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemCatalogGridlayoutSubcategoryBinding
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.ui.base.BaseViewHolder
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class GridPopularServicesAdapter(
    private val onServiceClick: (ProductShortModel) -> Unit = {}
) : RecyclerView.Adapter<GridPopularServicesAdapter.PopularServiceItemViewHolder>() {

    private var services: List<ProductShortModel> = listOf()

    override fun onBindViewHolder(holder: PopularServiceItemViewHolder, position: Int) {
        holder.bind(services[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularServiceItemViewHolder {
        val binding = ItemCatalogGridlayoutSubcategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularServiceItemViewHolder(binding, onServiceClick)
    }

    override fun getItemCount(): Int {
        return services.size
    }

    fun setItems(services: List<ProductShortModel>){
        this.services = services
        notifyDataSetChanged()
    }

    inner class PopularServiceItemViewHolder(
        private val binding: ItemCatalogGridlayoutSubcategoryBinding,
        private val onServiceClick: (ProductShortModel) -> Unit
    ) : BaseViewHolder<ProductShortModel>(binding.root) {

        override fun bind(model: ProductShortModel) {
            binding.titleTextView.text = model.title

            GlideApp.with(binding.root.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(model.icon))
                .transform(RoundedCorners(16.dp(binding.root.context)))
                .into(binding.logoImageView)

            binding.root.setOnDebouncedClickListener {
                onServiceClick(model)
            }
        }

    }
}