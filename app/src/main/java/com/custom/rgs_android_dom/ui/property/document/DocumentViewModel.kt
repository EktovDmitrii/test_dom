package com.custom.rgs_android_dom.ui.property.document

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.property.document.detail_document.DetailDocumentFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class DocumentViewModel(
    private val objectId: String,
    propertyItemModel: PropertyItemModel,
    private val propertyInteractor: PropertyInteractor
) : BaseViewModel() {

    var isDeleteButtonVisible = false

    private val propertyItemController = MutableLiveData<PropertyItemModel>()
    val propertyDocumentsObserver: LiveData<PropertyItemModel> = propertyItemController

    private val downloadFileController = MutableLiveData<PropertyDocument>()
    val downloadFileObserver: LiveData<PropertyDocument> = downloadFileController

    init {
        propertyItemController.postValue(propertyItemModel)

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

        propertyInteractor.propertyDocumentDeletedSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { documentList ->
                    propertyItemController.value = documentList
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

    fun onFileClick(propertyDocument: PropertyDocument) {
        isDeleteButtonVisible = false
        val documentType = propertyDocument.link.substringAfterLast(".", "missing")
        var documentIndex = 0
        propertyItemController.value?.documents?.forEachIndexed { index, propertyDoc ->
            if (propertyDoc == propertyDocument)
                documentIndex = index
        }
        when (documentType) {
            "jpg" -> showDetailDocumentScreen(documentIndex)
            "jpeg" -> showDetailDocumentScreen(documentIndex)
            "png" -> showDetailDocumentScreen(documentIndex)
            "bmp" -> showDetailDocumentScreen(documentIndex)
            else -> {
                downloadFileController.value = propertyDocument
            }
        }
    }

    private fun showDetailDocumentScreen(documentIndex: Int) {
        ScreenManager.showScreen(
            DetailDocumentFragment.newInstance(
                objectId,
                documentIndex,
                propertyItemController.value
            )
        )
    }

    fun deleteDocument(propertyDocument: PropertyDocument) {
        isDeleteButtonVisible = true
        val documentsList = propertyItemController.value?.documents
        documentsList?.remove(propertyDocument)
        val newPropertyItemModel = documentsList?.let {
            propertyItemController.value?.copy(
                documents = it
            )
        }

        if (newPropertyItemModel != null) {
            propertyInteractor.onFilesToDeleteSelected(newPropertyItemModel)
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
