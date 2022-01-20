package com.custom.rgs_android_dom.data.repositories.catalog

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.CatalogMapper
import com.custom.rgs_android_dom.data.providers.auth.manager.AuthContentProviderManager
import com.custom.rgs_android_dom.domain.catalog.models.*
import com.custom.rgs_android_dom.domain.repositories.CatalogRepository
import io.reactivex.Single

class CatalogRepositoryImpl(private val api: MSDApi, private val authContentProviderManager: AuthContentProviderManager): CatalogRepository {

    companion object{
        private const val TAGS_SEPARATOR = ","
        private const val QUERY_SHOWCASE_INDEX = 0
        private const val QUERY_SHOWCASE_SIZE = 5000L
        private const val QUERY_SERVICES_INDEX = 0
        private const val QUERY_SERVICES_SIZE = 100
        private const val QUERY_PRODUCTS_INDEX = 0
        private const val QUERY_PRODUCTS_SIZE = 100
        private const val QUERY_PRODUCT_SERVICES_INDEX = 0
        private const val QUERY_PRODUCT_SERVICES_SIZE = 100
    }

    override fun getCatalogCategories(): Single<List<CatalogCategoryModel>> {
        val catalogNodesSingle = if (authContentProviderManager.isAuthorized()){
            api.getCatalogNodes(null, null)
        } else {
            api.getGuestCatalogNodes(null, null)
        }

        return catalogNodesSingle.map { response->
            CatalogMapper.responseToCatalogCategories(response.items ?: listOf())
        }
    }

    override fun getProducts(): Single<List<ProductModel>> {
        return api.getProducts(QUERY_PRODUCTS_SIZE, QUERY_PRODUCTS_INDEX).map {response->
            response.items?.map {
                CatalogMapper.responseToProduct(it)
            }
        }
    }

    override fun getServices(): Single<List<ServiceModel>> {
        return api.getServices(QUERY_SERVICES_SIZE, QUERY_SERVICES_INDEX).map {response->
            response.items?.map {
                CatalogMapper.responseToService(it)
            }
        }
    }

    override fun getShowcase(tags: List<String>?): Single<List<ProductShortModel>> {
        val showcaseSingle = if (authContentProviderManager.isAuthorized()){
            api.getShowcase(tags?.joinToString(TAGS_SEPARATOR), QUERY_SHOWCASE_INDEX, QUERY_SHOWCASE_SIZE)
        } else {
            api.getGuestShowcase(tags?.joinToString(TAGS_SEPARATOR), QUERY_SHOWCASE_INDEX, QUERY_SHOWCASE_SIZE)
        }

        return showcaseSingle.map { response->
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

    override fun getProductServices(productId: String): Single<List<ServiceShortModel>> {
        val productServicesSingle = if (authContentProviderManager.isAuthorized()){
            api.getProductServicesResponse(productId, QUERY_PRODUCT_SERVICES_SIZE, QUERY_PRODUCT_SERVICES_INDEX)
        } else {
            api.getGuestProductServicesResponse(productId, QUERY_PRODUCT_SERVICES_SIZE, QUERY_PRODUCT_SERVICES_INDEX)
        }

        return productServicesSingle.map { response->
            return@map if (response.items != null) {
                response.items.map {
                    CatalogMapper.responseToServiceShort(it)
                }
            } else {
                listOf()
            }
        }
    }

}