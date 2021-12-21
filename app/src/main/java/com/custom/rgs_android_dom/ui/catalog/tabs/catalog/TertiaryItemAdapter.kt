package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemCatalogTertiaryCategoryItemBinding
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.mocks.TertiaryItemModel

class TertiaryItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = ArrayList<TertiaryItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TertiaryItemViewHolder(
            ItemCatalogTertiaryCategoryItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TertiaryItemViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<TertiaryItemModel>) {
        this.items.addAll(items)
    }

    inner class TertiaryItemViewHolder(val binding: ItemCatalogTertiaryCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: TertiaryItemModel) {
            binding.titleTextView.text = model.title
            binding.quantityTextView.text = model.quantity
        }
    }

}
