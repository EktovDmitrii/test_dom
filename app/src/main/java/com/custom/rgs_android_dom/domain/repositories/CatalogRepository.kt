package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.domain.catalog.models.ServiceModel
import io.reactivex.Single

interface CatalogRepository {

    fun getCatalogCategories(): Single<List<CatalogCategoryModel>>

    fun getServices(): Single<List<ServiceModel>>

    fun getProducts(): Single<List<ProductModel>>

    fun getProductsAvailableForPurchase(tags: List<String>?): Single<List<ProductShortModel>>

    fun getProductsAvailableForPurchaseWithoutAuth(): Single<List<ProductShortModel>>

    fun getProduct(productId: String): Single<ProductModel>
}