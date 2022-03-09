package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.responses.CancelledTaskResponse
import com.custom.rgs_android_dom.data.network.responses.CancelledTasksResponse
import com.custom.rgs_android_dom.data.network.responses.OrderResponse
import com.custom.rgs_android_dom.data.network.responses.OrdersResponse
import com.custom.rgs_android_dom.domain.client.models.*

object OrdersMapper {

    private const val ICON_ENDPOINT = "${BuildConfig.BASE_URL}/api/store"

    fun responseToOrders(invoices: List<GeneralInvoice>, ordersResponse: OrdersResponse): List<Order> {
        return ordersResponse.orders?.map {
            responseToOrder(generalInvoices = invoices, orderResponse = it)
        } ?: listOf()
    }

    fun responseToOrder(generalInvoices: List<GeneralInvoice>? = null, orderResponse: OrderResponse): Order {
        return Order(
            id = orderResponse.id,
            objectId = orderResponse.orderObject?.objectId,
            address = OrderAddress(
                objectName = orderResponse.orderObject?.objectName,
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
            generalInvoice = generalInvoices?.filter { invoice ->
                invoice.details?.orderId == orderResponse.id
            }
        )
    }

    fun responseToCancelledTasks(response: CancelledTasksResponse): List<CancelledTaskModel>{
        return response.tasks?.map {
            responseToCancelledTask(it)
        } ?: listOf()
    }

    fun responseToCancelledTask(response: CancelledTaskResponse): CancelledTaskModel {
        return CancelledTaskModel(
            status = response.status,
            subStatus = response.subStatus,
            taskId = response.taskId
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
