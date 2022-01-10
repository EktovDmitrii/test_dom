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
                icon = "",
                parentCategoryId = "",
                products = listOf(
                    ProductShortModel(id = "", type = "", title = "", code = "", versionId = "", name = "", price = 0, tags = emptyList()),
                    ProductShortModel(id = "", type = "", title = "", code = "", versionId = "", name = "", price = 0, tags = emptyList()),
                    ProductShortModel(id = "", type = "", title = "", code = "", versionId = "", name = "", price = 0, tags = emptyList())
            ),
                productTags = emptyList()
            ),
            CatalogSubCategoryModel(
                id = "1",
                title = "Установка мойки",
                icon = "",
                parentCategoryId = "",
                products = listOf(
                    ProductShortModel(id = "", type = "", title = "", code = "", versionId = "", name = "", price = 0, tags = emptyList())
                ),
                productTags = emptyList()
            ),
            CatalogSubCategoryModel(
                id = "2",
                title = "Установка ванны",
                icon = "",
                parentCategoryId = "",
                products = listOf(
                    ProductShortModel(id = "", type = "", title = "", code = "", versionId = "", name = "", price = 0, tags = emptyList()),
                    ProductShortModel(id = "", type = "", title = "", code = "", versionId = "", name = "", price = 0, tags = emptyList()),
                ),
                productTags = emptyList()
            ),
            CatalogSubCategoryModel(
                id = "3",
                title = "Ремонт ванны",
                icon = "",
                parentCategoryId = "",
                products = listOf(
                    ProductShortModel(id = "", type = "", title = "", code = "", versionId = "", name = "", price = 0, tags = emptyList()),
                    ProductShortModel(id = "", type = "", title = "", code = "", versionId = "", name = "", price = 0, tags = emptyList()),
                    ProductShortModel(id = "", type = "", title = "", code = "", versionId = "", name = "", price = 0, tags = emptyList()),
                    ProductShortModel(id = "", type = "", title = "", code = "", versionId = "", name = "", price = 0, tags = emptyList()),
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