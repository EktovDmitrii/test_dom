package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemCatalogSecondaryCategoryBinding
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.mocks.SecondaryCategoryModel
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.recycler_view.VerticalItemDecoration

class SecondaryCategoryAdapter(private val onServiceClick: () -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<SecondaryCategoryModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SecondaryCategoryItemViewHolder(
            ItemCatalogSecondaryCategoryBinding.inflate(
                LayoutInflater.from(parent.context)
            ),
            onServiceClick
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SecondaryCategoryItemViewHolder).bind(items[position])
    }

    fun setItems(items: List<SecondaryCategoryModel>) {
        this.items.addAll(items)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class SecondaryCategoryItemViewHolder(
        private val binding: ItemCatalogSecondaryCategoryBinding,
        private val onServiceClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        val adapter: SecondaryWithoutPictureAdapter
            get() = binding.withoutPictureRecyclerView.adapter as SecondaryWithoutPictureAdapter

        fun bind(model: SecondaryCategoryModel) {
            binding.serviceTitleTextView.text = model.title
            binding.leftTitleTextView.text = model.modelsWithPicture[0].title
            binding.centerTitleTextView.text = model.modelsWithPicture[1].title
            binding.rightTitleTextView.text = model.modelsWithPicture[2].title
            binding.withoutPictureRecyclerView.adapter = SecondaryWithoutPictureAdapter()
            binding.withoutPictureRecyclerView.addItemDecoration(VerticalItemDecoration(gap = 8.dp(binding.root.context)))
            adapter.setItems(model.modelsWithoutPicture)
        }

    }

}
