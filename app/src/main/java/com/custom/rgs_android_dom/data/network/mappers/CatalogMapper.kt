package com.custom.rgs_android_dom.data.network.mappers

import android.util.Log
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.responses.*
import com.custom.rgs_android_dom.domain.catalog.models.*
import com.custom.rgs_android_dom.domain.catalog.models.ClientProductModel
import com.custom.rgs_android_dom.utils.asEnumOrDefault
import com.custom.rgs_android_dom.utils.safeLet

object CatalogMapper {

    private const val TAG_PRODUCT_VIEW = "ПродуктовоеОтображение"

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
                    name = subNode.name ?: "",
                    parentCategoryId = categoryNode.id,
                    logoSmall = "${BuildConfig.BASE_URL}/api/store/${subNode.logoSmall}",
                    logoMiddle = "${BuildConfig.BASE_URL}/api/store/${subNode.logoMiddle}",
                    logoLarge = "${BuildConfig.BASE_URL}/api/store/${subNode.logoLarge}",
                    productTags = subNode.productTags ?: listOf(),
                    products = listOf()
                )
                subCategories.add(subCategory)
            }

            val category = CatalogCategoryModel(
                id = categoryNode.id,
                title = categoryNode.title ?: "",
                name = categoryNode.name ?: "",
                logoSmall = "${BuildConfig.BASE_URL}/api/store/${categoryNode.logoSmall}",
                logoMiddle = "${BuildConfig.BASE_URL}/api/store/${categoryNode.logoMiddle}",
                logoLarge = "${BuildConfig.BASE_URL}/api/store/${categoryNode.logoLarge}",
                productTags = categoryNode.productTags ?: listOf(),
                products = listOf(),
                subCategories = subCategories,
                isPrimary = categoryNode.productTags?.contains(TAG_PRODUCT_VIEW) ?: false
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

    fun responseToServiceShort(response: ServiceShortResponse): ServiceShortModel {
        return ServiceShortModel(
            priceAmount = response.priceAmount,
            providerId = response.providerId,
            providerName = response.providerName,
            quantity = response.quantity,
            serviceCode = response.serviceCode,
            serviceId = response.serviceId,
            serviceName = response.serviceName,
            serviceVersionId = response.serviceVersionId
        )
    }

    fun responseToBalanceServices(response: BalanceServicesResponse): List<AvailableServiceModel> {
        val balanceServices = arrayListOf<AvailableServiceModel>()

        safeLet(response.balance, response.services){balance, services->
            val filteredServices = services.filter {serviceDetails->
                balance.find { it.serviceId ==  serviceDetails.serviceId}  != null
            }

            filteredServices.forEach { serviceDetails->
                val serviceBalance = balance.find { it.serviceId ==  serviceDetails.serviceId}

                balanceServices.add(
                    AvailableServiceModel(
                        id = serviceDetails.id,
                        serviceId = serviceDetails.serviceId,
                        productId = serviceDetails.productId,
                        serviceName = serviceDetails.serviceName,
                        productIcon = "${BuildConfig.BASE_URL}/api/store/${serviceDetails.productIcon}",
                        available = serviceBalance?.available ?: 0,
                        total = serviceBalance?.total ?: 0
                    )
                )
            }

        }

        return balanceServices
    }

    fun responseToClientProduct(response: ClientProductResponse): ClientProductModel {
        return ClientProductModel(
            productDescriptionFormat = response.productDescriptionFormat ?: "",
            clientId = response.clientId ?: "",
            contractId = response.contractId ?: "",
            id = response.id ?: "",
            objectIds = response.objectIds ?: arrayListOf(),
            productCode = response.productCode ?: "",
            productDescription = response.productDescription ?: "",
            productDescriptionRef = response.productDescriptionRef ?: "",
            productIcon = "${BuildConfig.BASE_URL}/api/store/${response.productIcon}",
            productId = response.productId ?: "",
            productName = response.productName ?: "",
            productTitle = response.productTitle ?: "",
            productType = response.productType ?: "",
            productVersionId = response.productVersionId ?: "",
            status = response.status ?: "",
            validityFrom = response.validityFrom,
            validityTo = response.validityTo
        )
    }

}