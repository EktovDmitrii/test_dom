package com.custom.rgs_android_dom.ui.property.add.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.data.network.toNetworkException
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
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
                               private val propertyInteractor: PropertyInteractor) : BaseViewModel() {

    private val propertyDetailsViewStateController = MutableLiveData<PropertyDetailsViewState>()
    val propertyDetailsObserver: LiveData<PropertyDetailsViewState> = propertyDetailsViewStateController

    private val validateExceptionController = MutableLiveData<ValidatePropertyException>()
    val validateExceptionObserver: LiveData<ValidatePropertyException> = validateExceptionController

    private val showConfirmCloseController = MutableLiveData<Unit>()
    val showConfirmCloseObserver: LiveData<Unit> = showConfirmCloseController

    init {
        propertyInteractor.propertyDetailsViewStateSubject
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

        propertyInteractor.updatePropertyName(propertyCount, propertyType)
        propertyInteractor.updatePropertyType(propertyType)
    }

    fun onBackClick() {
        showConfirmCloseController.value = Unit
    }

    fun onAddClick(){
        propertyInteractor.addProperty()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onComplete = {
                    loadingStateController.value = LoadingState.CONTENT
                    notificationController.value = "Объект добавлен"
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
        propertyInteractor.updatePropertyAddress(address)
    }

    fun onEntranceChanged(entrance: String){
        propertyInteractor.updatePropertyEntrance(entrance)
    }

    fun onCorpusChanged(corpus: String){
        propertyInteractor.updatePropertyCorpus(corpus)
    }

    fun onFloorChanged(floor: String){
        propertyInteractor.updatePropertyFloor(floor)
    }

    fun onFlatChanged(flat: String){
        propertyInteractor.updatePropertyFlat(flat)
    }

    fun onIsOwnSelected(selection: MSDYesNoSelector.Selection){
        propertyInteractor.updatePropertyIsOwn(selection.selectionString)
    }

    fun onIsInRentSelected(selection: MSDYesNoSelector.Selection){
        propertyInteractor.updatePropertyIsRent(selection.selectionString)
    }

    fun onIsTemporarySelected(selection: MSDYesNoSelector.Selection){
        propertyInteractor.updatePropertyIsTemporary(selection.selectionString)
    }

    fun onTotalAreaChanged(totalArea: String){
        propertyInteractor.updatePropertyTotalArea(totalArea)
    }

    fun onCommentChanged(comment: String){
        propertyInteractor.updatePropertyComment(comment)
    }

}