package com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_date_time

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ItemDateTimeBinding
import com.custom.rgs_android_dom.domain.purchase_service.model.DateForCalendarModel
import com.custom.rgs_android_dom.utils.dpToPx
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import java.util.*

class PurchaseDatesAdapter(
    private val onDateClick: (DateForCalendarModel) -> Unit,
) : RecyclerView.Adapter<PurchaseDatesAdapter.PurchaseDateTimeViewHolder>() {

    private var dateList = mutableListOf<DateForCalendarModel>()

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

    fun setItems(dateForCalendarList: List<DateForCalendarModel>) {
        dateList.clear()
        dateList.addAll(dateForCalendarList)
        notifyDataSetChanged()
    }

    inner class PurchaseDateTimeViewHolder(
        private val binding: ItemDateTimeBinding,
        private val onDateClick: (DateForCalendarModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dateForCalendar: DateForCalendarModel) {
            binding.dayNumberTextView.text = dateForCalendar.dateNumber

            binding.dayOfWeekTextView.text = dateForCalendar.dayInWeek.capitalize(Locale.getDefault())
            if (dateForCalendar.isSelected) {
                binding.dayNumberTextView.setBackgroundResource(R.drawable.rectangle_filled_secondary_100_radius_12dp)
            } else binding.dayNumberTextView.setBackgroundColor(0)
            if (dateForCalendar.isEnable) {
                binding.root.setOnDebouncedClickListener {
                    onDateClick(dateForCalendar)
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
