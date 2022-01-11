package com.custom.rgs_android_dom.ui.property.info

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.property.document.DocumentFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PropertyInfoViewModel(
    private val objectId: String,
    private val propertyInteractor: PropertyInteractor
) : BaseViewModel() {

    private val propertyItemController = MutableLiveData<PropertyItemModel>()
    val propertyItemObserver: LiveData<PropertyItemModel> = propertyItemController

    init {
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
            .subscribeBy(
                onNext = { uri ->
                    updateDocuments(uri)
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)
    }

    private fun updateDocuments(uri: List<Uri>) {
        propertyItemController.value?.let {
            propertyInteractor.updatePropertyItem(
                objectId = objectId,
                propertyItemModel = it,
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

    fun onShowAllDocumentsClick() {
        ScreenManager.showScreen(
            DocumentFragment.newInstance(
                objectId,
                propertyItemObserver.value!!
            )
        )
    }

}
