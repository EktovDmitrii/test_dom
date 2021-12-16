package com.custom.rgs_android_dom.domain.catalog

import com.custom.rgs_android_dom.domain.catalog.models.CatalogNodeModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.domain.catalog.models.ServiceModel
import com.custom.rgs_android_dom.domain.repositories.CatalogRepository
import io.reactivex.Single

class CatalogInteractor(private val catalogRepository: CatalogRepository) {

    fun getCatalogNodes(): Single<List<CatalogNodeModel>> {
        return catalogRepository.getCatalogNodes()
    }

    fun getServices(): Single<List<ServiceModel>>{
        return catalogRepository.getServices()
    }

    fun getProducts(): Single<List<ProductModel>> {
        return catalogRepository.getProducts()
    }

}