package com.custom.rgs_android_dom.ui.property.add.details

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.details.exceptions.PropertyDocumentValidationException
import com.custom.rgs_android_dom.domain.property.details.exceptions.PropertyDocumentValidationException.*
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

@SuppressLint("MissingPermission")
class PropertyDetailsViewModel(
    propertyName: String,
    propertyAddress: AddressItemModel,
    propertyType: PropertyType,
    private val propertyInteractor: PropertyInteractor,
    private val context: Context
) : BaseViewModel() {

    private val propertyDetailsViewStateController = MutableLiveData<PropertyDetailsViewState>()
    val propertyDetailsObserver: LiveData<PropertyDetailsViewState> = propertyDetailsViewStateController

    private val internetConnectionController = MutableLiveData<Boolean>()
    val internetConnectionObserver: LiveData<Boolean> = internetConnectionController

    init {

        val connectivityManager =  this.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                onInternetConnectionChanged(true)
            }

            override fun onLost(network: Network) {
                onInternetConnectionChanged(false)
            }

        })

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

        propertyDetailsViewStateController.value = propertyInteractor.initPropertyDetails(propertyName, propertyType, propertyAddress)

        propertyInteractor.propertyDocumentUploadedSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                         propertyInteractor.updateDocuments(it)
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)
    }


    fun onBackClick() {
        close()
    }

    fun onAddClick() {
        if (internetConnectionController.value == true){
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
                        when (it) {
                            is ValidatePropertyException -> {
                                loadingStateController.value = LoadingState.CONTENT
                            }
                            is PropertyDocumentValidationException -> {
                                loadingStateController.value = LoadingState.CONTENT
                                when(it){
                                    is UnsupportedFileType -> { notificationController.value = "\"Файл не может быть в формате .${it.extension}\"" }
                                    FileSizeExceeded -> { notificationController.value = "Размер файла больше 10 mb" }
                                    TotalFilesSizeExceeded -> { notificationController.value = "Место для документов заполнено" }
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
        } else {
            loadingStateController.value = LoadingState.CONTENT
        }

    }

    fun onAddressChanged(address: String) {
        propertyInteractor.updatePropertyAddress(address)
    }

    fun onEntranceChanged(entrance: String) {
        propertyInteractor.updatePropertyEntrance(entrance)
    }

    fun onCorpusChanged(corpus: String) {
        propertyInteractor.updatePropertyCorpus(corpus)
    }

    fun onFloorChanged(floor: String) {
        propertyInteractor.updatePropertyFloor(floor)
    }

    fun onFlatChanged(flat: String) {
        propertyInteractor.updatePropertyFlat(flat)
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

    fun onRemoveDocumentClick(uri: Uri) {
        propertyInteractor.onRemoveDocument(uri)
    }

    fun onInternetConnectionChanged(isAvailable: Boolean) {
        internetConnectionController.postValue(isAvailable)
    }

}