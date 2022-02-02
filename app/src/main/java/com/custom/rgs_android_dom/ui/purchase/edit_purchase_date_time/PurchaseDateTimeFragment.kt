package com.custom.rgs_android_dom.ui.purchase.edit_purchase_date_time

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPurchaseDateTimeBinding
import com.custom.rgs_android_dom.domain.purchase.model.PurchaseDateTimeModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import java.io.Serializable

class PurchaseDateTimeFragment : BaseBottomSheetModalFragment<PurchaseDateTimeViewModel, FragmentPurchaseDateTimeBinding>() {

    companion object {
        private const val ARG_PURCHASE_DATE_TIME = "ARG_PURCHASE_DATE_TIME"

        fun newInstance(purchaseDateTimeModel: PurchaseDateTimeModel?): PurchaseDateTimeFragment {
            return PurchaseDateTimeFragment().args {
                putSerializable(ARG_PURCHASE_DATE_TIME, purchaseDateTimeModel)
            }
        }
    }

    private var purchaseDateTimeListener: PurchaseDateTimeListener? = null

    private val dateListAdapter: PurchaseDatesAdapter
        get() = binding.datesRecyclerView.adapter as PurchaseDatesAdapter

    private val periodListAdapter: PurchasePeriodAdapter
        get() = binding.periodRecyclerView.adapter as PurchasePeriodAdapter

    override val TAG: String = "PURCHASE_DATE_TIME_FRAGMENT"

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_PURCHASE_DATE_TIME))
    }

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

        binding.periodRecyclerView.adapter = PurchasePeriodAdapter {
            viewModel.selectPeriod(it)
        }

        binding.selectedMonth.nextMonthImageView.setOnDebouncedClickListener {
            viewModel.plusWeek()
        }

        binding.selectedMonth.previousMonthImageView.setOnDebouncedClickListener {
            viewModel.minusWeek()
        }

        binding.selectTextView.setOnDebouncedClickListener {
            viewModel.createPurchaseDateTimeModel()
                ?.let { purchaseDateTimeListener?.onSelectDateTimeClick(it) }
            dismissAllowingStateLoss()
        }

        subscribe(viewModel.dateListObserver) {

            if (it.datesForCalendar.isNotEmpty()) {
                dateListAdapter.setItems(it.datesForCalendar)
            }
            if (it.periodList.isNotEmpty()) {
                periodListAdapter.setItems(it.periodList)
            }
            binding.selectedMonth.monthTextView.text = it.selectedMonth

            if (it.isPreviousMonthButtonEnable) {
                binding.selectedMonth.previousMonthImageView.isEnabled = true
                binding.selectedMonth.previousMonthImageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic__arrow_left_24px
                    )
                )
            } else {
                binding.selectedMonth.previousMonthImageView.isEnabled = false
                binding.selectedMonth.previousMonthImageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic__arrow_left_24px_secondary300
                    )
                )
            }
            binding.selectTextView.isEnabled = it.isSelectButtonEnable
        }
    }

    interface PurchaseDateTimeListener : Serializable {
        fun onSelectDateTimeClick(purchaseDateTimeModel: PurchaseDateTimeModel)
    }
}
