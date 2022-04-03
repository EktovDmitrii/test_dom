package com.custom.rgs_android_dom.ui.property.add.details.files

import android.content.Context
import android.net.Uri
import android.os.FileUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.length
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File

class PropertyUploadDocumentsViewModel(
    private val context: Context,
    private val propertyInteractor: PropertyInteractor) : BaseViewModel() {

    companion object {
        private const val MAX_FILE_SIZE = 10485760
    }

    private val errorMessageController = MutableLiveData<String>()
    val errorMessageObserver: LiveData<String> = errorMessageController

    fun onDocumentsSelected(files: List<Uri>) {
        if (files.find { it.length(context.contentResolver) >= MAX_FILE_SIZE } != null){
            errorMessageController.value = TranslationInteractor.getTranslation("app.object.main.actions.add.err_file_limit")
            closeController.value = Unit
        } else {
            propertyInteractor.onFilesToUploadSelected(files)
            closeController.value = Unit
        }

    }

}