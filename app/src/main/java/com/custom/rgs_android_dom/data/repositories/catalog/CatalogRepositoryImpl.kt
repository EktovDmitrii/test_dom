package com.custom.rgs_android_dom.data.repositories.catalog

import com.custom.rgs_android_dom.BuildConfig
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
            CatalogMapper.responseToCatalogCategories(response.items?.filter { it.name?.contains("node") == false} ?: listOf())
        }
    }

    override fun getProducts(): Single<List<ProductModel>> {
        return api.getProducts(size = 100, index = 0, businessLine = BuildConfig.BUSINESS_LINE).map { response->
            response.items?.map {
                CatalogMapper.responseToProduct(it)
            }
        }
    }

    override fun getServices(): Single<List<ServiceModel>> {
        return api.getServices(size = 100, index = 0, businessLine = BuildConfig.BUSINESS_LINE).map {response->
            response.items?.map {
                CatalogMapper.responseToService(it)
            }
        }
    }

    override fun getShowcase(tags: List<String>?): Single<List<ProductShortModel>> {
        val showcaseSingle = if (authContentProviderManager.isAuthorized()){
            api.getShowcase(tags = tags?.joinToString(","), index = 0, size = 5000, businessLine = BuildConfig.BUSINESS_LINE)
        } else {
            api.getGuestShowcase(tags = tags?.joinToString(","), index = 0, size = 5000, businessLine = BuildConfig.BUSINESS_LINE)
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

    override fun findProducts(query: String): Single<List<ProductShortModel>> {
        val showcaseSingle = if (authContentProviderManager.isAuthorized()){
            api.getShowcase(nameOrTag = query, index = 0, size = 5000, businessLine = BuildConfig.BUSINESS_LINE)
        } else {
            api.getGuestShowcase(nameOrTag = query, index = 0, size = 5000, businessLine = BuildConfig.BUSINESS_LINE)
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

    override fun getProduct(productId: String, productVersionId: String?): Single<ProductModel> {
        val request = if (authContentProviderManager.isAuthorized()) api.getProduct(productVersionId ?: productId)
            else api.getGuestProduct(productId)
        return request.map { response->
            CatalogMapper.responseToProduct(response)
        }
    }

    override fun getProductServices(productId: String, productVersionId: String?): Single<List<ServiceShortModel>> {
        val productServicesSingle = if (authContentProviderManager.isAuthorized()){
            api.getProductServicesResponse(productVersionId ?: "", 100, 0)
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

    override fun getProductServiceDetails(productId: String, serviceId: String, serviceVersionId: String?): Single<ServiceModel> {
        val serviceModelSingle = if (authContentProviderManager.isAuthorized()) {
            api.getProductServiceDetails(productId, serviceVersionId ?: "")
        } else {
            api.getGuestProductServiceDetails(productId, serviceId)
        }
        return serviceModelSingle.map { response ->
            CatalogMapper.responseToService(response)
        }
    }

    override fun getAvailableServices(): Single<List<AvailableServiceModel>> {
        return api.getAvailableServices(size = 5000, index = 0, withBalance = true, businessLine = BuildConfig.BUSINESS_LINE).map { response->
            CatalogMapper.responseToBalanceServices(response).filter { it.available > 0 }
        }
    }

    override fun getClientProducts(contractIds: String?): Single<List<ClientProductModel>> {
        return api.getClientProducts(size = 5000, index = 0, businessLine = BuildConfig.BUSINESS_LINE, contractIds = contractIds, status = "active").map { response ->
            return@map if (response.clientProducts != null){
                response.clientProducts.map { CatalogMapper.responseToClientProduct(it) }
            } else listOf()
        }
    }

    override fun getAvailableServiceInProduct(productId: String, clientProductId: String?, serviceId: String): Single<AvailableServiceModel> {
        return api.getAvailableServices(5000, 0, true, "active", productId, serviceId).map { response->
            val services = CatalogMapper.responseToBalanceServices(response)
            if (clientProductId == null) services[0]
            else services.first { it.clientProductId == clientProductId }
        }
    }

}