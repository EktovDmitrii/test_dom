package com.custom.rgs_android_dom.data.repositories.catalog

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.CatalogMapper
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.domain.catalog.models.ServiceModel
import com.custom.rgs_android_dom.domain.repositories.CatalogRepository
import io.reactivex.Single

class CatalogRepositoryImpl(private val api: MSDApi): CatalogRepository {

    override fun getCatalogCategories(): Single<List<CatalogCategoryModel>> {
        return api.getCatalogNodes(null, null).map { response->
            CatalogMapper.responseToCatalogCategories(response.items ?: listOf())
        }
    }

    override fun getProducts(): Single<List<ProductModel>> {
        return api.getProducts(100, 0).map {response->
            response.items?.map {
                CatalogMapper.responseToProduct(it)
            }
        }
    }

    override fun getServices(): Single<List<ServiceModel>> {
        return api.getServices(100, 0).map {response->
            response.items?.map {
                CatalogMapper.responseToService(it)
            }
        }
    }

    override fun getProductsAvailableForPurchase(tags: List<String>?): Single<List<ProductShortModel>> {
        return api.getProductsAvailableForPurchase(tags?.joinToString(", ")).map { response->
            return@map if (response.products != null) {
                response.products.map {
                    CatalogMapper.responseToProductShort(it)
                }
            } else {
                listOf()
            }
        }
    }

    override fun getProduct(productId: String): Single<ProductModel> {
        return api.getProduct(productId).map { response->
            CatalogMapper.responseToProduct(response)
        }
    }

}