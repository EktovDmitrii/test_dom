package com.custom.rgs_android_dom.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemPopularCategoryBinding
import com.custom.rgs_android_dom.domain.main.PopularCategoryModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class PopularCategoriesAdapter(private val onCategoryClick: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var categories: List<PopularCategoryModel> = emptyList()

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): RecyclerView.ViewHolder {
        val binding =
            ItemPopularCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularCategoriesViewHolder(binding, onCategoryClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PopularCategoriesViewHolder).bind(categories[position])
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun setItems(categories: List<PopularCategoryModel>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    inner class PopularCategoriesViewHolder(
        private val binding: ItemPopularCategoryBinding,
        private val onCategoryClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PopularCategoryModel) {

            binding.titleTextView.text = model.title
            binding.quantityTextView.text = model.quantity

             GlideApp.with(binding.root.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(model.icon))
                .into(binding.iconImageView)

            binding.root.setOnDebouncedClickListener {
                onCategoryClick()
            }
        }

    }

}