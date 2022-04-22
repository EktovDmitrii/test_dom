package com.custom.rgs_android_dom.domain.catalog

import android.util.Log
import com.custom.rgs_android_dom.domain.catalog.models.*
import com.custom.rgs_android_dom.domain.main.CommentModel
import com.custom.rgs_android_dom.domain.repositories.CatalogRepository
import com.custom.rgs_android_dom.domain.repositories.PoliciesRepository
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import io.reactivex.Single
import org.joda.time.DateTime

class CatalogInteractor(
    private val catalogRepository: CatalogRepository,
    private val policiesRepository: PoliciesRepository
) {

    companion object {
        private const val TAG_POPULAR_SEARCH_PRODUCTS = "ВыводитьВПоиске"
        private const val TAG_POPULAR_PRODUCTS = "ВыводитьПродуктНаГлавной"
        private const val TAG_POPULAR_CATEGORIES = "ВыводитьУзелНаГлавной"
        private const val TAG_POPULAR_SERVICES = "ВыводитьУслугуНаГлавной"
        private const val CNT_POPULAR_SERVICES_IN_MAIN = 9
        private const val CNT_POPULAR_CATEGORIES_IN_MAIN = 6
        private const val CNT_POPULAR_PRODUCTS_IN_MAIN = 13
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
            return@map catalogCategories.filter { it.subCategories.isNotEmpty() || it.isPrimary }.sortedBy { it.sortOrder }
        }
    }

    fun getServices(): Single<List<ServiceModel>>{
        return catalogRepository.getServices()
    }

    fun getProducts(): Single<List<ProductModel>> {
        return catalogRepository.getProducts()
    }

    fun getProduct(productId: String, productVersionId: String?): Single<ProductModel>{
        return catalogRepository.getProduct(productId, productVersionId)
    }

    fun getProductServices(productId: String, productVersionId: String?, isPurchased: Boolean, validityFrom: DateTime?): Single<List<ServiceShortModel>> {
        return catalogRepository.getProductServices(productId, productVersionId).map { services->
            if (isPurchased){
                services.forEach { service->
                    val availableService = catalogRepository.getAvailableServiceInProduct(productId, null, service.serviceId).blockingGet()
                    service.quantity = availableService.available.toLong()
                }
            }
            if (validityFrom !=null && validityFrom.isAfterNow){
                services.forEach { service->
                    service.canBeOrdered = false
                }
            } else{
                services.forEach{service->
                    service.canBeOrdered = service.quantity > 0
                }
            }
            return@map services.map { it.copy(isPurchased = isPurchased) }
        }
    }

    fun getProductServiceDetails(productId: String, serviceId: String, serviceVersionId: String?): Single<ServiceModel> {
        return catalogRepository.getProductServiceDetails(productId, serviceId, serviceVersionId)
    }

    fun findProducts(query: String): Single<List<ProductShortModel>>{
        return catalogRepository.findProducts(query)
    }

    fun getPopularSearchProducts(): Single<List<ProductShortModel>>{
        return catalogRepository.getShowcase(listOf(TAG_POPULAR_SEARCH_PRODUCTS))
    }

    fun getPopularProducts(): Single<List<ProductShortModel>>{
        return catalogRepository.getShowcase(listOf(TAG_POPULAR_PRODUCTS)).map{
            it.filter { !it.defaultProduct }.take(CNT_POPULAR_PRODUCTS_IN_MAIN)
        }
    }


    fun getPopularServices(): Single<List<ProductShortModel>>{
        return catalogRepository.getShowcase(listOf(TAG_POPULAR_SERVICES)).map {
            it.filter { it.defaultProduct }.take(CNT_POPULAR_SERVICES_IN_MAIN)
        }
    }

    fun getPopularCategories(): Single<List<CatalogCategoryModel>>{
        return getCatalogCategories().map {
            it.filter { it.productTags.contains(TAG_POPULAR_CATEGORIES)}.take(CNT_POPULAR_CATEGORIES_IN_MAIN).sortedBy { it.sortOrder }
        }
    }

    fun getAvailableServices(): Single<List<AvailableServiceModel>>{
        return Single.zip(catalogRepository.getClientProducts(null), catalogRepository.getAvailableServices("client-service")){products, services->
           return@zip services.filter { service->
               val product = products.find { it.productId == service.productId && it.defaultProduct }
               return@filter product == null
           }
        }
    }

    fun getProductsOnBalance(): Single<List<ClientProductModel>>{
        return catalogRepository.getClientProducts(null).map {products->
            products.filter { !it.defaultProduct }
        }
    }

    fun getComments(): Single<List<CommentModel>> {
        return Single.just(
            listOf(
                CommentModel(
                    name = TranslationInteractor.getTranslation("app.menu.home.raiting_block.raiting_cell.one_name"),
                    rate = 5,
                    comment = TranslationInteractor.getTranslation("app.menu.home.raiting_block.raiting_cell.one_description")
                ),
                CommentModel(
                    name = TranslationInteractor.getTranslation("app.menu.home.raiting_block.raiting_cell.two_name"),
                    rate = 5,
                    comment = TranslationInteractor.getTranslation("app.menu.home.raiting_block.raiting_cell.two_description")
                ),
                CommentModel(
                    name = TranslationInteractor.getTranslation("app.menu.home.raiting_block.raiting_cell.three_name"),
                    rate = 5,
                    comment = TranslationInteractor.getTranslation("app.menu.home.raiting_block.raiting_cell.three_description")
                ),
                CommentModel(
                    name = TranslationInteractor.getTranslation("app.menu.home.raiting_block.raiting_cell.four_name"),
                    rate = 5,
                    comment = TranslationInteractor.getTranslation("app.menu.home.raiting_block.raiting_cell.four_description")
                ),
                CommentModel(
                    name = TranslationInteractor.getTranslation("app.menu.home.raiting_block.raiting_cell.five_name"),
                    rate = 4,
                    comment = TranslationInteractor.getTranslation("app.menu.home.raiting_block.raiting_cell.five_description")
                ),
                CommentModel(
                    name = TranslationInteractor.getTranslation("app.menu.home.raiting_block.raiting_cell.six_name"),
                    rate = 5,
                    comment = TranslationInteractor.getTranslation("app.menu.home.raiting_block.raiting_cell.six_description")
                )
            )
        )
    }

    fun getAvailableServiceInProduct(productId: String, serviceId: String): Single<AvailableServiceModel> {
        return catalogRepository.getAvailableServiceInProduct(productId, null, serviceId)
    }

    fun getProductsByContracts(): Single<List<ClientProductModel>> {
        return policiesRepository.getPoliciesSingle().flatMap {policies->
            val contractIds = policies.joinToString(",") { it.contractId }
            catalogRepository.getClientProducts(contractIds)
        }
    }

    fun getFromAvailableServices(serviceId: String): Single<AvailableServiceModel> {
        return catalogRepository.getAvailableServices("client-service").map { it.firstOrNull { it.id == serviceId } }
    }
}