package com.custom.rgs_android_dom.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemPopularCategoryBinding
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.formatServiceQuantity
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class PopularCategoriesAdapter(private val onCategoryClick: (CatalogCategoryModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        private const val ROUNDING_RADIUS = 16
    }

    private var categories: List<CatalogCategoryModel> = emptyList()

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): RecyclerView.ViewHolder {
        val binding =
            ItemPopularCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularCategoriesViewHolder(binding) { onCategoryClick(it) }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PopularCategoriesViewHolder).bind(categories[position])
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun setItems(categories: List<CatalogCategoryModel>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    inner class PopularCategoriesViewHolder(
        private val binding: ItemPopularCategoryBinding,
        private val onCategoryClick: (CatalogCategoryModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CatalogCategoryModel) {

            binding.titleTextView.text = model.name
            binding.quantityTextView.text = model.subCategories.size.formatServiceQuantity()

            val requestOptions = RequestOptions().transform( CenterCrop(), RoundedCorners(ROUNDING_RADIUS.dp(binding.root.context)))

             GlideApp.with(binding.root.context)
                 .load(GlideUrlProvider.makeHeadersGlideUrl(model.logoSmall))
                 .apply(requestOptions)
                 .into(binding.iconImageView)

            binding.root.setOnDebouncedClickListener {
                onCategoryClick(model)
            }
        }

    }

}