package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.data.network.responses.CatalogNodeResponse
import com.custom.rgs_android_dom.data.network.responses.ProductResponse
import com.custom.rgs_android_dom.data.network.responses.ServiceResponse
import com.custom.rgs_android_dom.domain.catalog.models.*
import com.custom.rgs_android_dom.utils.asEnumOrDefault

object CatalogMapper {

    fun responseToCatalogNode(response: CatalogNodeResponse): CatalogNodeModel {
        return CatalogNodeModel(
            id = response.id,
            code = response.code ?: "",
            iconLink = response.iconLink ?: "",
            name = response.name ?: "",
            parentNodeId = response.parentNodeId ?: "",
            productTags = response.productTags ?: listOf(),
            title = response.title ?: ""
        )
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
            iconLink = response.iconLink,
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
            iconLink = response.iconLink,
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

}