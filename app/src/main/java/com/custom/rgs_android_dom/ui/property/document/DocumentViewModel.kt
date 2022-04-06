package com.custom.rgs_android_dom.ui.property.document

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
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
import java.io.File


class DocumentViewModel(
    private val objectId: String,
    private val propertyInteractor: PropertyInteractor,
    private val context: Context
) : BaseViewModel() {

    private val isDeleteButtonVisibleController = MutableLiveData<Boolean>(false)
    val isDeleteButtonVisibleObserver: LiveData<Boolean> = isDeleteButtonVisibleController

    private val propertyItemController = MutableLiveData<PropertyItemModel>()
    val propertyDocumentsObserver: LiveData<PropertyItemModel> = propertyItemController

    private val downloadFileController = MutableLiveData<PropertyDocument>()
    val downloadFileObserver: LiveData<PropertyDocument> = downloadFileController

    private val requestReadExternalStoragePermController = MutableLiveData<Unit>()
    val requestReadExternalStoragePermObserver: LiveData<Unit> = requestReadExternalStoragePermController

    private var selectedPropertyDocument: PropertyDocument? = null

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
                onNext = { documentList ->
                    propertyItemController.value = documentList
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)

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

    fun onFileClick(propertyDocument: PropertyDocument) {
        changeDeleteButtonsVisibility(false)
        val documentType = propertyDocument.link.substringAfterLast(".", "missing")
        var documentIndex = 0
        propertyItemController.value?.documents?.forEachIndexed { index, propertyDoc ->
            if (propertyDoc == propertyDocument)
                documentIndex = index
        }
        when (documentType) {
            "jpg", "jpeg", "png", "bmp" -> showDetailDocumentScreen(documentIndex)
            else -> {
                selectedPropertyDocument = propertyDocument

                requestReadExternalStoragePermController.value = Unit

            }
        }
    }

    fun onReadExternalStoragePermGranted(){
        selectedPropertyDocument?.let { selectedPropertyDocument->
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .absoluteFile.path + File.separator + selectedPropertyDocument.name
            )
            if (file.exists()) {
                val contentUri = FileProvider.getUriForFile(context, context.packageName, file)

                val mime = context.contentResolver.getType(contentUri)

                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.setDataAndType(contentUri, mime)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                ScreenManager.launchIntent(intent)
            } else {
                downloadFileController.value = selectedPropertyDocument
            }
        }

    }

    // TODO Add some modal screen with permission rationale
    fun onShouldRequestReadExternalStoragePermRationale(){

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

    fun changeDeleteButtonsVisibility(isDeleteButtonVisible: Boolean){
        isDeleteButtonVisibleController.value = isDeleteButtonVisible
    }

    fun deleteDocument(propertyDocument: PropertyDocument) {
        changeDeleteButtonsVisibility(true)
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
