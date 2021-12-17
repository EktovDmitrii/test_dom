package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.catalog.models.CatalogNodeModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.domain.catalog.models.ServiceModel
import io.reactivex.Single

interface CatalogRepository {

    fun getCatalogNodes(): Single<List<CatalogNodeModel>>

    fun getServices(): Single<List<ServiceModel>>

    fun getProducts(): Single<List<ProductModel>>

}