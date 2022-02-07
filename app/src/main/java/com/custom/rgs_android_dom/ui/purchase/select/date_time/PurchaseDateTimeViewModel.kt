package com.custom.rgs_android_dom.ui.purchase.select.date_time

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.purchase.model.PurchaseDateModel
import com.custom.rgs_android_dom.domain.purchase.model.PurchaseDateTimeModel
import com.custom.rgs_android_dom.domain.purchase.PurchaseInteractor
import com.custom.rgs_android_dom.domain.purchase.model.DateForCalendarModel
import com.custom.rgs_android_dom.domain.purchase.model.PurchaseTimePeriodModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_ONLY
import com.custom.rgs_android_dom.utils.formatTo
import org.joda.time.LocalDateTime

class PurchaseDateTimeViewModel(
    private val purchaseInteractor: PurchaseInteractor
) : BaseViewModel() {

    private val dateListController = MutableLiveData<PurchaseDateModel>()
    val dateListObserver: LiveData<PurchaseDateModel> = dateListController

//    private val calendarListController = MutableLiveData<DateForCalendarModel>()
//    val calendarListObserver: LiveData<DateForCalendarModel> = calendarListController

    private var selectedDateTime = PurchaseDateTimeModel()

    init {
        updateDateListController(purchaseInteractor.createDateWeek())
    }

    fun plusMonth() {
//        val date = dateListController.value?.datesForCalendar?.first()?.date?.plusWeeks(1)
//        val newDateListController = date?.let {
//            selectedDateTime = selectedDateTime.copy(selectedDate = it)
//
//            dateListController.value?.copy(date = it)
//        }
//        newDateListController?.let {
//            updateDateListController(purchaseInteractor.updateDateWeek(it))
//        }
    }

    fun minusMonth() {
//        val date = dateListController.value?.datesForCalendar?.first()?.date?.minusWeeks(1)
//        val newDateListController = date?.let {
//            dateListController.value?.copy(date = it)
//        }
//        newDateListController?.let {
//            val newDateList = purchaseInteractor.updateDateWeek(it)
//            selectedDateTime = selectedDateTime.copy(selectedDate = newDateList.date)
//            updateDateListController(newDateList)
//        }
    }

    fun loadMoreDates() {

    }

    fun selectDay(date: LocalDateTime) {
        selectedDateTime = selectedDateTime.copy(selectedDate = date)

        dateListController.value?.let {
            updateDateListController(purchaseInteractor.selectDay(it, date))
        }
    }

    fun selectPeriod(purchasePeriodModel: PurchaseTimePeriodModel) {
        selectedDateTime = selectedDateTime.copy(selectedPeriodModel = purchasePeriodModel)

        val periodList = dateListController.value?.periodList
        periodList?.forEach {
            it.isSelected = purchasePeriodModel.id == it.id
        }
        periodList?.let {
            updateDateListController(
                dateListController.value?.copy(
                    periodList = it,
                    selectedPeriod = purchasePeriodModel
                )
            )
        }
    }

    private fun updateDateListController(dateList: PurchaseDateModel?) {
        dateListController.value = dateList?.copy(
            periodList = dateList.periodList.onEach {
                val isThatDayPeriod = selectedDateTime.selectedPeriodModel?.id == it.id &&
                        selectedDateTime.selectedDate.formatTo(DATE_PATTERN_DATE_ONLY) == dateList.date.formatTo(
                    DATE_PATTERN_DATE_ONLY
                )
                if (!it.isSelectable && isThatDayPeriod)
                    selectedDateTime = selectedDateTime.copy(selectedPeriodModel = null)

                it.isSelected = it.isSelectable && isThatDayPeriod
            }
        )
    }

    fun createPurchaseDateTimeModel(): PurchaseDateTimeModel? {
        val selectedDate = dateListController.value?.date
        val selectedPeriod = dateListController.value?.selectedPeriod
        return if (selectedDate != null && selectedPeriod != null) PurchaseDateTimeModel(
            selectedDate,
            selectedPeriod
        )
        else null
    }

}
