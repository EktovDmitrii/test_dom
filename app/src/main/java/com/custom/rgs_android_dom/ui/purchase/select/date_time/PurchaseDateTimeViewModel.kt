package com.custom.rgs_android_dom.ui.purchase.select.date_time

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseDateTimeModel
import com.custom.rgs_android_dom.domain.purchase.models.DateForCalendarModel
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseTimePeriodModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.purchase.select.date_time.PurchaseDateTimeFragment.Companion.ITEM_SIZE_THRESHOLD
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DAY_OF_WEEK
import com.custom.rgs_android_dom.utils.formatTo
import org.joda.time.LocalDateTime

class PurchaseDateTimeViewModel : BaseViewModel() {

    private val dateListController = MutableLiveData<List<DateForCalendarModel>>()
    val dateListObserver: LiveData<List<DateForCalendarModel>> = dateListController

    private var calendarList: MutableList<DateForCalendarModel> = mutableListOf()

    private val hasPreviousMonthController = MutableLiveData(false)
    val hasPreviousMonthObserver: LiveData<Boolean> = hasPreviousMonthController

    private var selectedDateTimeController = MutableLiveData<PurchaseDateTimeModel>()
    val selectedDateTimeObserver: LiveData<PurchaseDateTimeModel> = selectedDateTimeController

    private var selectedDateTime = PurchaseDateTimeModel()

    init {
        fillCalendarList(selectedDateTime.selectedDate, true)
        selectedDateTimeController.value = selectedDateTime
    }

    private fun fillCalendarList(dateTimeModel: LocalDateTime, isFirst: Boolean) {
        var firstDayInWeek = if (isFirst) dateTimeModel.minusDays(dateTimeModel.dayOfWeek - 1)
        else dateTimeModel.plusDays(1)
        for (counter in 0..ITEM_SIZE_THRESHOLD) {
            calendarList.add(
                DateForCalendarModel(
                    dayInWeek = firstDayInWeek.formatTo(DATE_PATTERN_DAY_OF_WEEK),
                    dateNumber = firstDayInWeek.dayOfMonth.toString(),
                    date = firstDayInWeek,
                    isEnable = firstDayInWeek.dayOfYear >= LocalDateTime.now().dayOfYear || firstDayInWeek.year > LocalDateTime.now().year,
                    isSelected = selectedDateTime.selectedDate == firstDayInWeek
                )
            )
            firstDayInWeek = firstDayInWeek.plusDays(1)
        }
        dateListController.value = calendarList
    }

    fun loadMoreDates() {
        fillCalendarList(calendarList.last().date, false)
    }

    fun selectDay(date: LocalDateTime) {
        selectedDateTime = selectedDateTime.copy(selectedDate = date, selectedPeriodModel = null)
        selectedDateTimeController.value = selectedDateTime

        calendarList.forEach {
            it.isSelected = it.date == date
        }
        dateListController.value = calendarList
    }

    fun selectPeriod(purchasePeriodModel: PurchaseTimePeriodModel) {
        selectedDateTime = selectedDateTime.copy(
            selectedPeriodModel = purchasePeriodModel
        )
        selectedDateTimeController.value = selectedDateTime
    }

    fun setPreviousMonthPossibility(hasPrevious: Boolean) {
        hasPreviousMonthController.value = hasPrevious
    }

    fun createPurchaseDateTimeModel(): PurchaseDateTimeModel = selectedDateTime

}
