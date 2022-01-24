package com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_date_time

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.LayoutPurchasePeriodBinding
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchasePeriodModel

class PurchasePeriodAdapter(
    private val onPeriodClick: (PurchasePeriodModel) -> Unit,
) : RecyclerView.Adapter<PurchasePeriodAdapter.PurchasePeriodViewHolder>() {

    private var periodList = mutableListOf<PurchasePeriodModel>()

    override fun onBindViewHolder(holder: PurchasePeriodViewHolder, position: Int) {
        (holder).bind(periodList[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PurchasePeriodViewHolder {
        val binding = LayoutPurchasePeriodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PurchasePeriodViewHolder(binding, onPeriodClick)
    }

    override fun getItemCount(): Int {
        return periodList.size
    }

    fun setItems(purchasePeriodList: List<PurchasePeriodModel>) {
        periodList = purchasePeriodList.toMutableList()
        notifyDataSetChanged()
    }

    inner class PurchasePeriodViewHolder(
        private val binding: LayoutPurchasePeriodBinding,
        private val onPeriodClick: (PurchasePeriodModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(period: PurchasePeriodModel) {
            binding.timesOfDayTextView.text = period.timesOfDay
            binding.timeIntervalTextView.text = period.timeInterval
            binding.selectPeriodBtn.isChecked = period.isSelected

            if (period.isClickable) {
                binding.timesOfDayTextView.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.secondary800
                    )
                )
                binding.timeIntervalTextView.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.secondary900
                    )
                )
                onPeriodClick(period)
            } else {
                binding.timesOfDayTextView.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.secondary300
                    )
                )
                binding.timeIntervalTextView.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.secondary250
                    )
                )
                binding.selectPeriodBtn.isEnabled = false
            }
        }

    }
}
