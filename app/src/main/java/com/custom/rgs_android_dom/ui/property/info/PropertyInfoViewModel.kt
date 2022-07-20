package com.custom.rgs_android_dom.ui.property.info

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.ui.managers.MSDConnectivityManager
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.property.document.DocumentListFragment
import com.custom.rgs_android_dom.ui.property.document.detail_document.DetailDocumentFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit

class PropertyInfoViewModel(
    private val objectId: String,
    private val propertyInteractor: PropertyInteractor,
    private val clientInteractor: ClientInteractor,
    private val connectivityManager: MSDConnectivityManager,
    private val context: Context
) : BaseViewModel() {

    private val propertyItemController = MutableLiveData<PropertyItemModel>()
    val propertyItemObserver: LiveData<PropertyItemModel> = propertyItemController

    private val internetConnectionController = MutableLiveData<Boolean>()
    val internetConnectionObserver: LiveData<Boolean> = internetConnectionController

    private val propertyMoreController = MutableLiveData<PropertyItemModel>()
    val propertyMoreObserver: LiveData<PropertyItemModel> = propertyMoreController

    private val requestReadExternalStoragePermController = MutableLiveData<Unit>()
    val requestReadExternalStoragePermObserver: LiveData<Unit> = requestReadExternalStoragePermController

    private val downloadFileController = MutableLiveData<PropertyDocument>()
    val downloadFileObserver: LiveData<PropertyDocument> = downloadFileController

    private var selectedPropertyDocument: PropertyDocument? = null

    init {
        internetConnectionController.value = connectivityManager.isInternetConnected()

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

    fun onShowAllDocumentsClick() {
        ScreenManager.showScreen(
            DocumentListFragment.newInstance(objectId)
        )
    }

    fun onMoreClick(){
        propertyMoreController.value = propertyItemController.value
    }

    fun onDocumentClick(propertyDocument: PropertyDocument) {
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

    private fun showDetailDocumentScreen(documentIndex: Int) {
        ScreenManager.showScreen(
            DetailDocumentFragment.newInstance(
                objectId,
                documentIndex,
                propertyItemController.value
            )
        )
    }

}
