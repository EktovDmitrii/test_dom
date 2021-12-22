package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemCatalogSecondaryWithoutPictureBinding
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.mocks.SecondaryCategoryModelWithoutPicture

class SecondaryWithoutPictureAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: ArrayList<SecondaryCategoryModelWithoutPicture> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WithoutPictureViewHolder(
            ItemCatalogSecondaryWithoutPictureBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as WithoutPictureViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<SecondaryCategoryModelWithoutPicture>) {
        this.items.addAll(items)
    }

    inner class WithoutPictureViewHolder(
        private val binding: ItemCatalogSecondaryWithoutPictureBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: SecondaryCategoryModelWithoutPicture) {
            binding.titleTextView.text = model.title
            binding.quantityTextView.text = model.servicesQuantity
        }

    }

}
