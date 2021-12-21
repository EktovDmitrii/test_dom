package com.custom.rgs_android_dom.domain.catalog

import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.domain.catalog.models.ServiceModel
import com.custom.rgs_android_dom.domain.repositories.CatalogRepository
import io.reactivex.Single

class CatalogInteractor(private val catalogRepository: CatalogRepository) {

    fun getCatalogCategories(): Single<List<CatalogCategoryModel>> {
        return catalogRepository.getCatalogCategories().map {catalogCategories->
            for (catalogCategory in catalogCategories){
                for (subCategory in catalogCategory.subCategories){
                    val availableProducts = catalogRepository.getProductsAvailableForPurchase(subCategory.productTags).blockingGet()
                    subCategory.products = availableProducts
                }
                val availableProducts = catalogRepository.getProductsAvailableForPurchase(catalogCategory.productTags).blockingGet()
                catalogCategory.products = availableProducts
            }
            return@map catalogCategories.filter { it.subCategories.isNotEmpty() }
        }
    }

    fun getServices(): Single<List<ServiceModel>>{
        return catalogRepository.getServices()
    }

    fun getProducts(): Single<List<ProductModel>> {
        return catalogRepository.getProducts()
    }

}