package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.responses.CatalogNodeResponse
import com.custom.rgs_android_dom.data.network.responses.ProductResponse
import com.custom.rgs_android_dom.data.network.responses.ProductShortResponse
import com.custom.rgs_android_dom.data.network.responses.ServiceResponse
import com.custom.rgs_android_dom.domain.catalog.models.*
import com.custom.rgs_android_dom.utils.asEnumOrDefault

object CatalogMapper {

    fun responseToCatalogCategories(response: List<CatalogNodeResponse>): List<CatalogCategoryModel>{
        val catalogCategories = arrayListOf<CatalogCategoryModel>()

        val categoryNodes = response.filter { it.parentNodeId == null }
        for (categoryNode in categoryNodes){
            val subNodes = response.filter { it.parentNodeId == categoryNode.id }
            val subCategories = arrayListOf<CatalogSubCategoryModel>()

            for (subNode in subNodes){
                val subCategory = CatalogSubCategoryModel(
                    id = subNode.id,
                    title = subNode.title ?: "",
                    parentCategoryId = categoryNode.id,
                    icon = "${BuildConfig.BASE_URL}/api/store/${subNode.logoMiddle}",
                    productTags = subNode.productTags ?: listOf(),
                    products = listOf()
                )
                subCategories.add(subCategory)
            }

            val category = CatalogCategoryModel(
                id = categoryNode.id,
                title = categoryNode.title ?: "",
                icon = "${BuildConfig.BASE_URL}/api/store/${categoryNode.iconLink}",
                productTags = categoryNode.productTags ?: listOf(),
                products = listOf(),
                subCategories = subCategories,
                isPrimary = categoryNode.id == categoryNodes[0].id
            )
            catalogCategories.add(category)

        }

        return catalogCategories
    }

    fun responseToProduct(response: ProductResponse): ProductModel {
        return ProductModel(
            code = response.code,
            activatedAt = response.activatedAt,
            archivedAt = response.archivedAt,
            coolOff = if (response.coolOff != null) {
                ProductCoolOffModel(
                    unitType = response.coolOff.unitType.asEnumOrDefault(ProductUnitType.UNKNOWN),
                    units = response.coolOff.units
                )
            } else {
                null
            },
            createdAt = response.createdAt,
            defaultProduct = response.defaultProduct,
            deliveryTime = response.deliveryTime,
            description = response.description,
            descriptionFormat = response.descriptionFormat,
            descriptionRef = response.descriptionRef,
            duration = if (response.duration != null){
                ProductDurationModel(
                    unitType = response.duration.unitType.asEnumOrDefault(ProductUnitType.UNKNOWN),
                    units = response.duration.units
                )
            } else {
                null
            },
            iconLink = "${BuildConfig.BASE_URL}/api/store/${response.iconLink}",
            id = response.id,
            insuranceProducts = response.insuranceProducts?.map {
                InsuranceProductModel(
                    productId = it.productId,
                    programId = it.programId
                )
            },
            internalDescription = response.internalDescription,
            name = response.name,
            objectRequired = response.objectRequired,
            price = if (response.price != null){
                ProductPriceModel(
                    amount = response.price.amount,
                    vatType = response.price.vatType
                )
            } else{
                null
            },
            status = response.status,
            tags = response.tags,
            validityFrom = response.validityFrom,
            validityTo = response.validityTo,
            versionActivatedAt = response.versionActivatedAt,
            versionArchivedAt = response.versionArchivedAt,
            versionCode = response.versionCode,
            versionCreatedAt = response.versionCreatedAt,
            versionId = response.versionId,
            versionStatus = response.versionStatus,
            title = response.title,
            type = response.type
        )
    }

    fun responseToService(response: ServiceResponse): ServiceModel {
        return ServiceModel(
            code = response.code,
            activatedAt = response.activatedAt,
            archivedAt = response.archivedAt,
            createdAt = response.createdAt,
            defaultProduct = if (response.defaultProduct != null){
                DefaultProductModel(
                    enabled = response.defaultProduct.enabled,
                    productId = response.defaultProduct.productId,
                    productName = response.defaultProduct.productName,
                    serviceConsultingId = response.defaultProduct.serviceConsultingId,
                    serviceConsultingName = response.defaultProduct.serviceConsultingName,
                    tags = response.defaultProduct.tags
                )
            } else {
                null
            },
            deliveryTime = response.deliveryTime,
            deliveryType = response.deliveryType,
            description = response.description,
            descriptionFormat = response.descriptionFormat,
            descriptionRef = response.descriptionRef,
            iconLink = "${BuildConfig.BASE_URL}/api/store/${response.iconLink}",
            id = response.id,
            internalDescription = response.internalDescription,
            name = response.name,
            objectRequired = response.objectRequired,
            price = if (response.price != null){
                ServicePriceModel(
                    amount = response.price.amount,
                    vatType = response.price.vatType
                )
            } else {
                null
            },
            provider = if (response.provider != null){
                ServiceProviderModel(
                    name = response.provider.name,
                    type = response.provider.type,
                    providerId = response.provider.providerId
                )
            } else {
                null
            },
            status = response.status,
            title = response.title,
            type = response.type,
            unitType = response.unitType,
            validityFrom = response.validityFrom,
            validityTo = response.validityTo,
            versionActivatedAt = response.versionActivatedAt,
            versionArchivedAt = response.versionArchivedAt,
            versionCode = response.versionCode,
            versionCreatedAt = response.versionCreatedAt,
            versionId = response.versionId,
            versionStatus = response.versionStatus
        )
    }

    fun responseToProductShort(response: ProductShortResponse): ProductShortModel {
        return ProductShortModel(
            id = response.id,
            type = response.type,
            title = response.title ?: "",
            code = response.code,
            icon = "${BuildConfig.BASE_URL}/api/store/${response.iconLink}",
            versionId = response.versionId,
            name = response.name,
            price = response.price,
            tags = response.tags ?: listOf(),
            defaultProduct = response.defaultProduct ?: false
        )
    }

}