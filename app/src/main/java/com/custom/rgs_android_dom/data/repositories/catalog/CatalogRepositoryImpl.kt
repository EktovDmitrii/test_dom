package com.custom.rgs_android_dom.data.repositories.catalog

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.CatalogMapper
import com.custom.rgs_android_dom.data.providers.auth.manager.AuthContentProviderManager
import com.custom.rgs_android_dom.domain.catalog.MainPopularProductModel
import com.custom.rgs_android_dom.domain.catalog.models.*
import com.custom.rgs_android_dom.domain.repositories.CatalogRepository
import io.reactivex.Single

class CatalogRepositoryImpl(private val api: MSDApi, private val authContentProviderManager: AuthContentProviderManager): CatalogRepository {

    companion object {
        private const val TAG_POPULAR_PRODUCTS = "ВыводитьНаГлавной"
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

    override fun getServices(): Single<List<ServiceModel>> {
        return api.getServices(100, 0).map {response->
            response.items?.map {
                CatalogMapper.responseToService(it)
            }
        }
    }

    override fun getShowcase(tags: List<String>?): Single<List<ProductShortModel>> {
        val showcaseSingle = if (authContentProviderManager.isAuthorized()){
            api.getShowcase(tags?.joinToString(","))
        } else {
            api.getGuestShowcase(tags?.joinToString(","))
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
            api.getProductServicesResponse(productId, 100, 0)
        } else {
            api.getGuestProductServicesResponse(productId, 100, 0)
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

    override fun getMainPopularProducts(): Single<List<MainPopularProductModel>> {

        val popularProductsSingle = if (authContentProviderManager.isAuthorized()){
            api.getCatalogNodes(null, null)
        } else {
            api.getGuestCatalogNodes(null, null)
        }

        return popularProductsSingle
            .map { response ->
            response.let {
                it.items?.let {
                    if (it.any {!it.productTags.isNullOrEmpty()}){
                        it.filter {
                            it.productTags!!.contains(TAG_POPULAR_PRODUCTS)
                        }.map {
                            //val productResponse = api.getProduct(it.id).blockingGet()
                            CatalogMapper.responseToMainPopularProduct(it/*,productResponse*/)
                        }
                    } else {
                        listOf()
                    }
                }
            }
        }

    }

}