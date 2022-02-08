package com.custom.rgs_android_dom.ui.purchase.select.date_time

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseDateModel
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseDateTimeModel
import com.custom.rgs_android_dom.domain.purchase.PurchaseInteractor
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseTimePeriodModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import org.joda.time.LocalDateTime

class PurchaseDateTimeViewModel(
    purchaseDateTimeModel: PurchaseDateTimeModel?,
    private val purchaseInteractor: PurchaseInteractor
) : BaseViewModel() {

    private var purchaseDateTime: PurchaseDateTimeModel? = null

    var periodList: MutableList<PurchaseTimePeriodModel> = mutableListOf()

    private val dateListController = MutableLiveData<PurchaseDateModel>()
    val dateListObserver: LiveData<PurchaseDateModel> = dateListController


    init {
        periodList = purchaseInteractor.timePeriods
        purchaseDateTime = purchaseDateTimeModel

        if (purchaseDateTimeModel != null) {
            dateListController.value =
                purchaseInteractor.createDateWeek(purchaseDateTimeModel, periodList)
        } else {
            dateListController.value =
                purchaseInteractor.createDateWeek(PurchaseDateTimeModel(), periodList)
        }
    }

    fun plusWeek() {
        val date = dateListController.value?.datesForCalendar?.first()?.date?.plusWeeks(1)
        val newDateListController = date?.let {
            dateListController.value?.copy(
                date = it
            )
        }
        newDateListController?.let {
            dateListController.value = purchaseInteractor.updateDateWeek(it)
        }
    }

    fun minusWeek() {
        val date = dateListController.value?.datesForCalendar?.first()?.date?.minusWeeks(1)
        val newDateListController = date?.let {
            dateListController.value?.copy(
                date = it
            )
        }
        newDateListController?.let {
            dateListController.value = purchaseInteractor.updateDateWeek(it)
        }
    }

    fun selectDay(date: LocalDateTime) {
        dateListController.value =
            dateListController.value?.let { purchaseInteractor.selectDay(it, date) }
        checkIsSelectButtonEnable()

    }

    fun selectPeriod(purchasePeriodModel: PurchaseTimePeriodModel) {
        val periodList = dateListController.value?.periodList
        periodList?.forEach {
            it.isSelected = purchasePeriodModel.id == it.id
        }
        dateListController.value = periodList?.let {
            dateListController.value?.copy(
                periodList = it,
                selectedPeriod = purchasePeriodModel
            )
        }
        checkIsSelectButtonEnable()
    }

    fun createPurchaseDateTimeModel(): PurchaseDateTimeModel? {
        val selectedDate = dateListController.value?.date
        val selectedPeriod = dateListController.value?.selectedPeriod
        return if (selectedDate != null && selectedPeriod != null)
            PurchaseDateTimeModel(
                selectedDate,
                selectedPeriod
            )
        else purchaseDateTime
    }

    private fun checkIsSelectButtonEnable() {
        val selectedDate = dateListController.value?.date
        val selectedPeriod = dateListController.value?.selectedPeriod
        val isSelectButtonEnable = selectedDate != null && selectedPeriod != null
        dateListController.value =
            dateListController.value?.copy(
                isSelectButtonEnable = isSelectButtonEnable
            )
    }

}
