package com.custom.rgs_android_dom.ui.catalog.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ItemGridCatalogInfoBinding
import com.custom.rgs_android_dom.domain.catalog.models.ServiceShortModel
import com.custom.rgs_android_dom.ui.base.BaseViewHolder
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visibleIf

class ProductInclusionAdapter(
    private val onServiceClick: (ServiceShortModel) -> Unit = {},
    private val onOrderClick: (ServiceShortModel) -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        private const val INFINITY = 999999
    }

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

    fun setItems(inclusions: List<ServiceShortModel>) {
        this.inclusions = inclusions
        notifyDataSetChanged()
    }

    inner class ProductFeatureViewHolder(
        private val binding: ItemGridCatalogInfoBinding,
        private val onServiceClick: (ServiceShortModel) -> Unit = {},
        private val onOrderClick: (ServiceShortModel) -> Unit = {}
    ) : BaseViewHolder<ServiceShortModel>(binding.root) {

        override fun bind(item: ServiceShortModel) {
            binding.titleTextView.text = item.serviceName
            if (item.quantity?.toInt() == INFINITY) {
                binding.numberTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_infinity, 0, 0, 0
                )
            } else {
                binding.numberTextView.text = "${item.quantity}"
            }
            binding.orderTextView.visibleIf(item.isPurchased)
            binding.orderTextView.setOnDebouncedClickListener {
                onOrderClick(item)
            }

            binding.layout.setOnDebouncedClickListener {
                onServiceClick(item)
            }
        }
    }
}