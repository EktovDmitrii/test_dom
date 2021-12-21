package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemCatalogTertiaryCategoryBinding
import com.custom.rgs_android_dom.databinding.ItemCatalogTertiaryCategoryItemBinding
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.mocks.TertiaryCategoryModel

class TertiaryCategoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<TertiaryCategoryModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TertiaryCategoryViewHolder(
            ItemCatalogTertiaryCategoryBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TertiaryCategoryViewHolder).bind(items[position])
    }

    fun setItems(items: List<TertiaryCategoryModel>) {
        this.items.addAll(items)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class TertiaryCategoryViewHolder(val binding: ItemCatalogTertiaryCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val adapter: TertiaryItemAdapter
            get() = binding.tertiaryRecyclerView.adapter as TertiaryItemAdapter

        fun bind(model: TertiaryCategoryModel) {
            binding.titleTextView.text = model.title
            binding.tertiaryRecyclerView.adapter = TertiaryItemAdapter()
            adapter.setItems(model.items)
        }

    }

}
