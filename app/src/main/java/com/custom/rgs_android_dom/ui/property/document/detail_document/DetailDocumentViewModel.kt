package com.custom.rgs_android_dom.ui.property.document.detail_document

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class DetailDocumentViewModel(
    private val objectId: String,
    private val documentIndex: Int,
    propertyItemModel: PropertyItemModel,
    private val propertyInteractor: PropertyInteractor
) : BaseViewModel() {

    private var currentPropertyItemModel: PropertyItemModel? = null

    private val propertyDocumentController = MutableLiveData<PropertyDocument>()
    val propertyDocumentObserver: LiveData<PropertyDocument> = propertyDocumentController

    init {
        propertyDocumentController.postValue(propertyItemModel.documents[documentIndex])
        currentPropertyItemModel = propertyItemModel
    }

    fun renameDocument(documentName: String) {

        val previousDocument = propertyDocumentController.value
        previousDocument?.name = documentName

        previousDocument?.let { propertyDocumentController.postValue(it) }

        val documentsList = currentPropertyItemModel?.documents
        documentsList?.forEachIndexed { index, propertyDocument ->
            if (index == documentIndex) {
                propertyDocument.name = documentName
            }
        }
        val newPropertyItemModel = documentsList?.let {
            currentPropertyItemModel?.copy(
                documents = it
            )
        }
        newPropertyItemModel?.let {
            propertyInteractor.updateDocument(
                objectId = objectId,
                propertyItemModel = it,
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

    fun deleteDocument() {
        val documentsList = currentPropertyItemModel?.documents
        documentsList?.removeAt(documentIndex)
        val newPropertyItemModel = documentsList?.let {
            currentPropertyItemModel?.copy(
                documents = it
            )
        }

        if (newPropertyItemModel != null) {
            propertyInteractor.updateDocument(objectId, newPropertyItemModel)
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
    }
}
