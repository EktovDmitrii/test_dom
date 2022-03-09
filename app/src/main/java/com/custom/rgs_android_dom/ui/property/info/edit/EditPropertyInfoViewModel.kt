package com.custom.rgs_android_dom.ui.property.info.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.address.AddressInteractor
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.details.exceptions.PropertyDocumentValidationException
import com.custom.rgs_android_dom.domain.property.details.exceptions.ValidatePropertyException
import com.custom.rgs_android_dom.domain.property.details.view_states.PropertyDetailsViewState
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.navigation.UPDATE_PROPERTY
import com.custom.rgs_android_dom.utils.logException
import com.custom.rgs_android_dom.views.MSDYesNoSelector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class EditPropertyInfoViewModel(
    private val objectId: String,
    private val isEditable: Boolean,
    private val propertyInteractor: PropertyInteractor,
    private val addressInteractor: AddressInteractor
    ) : BaseViewModel() {

    private val objectIdController = MutableLiveData(objectId)
    val objectIdObserver: LiveData<String> = objectIdController

    private val propertyDetailsViewStateController = MutableLiveData<PropertyDetailsViewState>()
    val propertyDetailsObserver: LiveData<PropertyDetailsViewState> = propertyDetailsViewStateController

    private val selectAddressItemModelController = MutableLiveData<AddressItemModel>()
    val selectAddressItemModelObserver: LiveData<AddressItemModel> = selectAddressItemModelController

    private val isEnabledSaveButtonController = MutableLiveData(true)
    val isEnabledSaveButtonObserver: LiveData<Boolean> = isEnabledSaveButtonController

    private val isExistAvatarController = MutableLiveData(false)
    val isExistAvatarObserver: LiveData<Boolean> = isEnabledSaveButtonController

    private val uploadedLocallyAvatarController = MutableLiveData<String>()
    val uploadedLocallyAvatarObserver: LiveData<String> = uploadedLocallyAvatarController

    private val editPropertyRequestedController = MutableLiveData<Boolean>()
    val editPropertyRequestedObserver: LiveData<Boolean> = editPropertyRequestedController

    private val isPropertyEditableController = MutableLiveData(isEditable)
    val isPropertyEditableObserver: LiveData<Boolean> = isPropertyEditableController

    init {
        propertyInteractor.getPropertyItem(objectId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    val propertyItemModel = propertyInteractor.initPropertyDetailsForUpdate(it)
                    propertyDetailsViewStateController.value = propertyItemModel
                    isExistAvatarController.value = !propertyItemModel.photoLink.isNullOrEmpty()
                },
                onError = {
                    logException(this, it)
                    networkErrorController.value = "Не удалось загрузить объект"
                }
            ).addTo(dataCompositeDisposable)

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
            )
            .addTo(dataCompositeDisposable)

        addressInteractor.getAddressSelectedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    selectAddressItemModelController.value = it
                    propertyInteractor.updatePropertyAddress(it)
                },
                onError = {
                    propertyInteractor.onPropertyAddressChanged(AddressItemModel.createEmpty())
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        propertyInteractor.propertyAvatarUrlChangedSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    propertyInteractor.updateAvatar(it)
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)

        propertyInteractor.propertyAvatarUrlRemovedSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    propertyInteractor.updateAvatar(null)
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)

        propertyInteractor.propertyAvatarSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    uploadedLocallyAvatarController.value = it
                    isExistAvatarController.value = !it.isNullOrEmpty()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        propertyInteractor.getModifications(objectId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    editPropertyRequestedController.value = it.isNotEmpty()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        propertyInteractor.getEditPropertyRequestedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    editPropertyRequestedController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onPropertyNameChanged(name: String){
        propertyInteractor.updatePropertyName(name)
    }

    fun onPropertyTypeChanged(type: String){
        propertyInteractor.updatePropertyType(type)
        if (type == PropertyType.HOUSE.type) {
            propertyInteractor.updatePropertyEntrance("")
            propertyInteractor.updatePropertyFloor("")
        }
    }

    fun onEntranceChanged(entrance: String) {
        propertyInteractor.updatePropertyEntrance(entrance)
    }

    fun onFloorChanged(floor: String) {
        propertyInteractor.updatePropertyFloor(floor)
    }

    fun onIsOwnSelected(selection: MSDYesNoSelector.Selection) {
        propertyInteractor.updatePropertyIsOwn(selection.selectionString)
    }

    fun onIsInRentSelected(selection: MSDYesNoSelector.Selection) {
        propertyInteractor.updatePropertyIsRent(selection.selectionString)
    }

    fun onIsTemporarySelected(selection: MSDYesNoSelector.Selection) {
        propertyInteractor.updatePropertyIsTemporary(selection.selectionString)
    }

    fun onTotalAreaChanged(totalArea: String) {
        propertyInteractor.updatePropertyTotalArea(totalArea)
    }

    fun onCommentChanged(comment: String) {
        propertyInteractor.updatePropertyComment(comment)
    }

    fun onSaveClicked() {
        propertyInteractor.updateProperty(objectId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onComplete = {
                    notificationController.value = "Недвижимость изменена"
                    loadingStateController.value = LoadingState.CONTENT
                    close()
                },
                onError = {
                    when (it) {
                        is ValidatePropertyException -> {
                            loadingStateController.value = LoadingState.CONTENT
                        }
                        is PropertyDocumentValidationException -> {
                            loadingStateController.value = LoadingState.CONTENT
                            when(it){
                                is PropertyDocumentValidationException.UnsupportedFileType -> { notificationController.value = "\"Файл не может быть в формате .${it.extension}\"" }
                                PropertyDocumentValidationException.FileSizeExceeded -> { notificationController.value = "Размер файла больше 10 mb" }
                                PropertyDocumentValidationException.TotalFilesSizeExceeded -> { notificationController.value = "Место для документов заполнено" }
                            }
                        }
                        else -> {
                            loadingStateController.value = LoadingState.ERROR
                            handleNetworkException(it)
                        }
                    }
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onBackClick() {
        close()
    }

}