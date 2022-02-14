package com.custom.rgs_android_dom.domain.client.models

import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_FULL_MONTH
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_ONLY
import com.custom.rgs_android_dom.utils.formatPrice
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import java.io.Serializable

data class Order(
    val id: String,
    val address: OrderAddress? = null,
    val client: OrderClient? = null,
    val closedAt: String? = null,
    val code: String? = null,
    val comment: String? = null,
    val createdAt: String? = null,
    val deliveryDate: String? = null,
    val deliveryTime: DeliveryTime? = null,
    val provider: OrderProvider? = null,
    val refId: String? = null,
    val services: List<OrderService>? = null,
    val status: OrderStatus,
    val generalInvoice: List<GeneralInvoice>? = null
) : Serializable {

    fun getOrderDescription(): String {
        val localDate = LocalDate.parse(deliveryDate, DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ"))
        val onlyDate = localDate.toString(DATE_PATTERN_DATE_ONLY)
        val service = if (services?.isNotEmpty() == true) services[0] else null
        val price = (service?.productPrice ?: 0).formatPrice()
        val isYellowColor = status == OrderStatus.DRAFT || status == OrderStatus.CONFIRMED || status == OrderStatus.ACTIVE
        return if (isYellowColor)
            "<font color=\"${"#EEA641"}\">${status.value}</font>  ∙  $price  ∙  $onlyDate"
        else "${status.value}  ∙  $price  ∙  $onlyDate"
    }

    fun getOrderStateTitle(): String = "Заказ ${status.value.toLowerCase()}"

    fun getDateTime(): String {
        val localDate = LocalDate.parse(deliveryDate, DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ"))
        val onlyDate = localDate.toString(DATE_PATTERN_DATE_FULL_MONTH)
        val time = "${deliveryTime?.from} - ${deliveryTime?.to}"
        return "$onlyDate; $time"
    }

    fun getPaymentState(): String {
        return when (status) {
            OrderStatus.DRAFT -> {
                "Ожидает оплату"
            }
            OrderStatus.CANCELLED -> {
                "Счёт аннулирован"
            }
            else -> {
                "Оплачен"
            }
        }
    }

    fun getPaymentStateWithDate(): String {
        return when (status) {
            OrderStatus.DRAFT -> {
                "<font color=\"${"#8E8F8F"}\">Ожидает оплату</font>"
            }
            OrderStatus.CANCELLED -> {
                "Счёт аннулирован"
            }
            else -> {
                "<font color=\"${"#EEA641"}\">Оплачено</font>"
            }
        }
    }
}
