package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.catalog.models.*
import com.custom.rgs_android_dom.domain.catalog.models.ClientProductModel
import io.reactivex.Single

interface CatalogRepository {

    fun getCatalogCategories(): Single<List<CatalogCategoryModel>>

    fun getServices(): Single<List<ServiceModel>>

    fun getProducts(): Single<List<ProductModel>>

    fun getShowcase(tags: List<String>?): Single<List<ProductShortModel>>

    fun findProducts(query: String): Single<List<ProductShortModel>>

    fun getProduct(productId: String, productVersionId: String?): Single<ProductModel>

    fun getProductServices(productId: String, productVersionId: String?): Single<List<ServiceShortModel>>

    fun getProductServiceDetails(productId: String, serviceId: String, serviceVersionId: String?): Single<ServiceModel>

    fun getAvailableServices(balanceGroupByType: String?): Single<List<AvailableServiceModel>>

    fun getClientProducts(contractIds: String?): Single<List<ClientProductModel>>

    fun getAvailableServiceInProduct(productId: String, clientProductId: String?, serviceId: String): Single<AvailableServiceModel>

}