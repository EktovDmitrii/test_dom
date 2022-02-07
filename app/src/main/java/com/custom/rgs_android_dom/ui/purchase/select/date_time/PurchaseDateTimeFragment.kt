package com.custom.rgs_android_dom.ui.purchase.select.date_time

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPurchaseDateTimeBinding
import com.custom.rgs_android_dom.domain.purchase.model.PurchaseDateTimeModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import java.io.Serializable

class PurchaseDateTimeFragment : BaseBottomSheetModalFragment<PurchaseDateTimeViewModel, FragmentPurchaseDateTimeBinding>() {

    companion object {
        fun newInstance(): PurchaseDateTimeFragment = PurchaseDateTimeFragment()
    }

    private var purchaseDateTimeListener: PurchaseDateTimeListener? = null

    private val dateListAdapter: PurchaseDatesAdapter
        get() = binding.datesRecyclerView.adapter as PurchaseDatesAdapter

    private val periodListAdapter: PurchasePeriodAdapter
        get() = binding.periodRecyclerView.adapter as PurchasePeriodAdapter

    override val TAG: String = "PURCHASE_DATE_TIME_FRAGMENT"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
            var visibleThreshold = 5
            var firstVisibleItem = 0
            var visibleItemCount = 0
            var totalItemCount = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = (binding.datesRecyclerView.layoutManager as LinearLayoutManager)
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
                        <= (firstVisibleItem + visibleThreshold)) {
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
            viewModel.plusMonth()
        }

        binding.selectedMonth.previousMonthImageView.setOnDebouncedClickListener {
            viewModel.minusMonth()
        }

        binding.selectTextView.setOnDebouncedClickListener {
            viewModel.createPurchaseDateTimeModel()?.let {
                purchaseDateTimeListener?.onSelectDateTimeClick(it)
            }
            dismissAllowingStateLoss()
        }

        subscribe(viewModel.dateListObserver) {
            dateListAdapter.setItems(it.datesForCalendar)
            periodListAdapter.setItems(it.periodList)

            binding.selectedMonth.monthTextView.text = it.selectedMonth
            binding.selectedMonth.previousMonthImageView.isEnabled = it.isPreviousMonthButtonEnable
            binding.selectTextView.isEnabled = it.selectedPeriod != null

            binding.selectedMonth.previousMonthImageView.setImageDrawable(
                ContextCompat.getDrawable(requireContext(),
                    if (it.isPreviousMonthButtonEnable) R.drawable.ic__arrow_left_24px
                    else R.drawable.ic__arrow_left_24px_secondary300
                )
            )
        }
    }

    interface PurchaseDateTimeListener : Serializable {
        fun onSelectDateTimeClick(purchaseDateTimeModel: PurchaseDateTimeModel)
    }
}
