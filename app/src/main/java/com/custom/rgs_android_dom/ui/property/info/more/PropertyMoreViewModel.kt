package com.custom.rgs_android_dom.ui.property.info.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class PropertyMoreViewModel(private val property: PropertyItemModel) : BaseViewModel() {

    private val editPropertyController = MutableLiveData<PropertyItemModel>()
    val editPropertyObserver: LiveData<PropertyItemModel> = editPropertyController

    private val deletePropertyController = MutableLiveData<PropertyItemModel>()
    val deletePropertyObserver: LiveData<PropertyItemModel> = deletePropertyController

    fun onEditPropertyClick(){
        editPropertyController.value = property
    }

    fun onDeletePropertyClick(){
        deletePropertyController.value = property
    }

}