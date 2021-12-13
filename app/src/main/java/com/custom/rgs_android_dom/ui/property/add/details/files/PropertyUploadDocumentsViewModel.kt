package com.custom.rgs_android_dom.ui.property.add.details.files

import android.net.Uri
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File

class PropertyUploadDocumentsViewModel(private val propertyInteractor: PropertyInteractor) :
    BaseViewModel() {

    fun onDocumentsSelected(files: List<Uri>) {
        propertyInteractor.onFilesToUploadSelected(files)
        closeController.value = Unit
    }

}