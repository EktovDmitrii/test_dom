package com.custom.rgs_android_dom.ui.policies.policy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ItemGridPolicyInfoBinding
import com.custom.rgs_android_dom.domain.catalog.models.ServiceShortModel
import com.custom.rgs_android_dom.ui.base.BaseViewHolder
import com.custom.rgs_android_dom.utils.gone
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visible
import com.custom.rgs_android_dom.utils.visibleIf

class PolicyProductInclusionAdapter (
    private val onServiceClick: (ServiceShortModel) -> Unit = {},
    private val onOrderClick: (ServiceShortModel) -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        private const val INFINITY = 999999
    }

    private var inclusions: List<ServiceShortModel> = emptyList()
    private var status: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemGridPolicyInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductFeatureViewHolder(binding, onServiceClick, onOrderClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProductFeatureViewHolder).bind(inclusions[position])
    }

    override fun getItemCount(): Int = inclusions.size

    fun setItems(inclusions: List<ServiceShortModel>) {
        this.inclusions = inclusions
        notifyDataSetChanged()
    }

    fun setStatus(status: Boolean) {
        this.status = status
    }

    inner class ProductFeatureViewHolder(
        private val binding: ItemGridPolicyInfoBinding,
        private val onServiceClick: (ServiceShortModel) -> Unit = {},
        private val onOrderClick: (ServiceShortModel) -> Unit = {}
    ) : BaseViewHolder<ServiceShortModel>(binding.root) {

        override fun bind(item: ServiceShortModel) {
            binding.titleTextView.text = item.serviceName

            binding.numberTextView.visibleIf(item.quantity > 0)

            if (status) {
                if (item.quantity.toInt() == INFINITY) {
                    binding.numberTextView.text = ""
                    binding.numberTextView.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_infinity, 0, 0, 0
                    )
                } else if (item.quantity.toInt() > 0) {
                    binding.numberTextView.text = "${item.quantity}"
                }
            } else {
                binding.numberTextView.text = "0"
            }

            if (item.isPurchased){
                binding.orderTextView.visible()
                binding.orderTextView.isEnabled = item.canBeOrdered && status
            } else {
                binding.orderTextView.gone()
            }

            if (item.quantity <= 0){
                binding.orderTextView.isEnabled = false
            } else if (item.isPurchased){
                binding.orderTextView.isEnabled = item.canBeOrdered && status
            }


            binding.orderTextView.setOnDebouncedClickListener {
                if (status) onOrderClick(item)
            }

            binding.layout.setOnDebouncedClickListener {
                onServiceClick(item)
            }
        }
    }
}