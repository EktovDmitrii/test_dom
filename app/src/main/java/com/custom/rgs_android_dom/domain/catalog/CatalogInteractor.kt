package com.custom.rgs_android_dom.domain.catalog

import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.domain.catalog.models.ServiceModel
import com.custom.rgs_android_dom.domain.repositories.CatalogRepository
import io.reactivex.Single

class CatalogInteractor(private val catalogRepository: CatalogRepository) {

    companion object {
        private const val TAG_POPULAR_PRODUCTS = "ВыводитьНаГлавной"
        private const val CNT_POPULAR_SERVICES_IN_MAIN = 6
    }

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
            val availableProducts = catalogRepository.getShowcase(tags).blockingGet()

            for (catalogCategory in catalogCategories){
                for (subCategory in catalogCategory.subCategories){
                    subCategory.products = availableProducts.filter { it.tags.any { it in subCategory.productTags }}
                }

                catalogCategory.products = availableProducts.filter { it.tags.any { it in catalogCategory.productTags }}
            }
            return@map catalogCategories.filter { it.subCategories.isNotEmpty() || it.isPrimary }.sortedByDescending { it.isPrimary }
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
        val tags = if (!query.isNullOrEmpty()) listOf(query) else null
        return catalogRepository.getShowcase(tags)
    }

    fun getPopularProducts(): Single<List<ProductShortModel>>{
        return catalogRepository.getShowcase(listOf(TAG_POPULAR_PRODUCTS))
    }

    fun getPopularServices(): Single<List<ProductShortModel>>{
        return catalogRepository.getShowcase(listOf(TAG_POPULAR_PRODUCTS)).map {
            it.filter { it.defaultProduct }.take(CNT_POPULAR_SERVICES_IN_MAIN)
        }
    }

}