package com.custom.rgs_android_dom.ui.purchase.select.date_time

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPurchaseDateTimeBinding
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseDateTimeModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.DATE_PATTERN_YEAR_MONTH
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import org.joda.time.LocalDateTime
import java.io.Serializable
import java.util.*

class PurchaseDateTimeFragment :
    BaseBottomSheetModalFragment<PurchaseDateTimeViewModel, FragmentPurchaseDateTimeBinding>() {

    companion object {
        fun newInstance(): PurchaseDateTimeFragment = PurchaseDateTimeFragment()
    }

    private var purchaseDateTimeListener: PurchaseDateTimeListener? = null

    private val dateListAdapter: PurchaseDatesAdapter
        get() = binding.datesRecyclerView.adapter as PurchaseDatesAdapter

    private val periodListAdapter: PurchasePeriodAdapter
        get() = binding.periodRecyclerView.adapter as PurchasePeriodAdapter

    override val TAG: String = "PURCHASE_DATE_TIME_FRAGMENT"

    private var visibleMonth: Int? = 0
    private var currDate: LocalDateTime = LocalDateTime.now()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is PurchaseDateTimeListener) {
            purchaseDateTimeListener = parentFragment as PurchaseDateTimeListener
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.datesRecyclerView.adapter = PurchaseDatesAdapter {
            viewModel.selectDay(it.date)
        }

        binding.datesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var loading = true
            var previousTotal = 0
            var visibleThreshold = 31
            var firstVisibleItem = 0
            var visibleItemCount = 0
            var totalItemCount = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = (binding.datesRecyclerView.layoutManager as LinearLayoutManager)

                val lastVisibleItemPos = layoutManager.findLastVisibleItemPosition()
                val lastItemDate = dateListAdapter.getItem(lastVisibleItemPos).date
                if (visibleMonth != lastItemDate.monthOfYear) {
                    binding.selectedMonth.monthTextView.text = formatMonth(lastItemDate.toDate())
                    visibleMonth = lastItemDate.monthOfYear
                    viewModel.setPreviousMonthPossibility(lastItemDate.monthOfYear > currDate.monthOfYear && lastItemDate.year >= currDate.year)
                }

                if (dx > 0) {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false
                            previousTotal = totalItemCount
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)
                    ) {
                        viewModel.loadMoreDates()
                        loading = true
                    }
                }
            }
        })

        binding.periodRecyclerView.adapter = PurchasePeriodAdapter {
            viewModel.selectPeriod(it)
        }

        binding.selectedMonth.nextMonthImageView.setOnDebouncedClickListener {
            nextMonth()
        }

        binding.selectedMonth.previousMonthImageView.setOnDebouncedClickListener {
            prevMonth()
        }

        binding.selectTextView.setOnDebouncedClickListener {
            viewModel.createPurchaseDateTimeModel().let {
                purchaseDateTimeListener?.onSelectDateTimeClick(it)
            }
            dismissAllowingStateLoss()
        }
        subscribe(viewModel.dateListObserver) {
            dateListAdapter.setItems(it)
        }
        subscribe(viewModel.selectedDateTimeObserver) {
            periodListAdapter.setSelected(it)

            binding.selectTextView.isEnabled = it.selectedPeriodModel != null
        }
        subscribe(viewModel.hasPreviousMonthObserver) {
            binding.selectedMonth.previousMonthImageView.isEnabled = it

            binding.selectedMonth.previousMonthImageView.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (it) R.drawable.ic__arrow_left_24px
                    else R.drawable.ic__arrow_left_24px_secondary300
                )
            )
        }
    }

    private fun formatMonth(monthForDate: Date): String {
        return SimpleDateFormat(DATE_PATTERN_YEAR_MONTH, Locale.getDefault())
            .format(monthForDate)
            .capitalize(Locale.getDefault())
    }

    private fun nextMonth() {
        visibleMonth?.let {
            val currLastPos =
                (binding.datesRecyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            val lastPosItem = dateListAdapter.getItem(currLastPos).date.dayOfYear

            val newMonthFirstDay =
                LocalDateTime().withMonthOfYear(it).plusMonths(1).withDayOfMonth(1).dayOfYear
            val scrollPosition = currLastPos + (newMonthFirstDay - lastPosItem) + 6
            if (scrollPosition > 0) {
                binding.datesRecyclerView.scrollToPosition(scrollPosition)
                viewModel.loadMoreDates()
            }
        }
    }

    private fun prevMonth() {
        visibleMonth?.let {
            val currFirstPos =
                (binding.datesRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            val firstPosItem = dateListAdapter.getItem(currFirstPos).date.dayOfYear

            val newMonthFirstDay =
                LocalDateTime().withMonthOfYear(it).minusMonths(1).withDayOfMonth(
                    if (visibleMonth == currDate.monthOfYear + 1) {
                        currDate.dayOfMonth
                    } else {
                        1
                    }
                ).dayOfYear

            binding.datesRecyclerView.scrollToPosition(currFirstPos - (firstPosItem - newMonthFirstDay))
        }
    }

    interface PurchaseDateTimeListener : Serializable {
        fun onSelectDateTimeClick(purchaseDateTimeModel: PurchaseDateTimeModel)
    }
}
