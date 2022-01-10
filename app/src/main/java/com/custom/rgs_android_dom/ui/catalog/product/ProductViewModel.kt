package com.custom.rgs_android_dom.ui.catalog.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.models.ProductDurationModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductUnitType
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager

class ProductViewModel(
    private val productDetail: ProductModel
) : BaseViewModel() {

    private val productController = MutableLiveData<ProductModel>()
    val productObserver: LiveData<ProductModel> = productController

    init {
        productController.value = productDetail
    }

    fun onBackClick(){
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
            duration = ProductDurationModel(
                units = 12,
                unitType = ProductUnitType.DAYS
            ),
            iconLink = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg",
            id = "1",
            insuranceProducts = null,
            internalDescription = null,
            name = "",
            objectRequired = null,
            price = null,
            status = null,
            tags = emptyList(),
            title = "Поиск утечек тепла, сквозняков.",
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
        val singleProductFragment = SingleProductFragment.newInstance(product)
        ScreenManager.showBottomScreen(singleProductFragment)
    }
}