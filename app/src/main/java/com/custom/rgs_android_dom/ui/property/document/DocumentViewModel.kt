package com.custom.rgs_android_dom.ui.property.document

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyDocumentsModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class DocumentViewModel(
    private val propertyInteractor: PropertyInteractor
) : BaseViewModel() {


    private val propertyDocumentsController = MutableLiveData<PropertyDocumentsModel>()
    val propertyDocumentsObserver: LiveData<PropertyDocumentsModel> = propertyDocumentsController

    fun initData(propertyDocumentsModel: PropertyDocumentsModel) {
        propertyDocumentsController.postValue(propertyDocumentsModel)
    }

    fun openDocument(docUrl: String) = Unit

    fun addDocument() = Unit

    fun deleteDocument() = Unit

    fun editDocument() = Unit
}
