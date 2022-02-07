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
import java.lang.IllegalArgumentException
import java.util.*

class PurchaseInteractor(private val purchaseRepository: PurchaseRepository) {

    private val periodsList = listOf(
        PurchaseTimePeriodModel(0, timeOfDay = "Утро", timeFrom = "6:00", timeTo = "9:00"),
        PurchaseTimePeriodModel(1, timeOfDay = "До полудня", timeFrom = "9:00", timeTo = "12:00"),
        PurchaseTimePeriodModel(2, timeOfDay = "День", timeFrom = "12:00", timeTo = "15:00"),
        PurchaseTimePeriodModel(3, timeOfDay = "Вечер", timeFrom = "15:00", timeTo = "18:00")
    )

    fun createDateWeek(): PurchaseDateModel {
        val purchaseDateTimeModel = PurchaseDateTimeModel()
        var firstDayInWeek =
            purchaseDateTimeModel.selectedDate.minusDays(purchaseDateTimeModel.selectedDate.dayOfWeek - 1)
        val dateForCalendarList: MutableList<DateForCalendarModel> = mutableListOf()
        for (counter in 0..6) {
            dateForCalendarList.add(
                DateForCalendarModel(
                    dayInWeek = firstDayInWeek.formatTo(DATE_PATTERN_DAY_OF_WEEK),
                    dateNumber = firstDayInWeek.dayOfMonth.toString(),
                    date = firstDayInWeek,
                    isEnable = firstDayInWeek.dayOfYear >= LocalDateTime.now().dayOfYear,
                    isSelected = firstDayInWeek.dayOfYear == purchaseDateTimeModel.selectedDate.dayOfYear
                )
            )
            firstDayInWeek = firstDayInWeek.plusDays(1)
        }

        val currentMonth = SimpleDateFormat(DATE_PATTERN_YEAR_MONTH, Locale.getDefault()).format(firstDayInWeek.minusDays(1).toDate()).capitalize(Locale.getDefault())

        return PurchaseDateModel(
            selectedMonth = currentMonth,
            isPreviousMonthButtonEnable = checkPreviousButtonEnable(
                dateForCalendarList[6].date.minusWeeks(1)
            ),
            datesForCalendar = dateForCalendarList,
            periodList = periodsList,
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
        val currentDateTime = LocalDateTime.now()
        if (selectedDate.dayOfYear == currentDateTime.dayOfYear) {
            periodList.forEach {
                it.isSelectable = when (it.timeFrom) {
                    "6:00" -> LocalTime("9:00") >= currentDateTime.toLocalTime()
                    "9:00" -> LocalTime("12:00") >= currentDateTime.toLocalTime()
                    "12:00" -> LocalTime("15:00") >= currentDateTime.toLocalTime()
                    "15:00" -> LocalTime("18:00") >= currentDateTime.toLocalTime()
                    else -> throw IllegalArgumentException("Wrong argument: ${it.timeFrom}")
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
        return LocalDateTime.now().dayOfYear <= lastDateOfPreviousWeek.dayOfYear
    }

    fun getSavedCards(): Single<List<CardModel>> {
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
