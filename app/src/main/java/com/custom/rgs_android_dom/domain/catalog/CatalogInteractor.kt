package com.custom.rgs_android_dom.domain.catalog

import android.util.Log
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.domain.catalog.models.ServiceModel
import com.custom.rgs_android_dom.domain.repositories.CatalogRepository
import io.reactivex.Single

class CatalogInteractor(private val catalogRepository: CatalogRepository) {

    fun getCatalogCategories(): Single<List<CatalogCategoryModel>> {
        return catalogRepository.getCatalogCategories().map {catalogCategories->

            var tags = mutableListOf<String>()

            for (catalogCategory in catalogCategories){
                tags.addAll(catalogCategory.productTags)

                for (subCategory in catalogCategory.subCategories){
                    tags.addAll(subCategory.productTags)
                }
            }

            tags = tags.distinct().toMutableList()
            val availableProducts = catalogRepository.getProductsAvailableForPurchase(tags).blockingGet()

            for (catalogCategory in catalogCategories){
                for (subCategory in catalogCategory.subCategories){
                    subCategory.products = availableProducts.filter { it.tags.any { it in subCategory.productTags }}
                }

                catalogCategory.products = availableProducts.filter { it.tags.any { it in catalogCategory.productTags }}
            }

           /*for (catalogCategory in catalogCategories){
                for (subCategory in catalogCategory.subCategories){
                    val availableProducts = catalogRepository.getProductsAvailableForPurchase(subCategory.productTags).blockingGet()
                    subCategory.products = availableProducts
                }
                val availableProducts = catalogRepository.getProductsAvailableForPurchase(catalogCategory.productTags).blockingGet()
                catalogCategory.products = availableProducts
            }*/
            return@map catalogCategories.filter { it.subCategories.isNotEmpty() }
        }
    }

    fun getServices(): Single<List<ServiceModel>>{
        return catalogRepository.getServices()
    }

    fun getProducts(): Single<List<ProductModel>> {
        return catalogRepository.getProducts()
    }

    fun getProduct(productId: String): Single<ProductModel>{
        return catalogRepository.getProduct(productId)
    }

    fun getProductsAvailableForPurchase(query: String?): Single<List<ProductShortModel>>{
        val tags = if (query != null) listOf(query) else null
        return catalogRepository.getProductsAvailableForPurchase(tags)
    }

}