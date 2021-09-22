package com.custom.rgs_android_dom.ui.property.add.select_type

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.property.select_type.SelectPropertyTypeInteractor
import com.custom.rgs_android_dom.domain.property.select_type.view_states.SelectPropertyTypeViewState
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SelectPropertyTypeViewModel(
    private val selectPropertyTypeInteractor: SelectPropertyTypeInteractor
) : BaseViewModel() {

    private val selectPropertyTypeViewStateController = MutableLiveData<SelectPropertyTypeViewState>()
    val selectPropertyTypeViewStateObserver: LiveData<SelectPropertyTypeViewState> = selectPropertyTypeViewStateController

    init {
        selectPropertyTypeInteractor.selectPropertyTypeViewStateSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    selectPropertyTypeViewStateController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onBackClick() {
        closeController.value = Unit
    }

    fun onNextClick() {

    }

    fun onSelectHomeClick(){
        selectPropertyTypeInteractor.onSelectHomeClick()
    }

    fun onSelectAppartmentClick(){
        selectPropertyTypeInteractor.onSelectAppartmentClick()
    }

}