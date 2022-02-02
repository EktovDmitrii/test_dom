package com.custom.rgs_android_dom.domain.purchase

import android.icu.text.SimpleDateFormat
import com.custom.rgs_android_dom.domain.purchase.model.*
import com.custom.rgs_android_dom.domain.repositories.PurchaseRepository
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DAY_OF_WEEK
import com.custom.rgs_android_dom.utils.DATE_PATTERN_YEAR_MONTH
import com.custom.rgs_android_dom.utils.formatTo
import io.reactivex.Single
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import java.util.*

class PurchaseInteractor(private val purchaseRepository: PurchaseRepository) {

    val timePeriods = mutableListOf<PurchaseTimePeriodModel>().apply {
        add(PurchaseTimePeriodModel(0, timeOfDay = "Утро", displayTime = "6:00 – 9:00", timeFrom = "6:00", timeTo = "9:00", isSelected = false))
        add(PurchaseTimePeriodModel(1, timeOfDay = "До полудня", displayTime = "9:00 – 12:00", timeFrom = "9:00", timeTo = "12:00", isSelected = false))
        add(PurchaseTimePeriodModel(2, timeOfDay = "День", displayTime = "12:00 – 15:00", timeFrom = "12:00", timeTo = "15:00", isSelected = false))
        add(PurchaseTimePeriodModel(3, timeOfDay = "Вечер", displayTime = "15:00 – 18:00", timeFrom = "15:00", timeTo = "18:00", isSelected = false))
    }

    fun createDateWeek(
        purchaseDateTimeModel: PurchaseDateTimeModel,
        periodList: List<PurchaseTimePeriodModel>
    ): PurchaseDateModel {
        var firstDayInWeek =
            purchaseDateTimeModel.date.minusDays(purchaseDateTimeModel.date.dayOfWeek - 1)
        val dateForCalendarList: MutableList<DateForCalendarModel> = mutableListOf()
        for (counter in 0..6) {
            dateForCalendarList.add(
                DateForCalendarModel(
                    dayInWeek = firstDayInWeek.formatTo(DATE_PATTERN_DAY_OF_WEEK),
                    dateNumber = firstDayInWeek.dayOfMonth.toString(),
                    date = firstDayInWeek,
                    isEnable = firstDayInWeek.dayOfYear >= LocalDateTime.now().dayOfYear,
                    isSelected = firstDayInWeek.dayOfYear == purchaseDateTimeModel.date.dayOfYear
                )
            )
            firstDayInWeek = firstDayInWeek.plusDays(1)
        }

        val currentMonth = SimpleDateFormat(DATE_PATTERN_YEAR_MONTH, Locale.getDefault()).format(firstDayInWeek.minusDays(1).toDate()).capitalize(Locale.getDefault())

        val periodicList = checkPeriodEnable(periodList, selectedDate = purchaseDateTimeModel.date)
        periodList.find { it.id == purchaseDateTimeModel.selectedPeriodModel?.id }?.isSelected = true

        return PurchaseDateModel(
            selectedMonth = currentMonth,
            isPreviousMonthButtonEnable = checkPreviousButtonEnable(
                dateForCalendarList[6].date.minusWeeks(
                    1
                )
            ),
            datesForCalendar = dateForCalendarList,
            isSelectButtonEnable = false,
            periodList = periodicList,
            selectedPeriod = purchaseDateTimeModel.selectedPeriodModel
        )
    }

    fun updateDateWeek(
        purchaseDateModel: PurchaseDateModel
    ): PurchaseDateModel {
        var firstDayInWeek = purchaseDateModel.date.minusDays(purchaseDateModel.date.dayOfWeek - 1)
        val dateForCalendarList: MutableList<DateForCalendarModel> = mutableListOf()
        for (counter in 0..6) {
            dateForCalendarList.add(
                DateForCalendarModel(
                    dayInWeek = firstDayInWeek.formatTo(DATE_PATTERN_DAY_OF_WEEK),
                    dateNumber = firstDayInWeek.dayOfMonth.toString(),
                    date = firstDayInWeek,
                    isEnable = firstDayInWeek.dayOfYear >= LocalDateTime.now().dayOfYear,
                    isSelected = false
                )
            )
            firstDayInWeek = firstDayInWeek.plusDays(1)
        }
        val currentMonth = SimpleDateFormat(DATE_PATTERN_YEAR_MONTH, Locale.getDefault()).format(firstDayInWeek.minusDays(1).toDate()).capitalize(Locale.getDefault())
        val updatedDateListForCalendar = checkSelectedDate(dateForCalendarList)

        val currentDate = updatedDateListForCalendar.find { it.isSelected }?.date!!
        return purchaseDateModel.copy(
            date = currentDate,
            selectedMonth = currentMonth,
            isPreviousMonthButtonEnable = checkPreviousButtonEnable(
                dateForCalendarList[6].date.minusWeeks(
                    1
                )
            ),
            datesForCalendar = updatedDateListForCalendar,
            periodList = checkPeriodEnable(purchaseDateModel.periodList, selectedDate = currentDate)
        )
    }

    private fun checkSelectedDate(datesForCalendar: List<DateForCalendarModel>): List<DateForCalendarModel> {
        val currentDate = datesForCalendar.find { it.isEnable }
        datesForCalendar.forEach {
            if (it == currentDate) it.isSelected = true
        }
        return datesForCalendar
    }

    fun selectDay(
        purchaseDateModel: PurchaseDateModel,
        selectedDate: LocalDateTime
    ): PurchaseDateModel {
        val datesForCalendar = purchaseDateModel.datesForCalendar
        datesForCalendar.forEach {
            it.isSelected = selectedDate.dayOfYear == it.date.dayOfYear
        }
        val periodList = checkPeriodEnable(purchaseDateModel.periodList, selectedDate)
        var isSelectedPeriodChanged = false
        periodList.forEach {
            if (!it.isSelectable && it.isSelected) {
                it.isSelected = false
                isSelectedPeriodChanged = true
            }
        }
        return if (isSelectedPeriodChanged)
            purchaseDateModel.copy(
                date = selectedDate,
                periodList = periodList,
                selectedPeriod = null
            )
        else
            purchaseDateModel.copy(
                date = selectedDate,
                periodList = periodList
            )
    }

    private fun checkPeriodEnable(
        periodList: List<PurchaseTimePeriodModel>,
        selectedDate: LocalDateTime
    ): List<PurchaseTimePeriodModel> {
        val currentDate = LocalDateTime.now()
        if (selectedDate.dayOfYear == currentDate.dayOfYear) {
            val currentTime = LocalTime.now()
            periodList.forEach {
                when (it.timeFrom) {
                    "6:00" -> {
                        it.isSelectable = LocalTime("9:00") >= currentTime
                    }
                    "9:00" -> {
                        it.isSelectable = LocalTime("12:00") >= currentTime
                    }
                    "12:00" -> {
                        it.isSelectable = LocalTime("15:00") >= currentTime
                    }
                    "15:00" -> {
                        it.isSelectable = LocalTime("18:00") >= currentTime
                    }
                }
                it.isSelected = false
            }
        } else {
            periodList.forEach {
                it.isSelectable = true
                it.isSelected = false
            }
        }
        return periodList
    }

    private fun checkPreviousButtonEnable(lastDateOfPreviousWeek: LocalDateTime): Boolean {
        val date = LocalDateTime.now()
        return date.dayOfYear <= lastDateOfPreviousWeek.dayOfYear
    }

    fun getSavedCards(): Single<List<SavedCardModel>> {
        return purchaseRepository.getSavedCards()
    }

    fun makeProductPurchase(
        productId: String,
        bindingId: String?,
        email: String,
        saveCard: Boolean,
        objectId: String,
        deliveryDate: String,
        timeFrom: String,
        timeTo: String,
    ): Single<String> {
        return purchaseRepository.makeProductPurchase(
            productId = productId,
            bindingId = bindingId,
            email = email,
            saveCard = saveCard,
            objectId = objectId,
            deliveryDate = deliveryDate,
            timeFrom = timeFrom,
            timeTo = timeTo
        )
    }
}
