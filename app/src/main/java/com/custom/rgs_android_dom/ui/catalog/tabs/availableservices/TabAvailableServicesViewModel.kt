package com.custom.rgs_android_dom.ui.catalog.tabs.availableservices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.subcategory.CatalogSubcategoryFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager

class TabAvailableServicesViewModel : BaseViewModel() {

    private val servicesController = MutableLiveData<List<CatalogSubCategoryModel>>()
    val servicesObserver: LiveData<List<CatalogSubCategoryModel>> = servicesController

    init {
        servicesController.value = listOf(
            CatalogSubCategoryModel(
                id = "0",
                title = "Установка раковины",
                name = "Установка раковины",
                logoSmall = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg",
                logoMiddle = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg",
                logoLarge = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg",
                parentCategoryId = "",
                products = listOf(
                    ProductShortModel(id = "", type = "", title = "Установка раковины", code = "", versionId = "", name = "", price = 150, tags = emptyList(), icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg"),
                    ProductShortModel(id = "", type = "", title = "Установка раковины", code = "", versionId = "", name = "", price = 150, tags = emptyList(), icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg"),
                    ProductShortModel(id = "", type = "", title = "Установка раковины", code = "", versionId = "", name = "", price = 150, tags = emptyList(), icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg")
            ),
                productTags = emptyList()
            ),
            CatalogSubCategoryModel(
                id = "1",
                title = "Установка мойки",
                name = "Установка мойки",
                logoSmall = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg",
                logoMiddle = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg",
                logoLarge = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg",
                parentCategoryId = "",
                products = listOf(
                    ProductShortModel(id = "", type = "", title = "Установка мойки", code = "", versionId = "", name = "", price = 150, tags = emptyList(), icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg")
                ),
                productTags = emptyList()
            ),
            CatalogSubCategoryModel(
                id = "2",
                title = "Установка ванны",
                name = "Установка ванны",
                logoSmall = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg",
                logoMiddle = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg",
                logoLarge = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg",
                parentCategoryId = "",
                products = listOf(
                    ProductShortModel(id = "", type = "", title = "Установка ванны", code = "", versionId = "", name = "", price = 150, tags = emptyList(), icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg"),
                    ProductShortModel(id = "", type = "", title = "Установка ванны", code = "", versionId = "", name = "", price = 150, tags = emptyList(), icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg"),
                ),
                productTags = emptyList()
            ),
            CatalogSubCategoryModel(
                id = "3",
                title = "Ремонт ванны",
                name = "Ремонт ванны",
                logoSmall = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg",
                logoMiddle = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg",
                logoLarge = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg",
                parentCategoryId = "",
                products = listOf(
                    ProductShortModel(id = "", type = "", title = "Ремонт ванны", code = "", versionId = "", name = "", price = 150, tags = emptyList(), icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg"),
                    ProductShortModel(id = "", type = "", title = "Ремонт ванны", code = "", versionId = "", name = "", price = 150, tags = emptyList(), icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg"),
                    ProductShortModel(id = "", type = "", title = "Ремонт ванны", code = "", versionId = "", name = "", price = 150, tags = emptyList(), icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg"),
                    ProductShortModel(id = "", type = "", title = "Ремонт ванны", code = "", versionId = "", name = "", price = 150, tags = emptyList(), icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg"),
                ),
                productTags = emptyList()
            )
        )
    }

    fun onServiceClick(catalogSubCategoryModel: CatalogSubCategoryModel) {
        val catalogSubcategoryFragment = CatalogSubcategoryFragment.newInstance(catalogSubCategoryModel)
        ScreenManager.showBottomScreen(catalogSubcategoryFragment)
    }
}