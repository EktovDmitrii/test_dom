package com.custom.rgs_android_dom.ui.catalog.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemGridCatalogInfoBinding
import com.custom.rgs_android_dom.domain.catalog.models.ServiceShortModel
import com.custom.rgs_android_dom.ui.base.BaseViewHolder
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class ProductInclusionAdapter(
    private val onServiceClick: (ServiceShortModel) -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var inclusions: List<ServiceShortModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemGridCatalogInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductFeatureViewHolder(binding, onServiceClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProductFeatureViewHolder).bind(inclusions[position])
    }

    override fun getItemCount(): Int = inclusions.size

    fun setItems(inclusions: List<ServiceShortModel>){
        this.inclusions = inclusions
        notifyDataSetChanged()
    }

    inner class ProductFeatureViewHolder(
        private val binding: ItemGridCatalogInfoBinding,
        private val onServiceClick: (ServiceShortModel) -> Unit = {}
    ) : BaseViewHolder<ServiceShortModel>(binding.root) {

        override fun bind(item: ServiceShortModel) {
            binding.titleTextView.text = item.serviceName
            binding.numberTextView.text = "${item.quantity}"

            binding.layout.setOnDebouncedClickListener {
                onServiceClick(item)
            }
        }
    }
}