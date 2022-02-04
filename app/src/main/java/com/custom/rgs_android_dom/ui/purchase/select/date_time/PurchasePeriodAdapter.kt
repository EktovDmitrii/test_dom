package com.custom.rgs_android_dom.ui.purchase.select.date_time

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.LayoutPurchasePeriodBinding
import com.custom.rgs_android_dom.domain.purchase.model.PurchaseTimePeriodModel
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class PurchasePeriodAdapter(
    private val onPeriodClick: (PurchaseTimePeriodModel) -> Unit,
) : RecyclerView.Adapter<PurchasePeriodAdapter.PurchasePeriodViewHolder>() {

    private var periodList = mutableListOf<PurchaseTimePeriodModel>()

    override fun onBindViewHolder(holder: PurchasePeriodViewHolder, position: Int) {
        (holder).bind(periodList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchasePeriodViewHolder {
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

    fun setItems(purchasePeriodList: List<PurchaseTimePeriodModel>) {
        periodList.clear()
        periodList.addAll(purchasePeriodList)
        notifyDataSetChanged()
    }

    inner class PurchasePeriodViewHolder(
        private val binding: LayoutPurchasePeriodBinding,
        private val onPeriodClick: (PurchaseTimePeriodModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(period: PurchaseTimePeriodModel) {
            binding.timeOfDayTextView.text = period.timeOfDay
            binding.timeIntervalTextView.text = period.displayTime

            binding.selectPeriodBtn.isChecked = period.isSelected

            Log.d("MyLog", "PERIOD " + period.displayTime + " IS SELECTAVBLER " + period.isSelectable)

            if (period.isSelectable) {
                binding.timeOfDayTextView.setTextColor(
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
            } else {
                binding.timeOfDayTextView.setTextColor(
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
            }

            binding.root.setOnDebouncedClickListener {
                if (period.isSelectable){
                    onPeriodClick(period)
                }
            }

        }

    }
}
