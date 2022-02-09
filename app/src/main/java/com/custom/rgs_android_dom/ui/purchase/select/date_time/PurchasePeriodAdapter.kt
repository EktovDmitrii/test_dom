package com.custom.rgs_android_dom.ui.purchase.select.date_time

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.LayoutPurchasePeriodBinding
import com.custom.rgs_android_dom.domain.purchase.model.PurchaseDateTimeModel
import com.custom.rgs_android_dom.domain.purchase.model.PurchaseTimePeriodModel
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import java.lang.IllegalArgumentException

class PurchasePeriodAdapter(
    private val onPeriodClick: (PurchaseTimePeriodModel) -> Unit,
) : RecyclerView.Adapter<PurchasePeriodAdapter.PurchasePeriodViewHolder>() {

    private val periodList = listOf(
        PurchaseTimePeriodModel(0, timeOfDay = "Утро", timeFrom = "6:00", timeTo = "9:00"),
        PurchaseTimePeriodModel(1, timeOfDay = "До полудня", timeFrom = "9:00", timeTo = "12:00"),
        PurchaseTimePeriodModel(2, timeOfDay = "День", timeFrom = "12:00", timeTo = "15:00"),
        PurchaseTimePeriodModel(3, timeOfDay = "Вечер", timeFrom = "15:00", timeTo = "18:00")
    )

    override fun onBindViewHolder(holder: PurchasePeriodViewHolder, position: Int) {
        holder.bind(periodList[position])
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

    fun setSelected(dateTimeModel: PurchaseDateTimeModel) {
        val currDay = LocalDateTime.now().dayOfYear
        if (dateTimeModel.selectedDate.dayOfYear != currDay) {
            dateTimeModel.selectedDate = dateTimeModel.selectedDate.withTime(0,0,0,0)
        }
        periodList.forEach {
            it.isSelectable = when (it.timeFrom) {
                "6:00" -> LocalTime("9:00") >= dateTimeModel.selectedDate.toLocalTime()
                "9:00" -> LocalTime("12:00") >= dateTimeModel.selectedDate.toLocalTime()
                "12:00" -> LocalTime("15:00") >= dateTimeModel.selectedDate.toLocalTime()
                "15:00" -> LocalTime("18:00") >= dateTimeModel.selectedDate.toLocalTime()
                else -> throw IllegalArgumentException("Wrong argument: ${it.timeFrom}")
            }
            it.isSelected = dateTimeModel.selectedPeriodModel?.id == it.id
        }
        notifyDataSetChanged()
    }

    inner class PurchasePeriodViewHolder(
        private val binding: LayoutPurchasePeriodBinding,
        private val onPeriodClick: (PurchaseTimePeriodModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(period: PurchaseTimePeriodModel) {
            binding.timeOfDayTextView.text = period.timeOfDay
            binding.timeIntervalTextView.text = "${period.timeFrom} – ${period.timeTo}"

            binding.selectPeriodBtn.isChecked = period.isSelected

            binding.root.setOnDebouncedClickListener {
                if (period.isSelectable && !period.isSelected) {
                    onPeriodClick(period)
                }
            }

            if (period.isSelectable) {
                binding.timeOfDayTextView.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.secondary800)
                )
                binding.timeIntervalTextView.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.secondary900)
                )
            } else {
                binding.timeOfDayTextView.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.secondary300)
                )
                binding.timeIntervalTextView.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.secondary250)
                )
            }

        }

    }
}
