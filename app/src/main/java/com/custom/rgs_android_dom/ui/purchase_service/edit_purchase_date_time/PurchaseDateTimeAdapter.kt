package com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_date_time

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ItemDateTimeBinding
import com.custom.rgs_android_dom.domain.purchase_service.PurchaseDateContent
import com.custom.rgs_android_dom.utils.dpToPx
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class PurchaseDateTimeAdapter(
    private val onDateClick: (PurchaseDateContent) -> Unit,
) : RecyclerView.Adapter<PurchaseDateTimeAdapter.PurchaseDateTimeViewHolder>() {

    private var dateList = mutableListOf<PurchaseDateContent>()

    override fun onBindViewHolder(holder: PurchaseDateTimeViewHolder, position: Int) {
        (holder).bind(dateList[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PurchaseDateTimeViewHolder {
        val binding = ItemDateTimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val metrics: DisplayMetrics = parent.resources.displayMetrics
        val width = metrics.widthPixels - 8.dpToPx(binding.root.context)

        binding.root.updateLayoutParams {
            this.width = width / 7
        }
        return PurchaseDateTimeViewHolder(binding, onDateClick)
    }

    override fun getItemCount(): Int {
        return dateList.size
    }

    fun setItems(purchaseDateList: List<PurchaseDateContent>) {
        dateList.clear()
        dateList.addAll(purchaseDateList)
        notifyDataSetChanged()
    }

    inner class PurchaseDateTimeViewHolder(
        private val binding: ItemDateTimeBinding,
        private val onDateClick: (PurchaseDateContent) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(date: PurchaseDateContent) {
            binding.dayNumberTextView.text = date.dateNumber
            binding.dayOfWeekTextView.text = date.dayInWeek
            if (date.isSelected) {
                binding.dayNumberTextView.setBackgroundResource(R.drawable.rectangle_filled_secondary_100_radius_12dp)
            } else binding.dayNumberTextView.setBackgroundColor(0)
            if (date.isEnable) {
                binding.root.setOnDebouncedClickListener {
                    onDateClick(date)
                }
                binding.dayOfWeekTextView.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.secondary600
                    )
                )
                binding.dayNumberTextView.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.secondary800
                    )
                )
            } else {
                binding.dayOfWeekTextView.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.secondary300
                    )
                )
                binding.dayNumberTextView.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.secondary400
                    )
                )
            }
        }

    }
}
