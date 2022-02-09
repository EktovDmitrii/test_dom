package com.custom.rgs_android_dom.data.repositories.catalog

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.CatalogMapper
import com.custom.rgs_android_dom.data.providers.auth.manager.AuthContentProviderManager
import com.custom.rgs_android_dom.domain.catalog.models.*
import com.custom.rgs_android_dom.domain.catalog.models.ClientProductModel
import com.custom.rgs_android_dom.domain.repositories.CatalogRepository
import io.reactivex.Single

class CatalogRepositoryImpl(private val api: MSDApi, private val authContentProviderManager: AuthContentProviderManager): CatalogRepository {

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

    override fun getShowcase(tags: List<String>?, name: String?): Single<List<ProductShortModel>> {
        val showcaseSingle = if (authContentProviderManager.isAuthorized()){
            api.getShowcase(tags?.joinToString(","), name, 0, 5000)
        } else {
            api.getGuestShowcase(tags?.joinToString(","), name, 0, 5000)
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
        val request = if (authContentProviderManager.isAuthorized()) api.getProduct(productId)
            else api.getGuestProduct(productId)
        return request.map { response->
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

    override fun getProductServiceDetails(productId: String, serviceId: String): Single<ServiceModel> {
        val serviceModelSingle = if (authContentProviderManager.isAuthorized()) {
            api.getProductServiceDetails(productId, serviceId)
        } else {
            api.getGuestProductServiceDetails(productId, serviceId)
        }
        return serviceModelSingle.map { response ->
            CatalogMapper.responseToService(response)
        }
    }

    override fun getAvailableServices(): Single<List<AvailableServiceModel>> {
        return api.getAvailableServices(5000, 0, true).map { response->
            CatalogMapper.responseToBalanceServices(response)
        }
    }

    override fun getClientProducts(): Single<List<ClientProductModel>> {
        return api.getClientProducts(5000, 0, null).map {response ->
            return@map if (response.clientProducts != null){
                response.clientProducts.map { CatalogMapper.responseToClientProduct(it) }.filter { !it.defaultProduct }
            } else listOf()
        }
    }

    override fun getAvailableServiceInProduct(productId: String, serviceId: String): Single<AvailableServiceModel> {
        return api.getAvailableServices(5000, 0, true, "active", productId, serviceId).map { response->
            CatalogMapper.responseToBalanceServices(response)[0]
        }
    }

}