package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.responses.OrderResponse
import com.custom.rgs_android_dom.data.network.responses.OrdersResponse
import com.custom.rgs_android_dom.domain.client.models.*

object OrdersMapper {

    private const val ICON_ENDPOINT = "${BuildConfig.BASE_URL}/api/store"

    fun responseToOrders(invoices: List<GeneralInvoice>, ordersResponse: OrdersResponse): List<Order> {
        val invoicesMap = mutableMapOf<String, GeneralInvoice>()
        invoices.forEach {
            invoicesMap[it.details?.orderId ?: ""] = it
        }
        return ordersResponse.orders.map {
            responseToOrder(generalInvoice = invoicesMap[it.id], orderResponse = it)
        }
    }

    fun responseToOrder(generalInvoice: GeneralInvoice? = null, orderResponse: OrderResponse): Order {
        return Order(
            id = orderResponse.id,
            address = OrderAddress(
                address = orderResponse.address?.address,
                cityFiasId = orderResponse.address?.cityFiasId,
                cityName = orderResponse.address?.cityName,
                fiasId = orderResponse.address?.fiasId,
                regionFiasId = orderResponse.address?.regionFiasId,
                regionName = orderResponse.address?.regionName
            ),
            client = OrderClient(
                clientId = orderResponse.client?.clientId,
                name = orderResponse.client?.name,
                phone = orderResponse.client?.phone
            ),
            closedAt = orderResponse.closedAt,
            code = orderResponse.code,
            comment = orderResponse.comment,
            createdAt = orderResponse.createdAt,
            deliveryDate = orderResponse.deliveryDate,
            deliveryTime = DeliveryTime(
                from = orderResponse.deliveryTime?.from,
                to = orderResponse.deliveryTime?.to
            ),
            provider = OrderProvider(
                code = orderResponse.provider?.code,
                inn = orderResponse.provider?.inn,
                name = orderResponse.provider?.name,
                providerId = orderResponse.provider?.providerId
            ),
            refId = orderResponse.refId,
            services = orderResponse.services?.map {
                OrderService(
                    clientProductId = it.clientProductId,
                    clientServiceId = it.clientServiceId,
                    defaultProduct = it.defaultProduct,
                    productIcon = it.productIcon?.let { logo -> "$ICON_ENDPOINT/$logo" },
                    productName = it.productName,
                    productPrice = it.productPrice,
                    serviceCode = it.serviceCode,
                    serviceDeliveryType = it.serviceDeliveryType,
                    serviceFixPrice = it.serviceFixPrice,
                    serviceIdInGroup = it.serviceIdInGroup,
                    serviceLogoMiddle = it.serviceLogoMiddle?.let { logo -> "$ICON_ENDPOINT/$logo" },
                    serviceName = it.serviceName,
                    serviceType = it.serviceType,
                    serviceVersionId = it.serviceVersionId
                )
            },
            status = getStatus(orderResponse.status ?: ""),
            generalInvoice = generalInvoice
        )
    }

    private fun getStatus(key: String): OrderStatus {
        return when (key) {
            OrderStatus.DRAFT.key -> OrderStatus.DRAFT
            OrderStatus.CONFIRMED.key -> OrderStatus.CONFIRMED
            OrderStatus.ACTIVE.key -> OrderStatus.ACTIVE
            OrderStatus.RESOLVED.key -> OrderStatus.RESOLVED
            OrderStatus.CANCELLED.key -> OrderStatus.CANCELLED
            else -> OrderStatus.NOT_FOUND
        }
    }
}
