package com.custom.rgs_android_dom.domain.purchase_service.model

import com.custom.rgs_android_dom.domain.repositories.PurchaseRepository
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DAY_OF_WEEK
import com.custom.rgs_android_dom.utils.formatTo
import io.reactivex.Single
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime

class PurchaseInteractor(private val purchaseRepository: PurchaseRepository) {

    fun createPeriodList(): MutableList<PurchasePeriodModel> {
        val periodList: MutableList<PurchasePeriodModel> = mutableListOf()
        periodList.apply {
            add(PurchasePeriodModel(0, "Утро", "6:00-9:00", isSelected = false))
            add(PurchasePeriodModel(1, "До полудня", "9:00-12:00", isSelected = false))
            add(PurchasePeriodModel(2, "День", "12:00-15:00", isSelected = false))
            add(PurchasePeriodModel(3, "Вечер", "15:00-18:00", isSelected = false))
        }
        return periodList
    }

    fun updateDateWeek(
        purchaseDateTimeModel: PurchaseDateTimeModel,
        periodList: List<PurchasePeriodModel>
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
        val currentMouth: String = purchaseDateTimeModel.date.monthOfYear().asString

        return PurchaseDateModel(
            selectedMouth = currentMouth,
            isPreviousMouthButtonEnable = checkPreviousButtonEnable(dateForCalendarList[4].date.minusWeeks(1)),
            datesForCalendar = dateForCalendarList,
            isSelectButtonEnable = false,
            periodList = checkPeriodEnable(periodList)
        )
    }

    private fun checkPeriodEnable(
        periodList: List<PurchasePeriodModel>,
        selectedDate: LocalDateTime? = null
    ): List<PurchasePeriodModel> {
        val currentDate = LocalDateTime.now()
        if (selectedDate?.dayOfYear == currentDate.dayOfYear) {
            val currentTime = LocalTime.now()
            periodList.forEach {
                when (it.timeInterval) {
                    "6:00-9:00" -> {
                        it.isClickable = LocalTime("9:00") >= currentTime
                    }
                    "9:00-12:00" -> {
                        it.isClickable = LocalTime("12:00") >= currentTime
                    }
                    "12:00-15:00" -> {
                        it.isClickable = LocalTime("15:00") >= currentTime
                    }
                    "15:00-18:00" -> {
                        it.isClickable = LocalTime("18:00") >= currentTime
                    }

                }
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
