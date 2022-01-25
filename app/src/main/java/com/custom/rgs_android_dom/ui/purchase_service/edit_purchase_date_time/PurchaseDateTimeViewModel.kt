package com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_date_time

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchaseDateModel
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchaseDateTimeModel
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchaseInteractor
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchasePeriodModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import org.joda.time.LocalDateTime

class PurchaseDateTimeViewModel(
    purchaseDateTimeModel: PurchaseDateTimeModel?,
    private val purchaseInteractor: PurchaseInteractor
) : BaseViewModel() {

    private var purchaseDateTime: PurchaseDateTimeModel? = null

    var periodList: MutableList<PurchasePeriodModel> = mutableListOf()

    private val dateListController = MutableLiveData<PurchaseDateModel>()
    val dateListObserver: LiveData<PurchaseDateModel> = dateListController


    init {
        periodList = purchaseInteractor.createPeriodList()
        purchaseDateTime = purchaseDateTimeModel

        if (purchaseDateTimeModel != null) {
            dateListController.value =
                purchaseInteractor.updateDateWeek(purchaseDateTimeModel, periodList)
        } else {
            dateListController.value = purchaseInteractor.updateDateWeek(PurchaseDateTimeModel(), periodList)
        }
    }

    fun plusWeek() {
        val dayList = dateListController.value?.datesForCalendar?.first()?.date?.plusWeeks(1)
        dayList?.let { date ->
            purchaseDateTime?.date = date
            purchaseDateTime?.let { it ->
                dateListController.value = purchaseInteractor.updateDateWeek(it, periodList)
            }
        }
    }

    fun minusWeek() {
        val dayList = dateListController.value?.datesForCalendar?.first()?.date?.minusWeeks(1)
        dayList?.let { date ->
            purchaseDateTime?.date = date
            purchaseDateTime?.let { it ->
                dateListController.value = purchaseInteractor.updateDateWeek(it, periodList)
            }
        }
    }

    fun selectDay(date: LocalDateTime) {
        val datesForCalendar = dateListController.value?.datesForCalendar
        datesForCalendar?.forEach {
            it.isSelected = date.dayOfYear == it.date.dayOfYear
        }
        dateListController.value = datesForCalendar?.let {
            dateListController.value?.copy(
                datesForCalendar = it
            )
        }
    }

    fun selectPeriod(purchasePeriodModel: PurchasePeriodModel) {
        val periodList = dateListController.value?.periodList
        periodList?.forEach {
            it.isSelected = purchasePeriodModel.id == it.id
        }
        dateListController.value = periodList?.let {
            dateListController.value?.copy(
                periodList = it
            )
        }
    }
}
