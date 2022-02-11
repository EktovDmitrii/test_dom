package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.data.network.responses.GeneralInvoicesResponse
import com.custom.rgs_android_dom.domain.client.models.*

object GeneralInvoiceMapper {

    fun responseToDomainModel(response: GeneralInvoicesResponse): List<GeneralInvoice> {
        return response.invoice?.map {
            GeneralInvoice(
                id = it.id,
                amountTotal = it.amountTotal,
                cancelledAt = it.cancelledAt,
                cardBindingId = it.cardBindingId,
                client = GeneralInvoiceClient(
                    id = it.client?.id,
                    email = it.client?.email,
                    name = it.client?.name,
                    phone = it.client?.phone
                ),
                createdAt = it.createdAt,
                currency = it.currency,
                details = GeneralInvoiceDetails(
                    bankOrderId = it.details?.bankOrderId,
                    orderCode = it.details?.orderCode,
                    orderId = it.details?.orderId,
                    paymentEmail = it.details?.paymentEmail,
                    paymentOrderId = it.details?.paymentOrderId,
                    paymentUrl = it.details?.paymentUrl
                ),
                items = it.items.map { item ->
                    GeneralInvoiceItem(
                        amount = item.amount,
                        itemCode = item.itemCode,
                        itemId = item.itemId,
                        measure = item.measure,
                        name = item.name,
                        price = item.price,
                        quantity = item.quantity,
                        vatAmount = item.vatAmount,
                        vatType = item.vatType,
                        provider = GeneralInvoiceProvider(
                            agentType = item.provider?.agentType,
                            inn = item.provider?.inn,
                            name = item.provider?.name,
                            phone = item.provider?.phone
                        )
                    )
                },
                maskedPan = it.maskedPan,
                num = it.num,
                paidAt = it.paidAt,
                paymentSystem = it.paymentSystem,
                paymentWay = it.paymentWay,
                status = it.status,
                submittedAt = it.submittedAt,
                type = it.type,
                vatTotal = it.vatTotal
            )
        } ?: listOf()

    }
}
