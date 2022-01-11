package com.custom.rgs_android_dom.ui.catalog.product

import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager

class MyProductViewModel : BaseViewModel() {

    fun onBackClick() {
        closeController.value = Unit
    }

    fun onServiceClick() {
        val product = ProductModel(
            code = "",
            activatedAt = null,
            archivedAt = null,
            coolOff = null,
            createdAt = null,
            defaultProduct = false,
            deliveryTime = null,
            description = "",
            descriptionFormat = "",
            descriptionRef = null,
            duration = null,
            iconLink = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg",
            id = "1",
            insuranceProducts = null,
            internalDescription = null,
            name = "",
            objectRequired = null,
            price = null,
            status = null,
            tags = emptyList(),
            title = "",
            type = "",
            validityFrom = null,
            validityTo = null,
            versionActivatedAt = null,
            versionArchivedAt = null,
            versionCode = null,
            versionCreatedAt = null,
            versionId = null,
            versionStatus = null
        )
        val serviceFragment = ServiceFragment.newInstance(product)
        ScreenManager.showBottomScreen(serviceFragment)
    }

}