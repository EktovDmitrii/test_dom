package com.custom.rgs_android_dom.data.repositories.catalog

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.CatalogMapper
import com.custom.rgs_android_dom.domain.catalog.models.CatalogNodeModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.domain.catalog.models.ServiceModel
import com.custom.rgs_android_dom.domain.repositories.CatalogRepository
import io.reactivex.Single

class CatalogRepositoryImpl(private val api: MSDApi): CatalogRepository {

    override fun getCatalogNodes(): Single<List<CatalogNodeModel>> {
        return api.getCatalogNodes(null, null).map { response->
            response.items?.map {
                CatalogMapper.responseToCatalogNode(it)
            }
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

}