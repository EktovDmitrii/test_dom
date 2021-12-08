package com.custom.rgs_android_dom.ui.property.add.select_type

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.domain.property.view_states.SelectPropertyTypeViewState
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ADD_PROPERTY
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.property.add.details.PropertyDetailsFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SelectPropertyTypeViewModel(private val propertyName: String,
                                  private val propertyAddress: String,
                                  private val propertyInteractor: PropertyInteractor) : BaseViewModel() {

    private val selectPropertyTypeViewStateController = MutableLiveData<SelectPropertyTypeViewState>()
    val selectPropertyTypeViewStateObserver: LiveData<SelectPropertyTypeViewState> = selectPropertyTypeViewStateController

    private var propertyType = PropertyType.UNDEFINED

    init {

        propertyInteractor.selectPropertyTypeViewStateSubject
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

    fun onNextClick() {
        ScreenManager.showScreenScope(
            PropertyDetailsFragment.newInstance(
                propertyName,
                propertyAddress,
                propertyType
            ), ADD_PROPERTY
        )
    }

    fun onSelectHomeClick(){
        propertyType = PropertyType.HOUSE
        propertyInteractor.selectHome()
    }

    fun onSelectAppartmentClick(){
        propertyType = PropertyType.APARTMENT
        propertyInteractor.selectApartment()
    }

    fun onBackClick(){
        closeController.value = Unit
    }

}