package com.custom.rgs_android_dom.ui.property.add.select_type

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.domain.property.select_type.SelectPropertyTypeInteractor
import com.custom.rgs_android_dom.domain.property.select_type.view_states.SelectPropertyTypeViewState
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ADD_PROPERTY
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.property.add.details.PropertyDetailsFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SelectPropertyTypeViewModel(private val propertyCount: Int,
                                  private val selectPropertyTypeInteractor: SelectPropertyTypeInteractor) : BaseViewModel() {

    companion object {
        private const val PROPERTY_HOME = "Дом"
        private const val PROPERTY_APPARTMENT = "Квартира"
    }

    private val selectPropertyTypeViewStateController = MutableLiveData<SelectPropertyTypeViewState>()
    val selectPropertyTypeViewStateObserver: LiveData<SelectPropertyTypeViewState> = selectPropertyTypeViewStateController

    private var propertyType = PropertyType.UNDEFINED

    private val showConfirmCloseController = MutableLiveData<Unit>()
    val showConfirmCloseObserver: LiveData<Unit> = showConfirmCloseController

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
        showConfirmCloseController.value = Unit
    }

    fun onNextClick() {
        ScreenManager.showScreenScope(PropertyDetailsFragment.newInstance(propertyCount, propertyType), ADD_PROPERTY)
    }

    fun onSelectHomeClick(){
        propertyType = PropertyType.HOUSE
        selectPropertyTypeInteractor.onSelectHomeClick()
    }

    fun onSelectAppartmentClick(){
        propertyType = PropertyType.APARTMENT
        selectPropertyTypeInteractor.onSelectAppartmentClick()
    }

}