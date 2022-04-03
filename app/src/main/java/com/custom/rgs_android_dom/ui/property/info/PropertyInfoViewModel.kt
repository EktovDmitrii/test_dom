package com.custom.rgs_android_dom.ui.property.info

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.models.OrderStatus
import com.custom.rgs_android_dom.ui.managers.MSDConnectivityManager
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.navigation.UPDATE_PROPERTY
import com.custom.rgs_android_dom.ui.property.document.DocumentListFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class PropertyInfoViewModel(
    private val objectId: String,
    private val propertyInteractor: PropertyInteractor,
    private val clientInteractor: ClientInteractor,
    private val connectivityManager: MSDConnectivityManager
) : BaseViewModel() {

    private val propertyItemController = MutableLiveData<PropertyItemModel>()
    val propertyItemObserver: LiveData<PropertyItemModel> = propertyItemController

    private val internetConnectionController = MutableLiveData<Boolean>()
    val internetConnectionObserver: LiveData<Boolean> = internetConnectionController

    private val propertyMoreController = MutableLiveData<PropertyItemModel>()
    val propertyMoreObserver: LiveData<PropertyItemModel> = propertyMoreController

    private var property: PropertyItemModel? = null

    init {
        getPropertyItem()

        propertyInteractor.propertyInfoStateSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    propertyItemController.value = it
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)

        propertyInteractor.propertyDocumentUploadedSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .delay(500, TimeUnit.MILLISECONDS)
            .subscribeBy(
                onNext = { uri ->
                    propertyItemController.value?.let {
                        updateDocuments(it, uri)
                    }
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)

        propertyInteractor.propertyDocumentDeletedSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    propertyItemController.value = it
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)

        connectivityManager.connectivitySubject
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                internetConnectionController.value = it
            }
            .addTo(dataCompositeDisposable)

        propertyInteractor.getPropertyAddedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    getPropertyItem()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        propertyInteractor.getPropertyDeletedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    close()
                }
            ).addTo(dataCompositeDisposable)

        propertyInteractor.closePropertyPageSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { close() },
                onError = { logException(this, it) }
            )
            .addTo(dataCompositeDisposable)
    }

    private fun getPropertyItem() {
        propertyInteractor.getPropertyItem(objectId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    propertyInteractor.propertyInfoStateSubject.onNext(it)
                },
                onError = {
                    logException(this, it)
                    networkErrorController.value = "Не удалось загрузить объект"
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onShowAllDocumentsClick() {
        ScreenManager.showScreen(
            DocumentListFragment.newInstance(objectId)
        )
    }

    fun onMoreClick(){
        propertyMoreController.value = propertyItemController.value
    }

    private fun updateDocuments(currentModel: PropertyItemModel, uri: List<Uri>) {
        propertyInteractor.updatePropertyDocuments(
            objectId = objectId,
            propertyItemModel = currentModel,
            filesUri = uri,
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    propertyInteractor.propertyInfoStateSubject.onNext(it)
                },
                onError = {
                    logException(this, it)
                    networkErrorController.value = "Не удалось загрузить объект"
                }
            ).addTo(dataCompositeDisposable)
    }

}
