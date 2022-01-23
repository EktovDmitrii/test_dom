package com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_date_time

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.purchase_service.PurchaseDateContent
import com.custom.rgs_android_dom.domain.purchase_service.PurchaseDateModel
import com.custom.rgs_android_dom.domain.purchase_service.PurchaseDateTimeModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import org.joda.time.LocalDateTime
import java.text.SimpleDateFormat
import java.util.*

class PurchaseDateTimeViewModel(purchaseDateTimeModel: PurchaseDateTimeModel?) : BaseViewModel() {

    var purchaseDateTime: PurchaseDateTimeModel? = null

    val formatter = SimpleDateFormat("EE", Locale.getDefault())

    private val dateListController = MutableLiveData<PurchaseDateModel>()
    val dateListObserver: LiveData<PurchaseDateModel> = dateListController

    init {
        if (purchaseDateTimeModel != null) {
            purchaseDateTime = purchaseDateTimeModel
            updateDateWeek(purchaseDateTimeModel.date)
        } else {
            updateDateWeek(LocalDateTime.now())
        }
    }

    fun updateDateWeek(date: LocalDateTime) {
        var firstDayInWeek = date.minusDays(date.dayOfWeek - 1)
        val dateList: MutableList<PurchaseDateContent> = mutableListOf()
        for (counter in 0..6) {
            dateList.add(
                PurchaseDateContent(
                    id = counter,
                    dayInWeek = formatter.format(firstDayInWeek.toDate()),
                    dateNumber = firstDayInWeek.dayOfMonth.toString(),
                    date = firstDayInWeek,
                    isEnable = firstDayInWeek.dayOfYear >= LocalDateTime.now().dayOfYear,
                    isSelected = firstDayInWeek.dayOfYear == date.dayOfYear
                )
            )
            firstDayInWeek = firstDayInWeek.plusDays(1)
        }
        val currentMouth: String = date.monthOfYear().asString

        dateListController.value = PurchaseDateModel(
            currentMouth,
            checkPreviousButtonEnable(dateList[4].date.minusWeeks(1)),
            dateList,
            true,
            false
        )
    }

    private fun checkTimesPeriodsEnable() {

    }

    private fun checkPreviousButtonEnable(lastDateOfPreviousWeek: LocalDateTime): Boolean {
        val date = LocalDateTime.now()
        return date.dayOfYear <= lastDateOfPreviousWeek.dayOfYear
    }

    fun plusWeek() {
        val dayList = dateListController.value?.dates?.first()?.date?.plusWeeks(1)
        dayList?.let { updateDateWeek(it) }
    }

    fun minusWeek() {
        val dayList = dateListController.value?.dates?.first()?.date?.minusWeeks(1)
        dayList?.let { updateDateWeek(it) }
    }
}
