package com.custom.rgs_android_dom.domain.catalog

import com.custom.rgs_android_dom.domain.catalog.models.*
import com.custom.rgs_android_dom.domain.main.CommentModel
import com.custom.rgs_android_dom.domain.repositories.CatalogRepository
import io.reactivex.Single

class CatalogInteractor(private val catalogRepository: CatalogRepository) {

    companion object {
        private const val TAG_POPULAR_SEARCH_PRODUCTS = "ВыводитьВПоиске"
        private const val TAG_POPULAR_PRODUCTS = "ВыводитьНаГлавной"
        private const val TAG_POPULAR_CATEGORIES = "ВыводитьНаГлавной"
        private const val TAG_POPULAR_SERVICES = "ВыводитьНаГлавной"
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
            val availableProducts = catalogRepository.getShowcase(tags, null).blockingGet()

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

    fun getProduct(productId: String): Single<ProductModel>{
        return catalogRepository.getProduct(productId)
    }

    fun getProductServices(productId: String): Single<List<ServiceShortModel>> {
        return catalogRepository.getProductServices(productId)
    }

    fun getProductServiceDetails(productId: String, serviceId: String): Single<ServiceModel> {
        return catalogRepository.getProductServiceDetails(productId, serviceId)
    }

    fun findProducts(query: String): Single<List<ProductShortModel>>{
        return catalogRepository.getShowcase(null, query)
    }

    fun getPopularSearchProducts(): Single<List<ProductShortModel>>{
        return catalogRepository.getShowcase(listOf(TAG_POPULAR_SEARCH_PRODUCTS), null)
    }

    fun getPopularProducts(): Single<List<ProductShortModel>>{
        return catalogRepository.getShowcase(listOf(TAG_POPULAR_PRODUCTS), null).map{
            it.filter { !it.defaultProduct }.take(CNT_POPULAR_PRODUCTS_IN_MAIN)
        }
    }


    fun getPopularServices(): Single<List<ProductShortModel>>{
        return catalogRepository.getShowcase(listOf(TAG_POPULAR_SERVICES), null).map {
            it.filter { it.defaultProduct }.take(CNT_POPULAR_SERVICES_IN_MAIN)
        }
    }

    fun getPopularCategories(): Single<List<CatalogCategoryModel>>{
        return catalogRepository.getCatalogCategories()
            .map {
                it.filter {
                    it.subCategories.isNotEmpty() && it.productTags.contains(TAG_POPULAR_CATEGORIES)
                }.take(CNT_POPULAR_CATEGORIES_IN_MAIN)
            }
    }

    fun getAvailableServices(): Single<List<AvailableServiceModel>>{
        return catalogRepository.getAvailableServices()
    }

    fun getClientProducts(): Single<List<ClientProductModel>>{
        return catalogRepository.getClientProducts()
    }

    fun getComments(): Single<List<CommentModel>> {
        return Single.just(
            listOf(
                CommentModel(
                    name = "Сергей",
                    rate = 5,
                    comment = "Все очень грамотно, быстро, все объяснили по заявке."
                ),
                CommentModel(
                    name = "Ханума",
                    rate = 5,
                    comment = "Все быстро организовали, не пришлось долго ждать, мастер вежливый и культурный"
                ),
                CommentModel(
                    name = "Ирина",
                    rate = 5,
                    comment = "Вовремя приехали, быстро все сделали, мастер очень понравился, готова рекомендовать"
                ),
                CommentModel(
                    name = "Татьяна",
                    rate = 5,
                    comment = "Очень довольна, все было своевременно, мастер был всегда на связи"
                ),
                CommentModel(
                    name = "Серафима",
                    rate = 4,
                    comment = "Мастер - хороший, толковый парень, на все руки мастер"
                ),
                CommentModel(
                    name = "Анастасия",
                    rate = 5,
                    comment = "Супер все быстро организовано, качество работ на высоком уровне, все понравилось"
                )
            )
        )
    }
}