package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.catalog.models.*
import io.reactivex.Single

interface CatalogRepository {

    fun getCatalogCategories(): Single<List<CatalogCategoryModel>>

    fun getServices(): Single<List<ServiceModel>>

    fun getProducts(): Single<List<ProductModel>>

    fun getShowcase(tags: List<String>?): Single<List<ProductShortModel>>

    fun getProduct(productId: String): Single<ProductModel>

    fun getProductServices(productId: String): Single<List<ServiceShortModel>>

    fun getPopularProducts(tags: List<String>): Single<List<ProductShortModel>>

}