package com.custom.rgs_android_dom.ui.property.add.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.data.network.toNetworkException
import com.custom.rgs_android_dom.domain.client.exceptions.ValidateClientException
import com.custom.rgs_android_dom.domain.property.details.PropertyDetailsInteractor
import com.custom.rgs_android_dom.domain.property.details.exceptions.ValidatePropertyException
import com.custom.rgs_android_dom.domain.property.details.view_states.PropertyDetailsViewState
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ADD_PROPERTY
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import com.custom.rgs_android_dom.views.MSDYesNoSelector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PropertyDetailsViewModel(propertyCount: Int,
                               propertyType: PropertyType,
                               private val propertyDetailsInteractor: PropertyDetailsInteractor) : BaseViewModel() {

    private val propertyDetailsViewStateController = MutableLiveData<PropertyDetailsViewState>()
    val propertyDetailsObserver: LiveData<PropertyDetailsViewState> = propertyDetailsViewStateController

    private val validateExceptionController = MutableLiveData<ValidatePropertyException>()
    val validateExceptionObserver: LiveData<ValidatePropertyException> = validateExceptionController

    init {
        propertyDetailsInteractor.propertyDetailsViewStateSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    propertyDetailsViewStateController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        propertyDetailsInteractor.updatePropertyName(propertyCount, propertyType)
        propertyDetailsInteractor.updatePropertyType(propertyType)
    }

    fun onBackClick() {
        //closeController.value = Unit
        ScreenManager.closeScope(ADD_PROPERTY)
    }

    fun onAddClick(){
        propertyDetailsInteractor.addProperty()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onComplete = {
                    loadingStateController.value = LoadingState.CONTENT
                    //closeController.value = Unit
                    ScreenManager.closeScope(ADD_PROPERTY)
                },
                onError = {
                    when (it){
                        is ValidatePropertyException -> {
                            loadingStateController.value = LoadingState.CONTENT
                            validateExceptionController.value = it
                        }
                        else -> {
                            loadingStateController.value = LoadingState.ERROR
                            networkErrorController.value = it.toNetworkException()?.message ?: "Ошибка добавления объекта"
                        }
                    }

                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onAddressChanged(address: String){
        propertyDetailsInteractor.updatePropertyAddress(address)
    }

    fun onEntranceChanged(entrance: String){
        propertyDetailsInteractor.updatePropertyEntrance(entrance)
    }

    fun onCorpusChanged(corpus: String){
        propertyDetailsInteractor.updatePropertyCorpus(corpus)
    }

    fun onFloorChanged(floor: String){
        propertyDetailsInteractor.updatePropertyFloor(floor)
    }

    fun onFlatChanged(flat: String){
        propertyDetailsInteractor.updatePropertyFlat(flat)
    }

    fun onIsOwnSelected(selection: MSDYesNoSelector.Selection){
        propertyDetailsInteractor.updatePropertyIsOwn(selection.selectionString)
    }

    fun onIsInRentSelected(selection: MSDYesNoSelector.Selection){
        propertyDetailsInteractor.updatePropertyIsRent(selection.selectionString)
    }

    fun onIsTemporarySelected(selection: MSDYesNoSelector.Selection){
        propertyDetailsInteractor.updatePropertyIsTemporary(selection.selectionString)
    }

    fun onTotalAreaChanged(totalArea: String){
        propertyDetailsInteractor.updatePropertyTotalArea(totalArea)
    }

    fun onCommentChanged(comment: String){
        propertyDetailsInteractor.updatePropertyComment(comment)
    }

}