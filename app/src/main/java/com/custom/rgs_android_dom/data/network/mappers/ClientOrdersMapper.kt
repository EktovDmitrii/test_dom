package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.responses.OrdersResponse
import com.custom.rgs_android_dom.domain.client.models.*
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_ONLY
import com.custom.rgs_android_dom.utils.formatPrice
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

object ClientOrdersMapper {

    private const val ICON_ENDPOINT = "${BuildConfig.BASE_URL}/api/store"

    fun responseToOrderItem(invoices: List<GeneralInvoice>, ordersResponse: OrdersResponse): List<OrderItemModel> {
        val invoicesMap = mutableMapOf<String, GeneralInvoice>()
        invoices.forEach {
            invoicesMap[it.details?.orderId ?: ""] = it
        }
        return ordersResponse.orders.map {
            val service = if (it.services?.isNotEmpty() == true) it.services[0] else null
            val status = getStatus(it.status ?: "")
            val localDateTime = LocalDate.parse(it.deliveryDate, DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ"))
            val additionalInvoices = invoicesMap[it.id]
            OrderItemModel(
                id = it.id,
                productId = service?.clientProductId ?: "",
                defaultProduct = service?.defaultProduct ?: false,
                deliveryTime = it.deliveryTime?.let { time -> "${time.from} - ${time.to}" } ?: "",
                fix = service?.serviceFixPrice ?: false,
                title = service?.serviceName ?: "",
                status = status,
                price = service?.productPrice,
                date = localDateTime,
                icon = if (service?.serviceLogoMiddle?.isNotEmpty() == true)
                    "${ICON_ENDPOINT}/${service.serviceLogoMiddle}" else "",
                description = getOrderDescription(status, service?.productPrice ?: 0, localDateTime),
                invoices = mutableListOf<InvoiceItemModel>().apply {
                    if (status == OrderStatus.DRAFT || status == OrderStatus.CONFIRMED) {
                        add(
                            InvoiceItemModel(
                                orderId = it.id,
                                type = InvoiceType.MAIN,
                                description = ""
                            )
                        )
                    }
                    additionalInvoices?.items?.forEach { item ->
                        add(
                            InvoiceItemModel(
                                orderId = it.id,
                                type = InvoiceType.ADDITIONAL,
                                amount = item.amount,
                                vatType = item.vatType,
                                description = "Дополнительный счёт  ∙  ${item.price?.formatPrice()}"
                            )
                        )
                    }
                }
            )
        }
    }

    private fun getOrderDescription(status: OrderStatus, productPrice: Int, localDate: LocalDate): String {
        val onlyDate = localDate.toString(DATE_PATTERN_DATE_ONLY)
        val price = productPrice.formatPrice()
        val isYellowColor = status == OrderStatus.DRAFT || status == OrderStatus.CONFIRMED || status == OrderStatus.ACTIVE
        return if (isYellowColor)
            "<font color=\"${"#EEA641"}\">${status.value}</font>  ∙  $price  ∙  $onlyDate"
        else "${status.value}  ∙  $price  ∙  $onlyDate"
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
