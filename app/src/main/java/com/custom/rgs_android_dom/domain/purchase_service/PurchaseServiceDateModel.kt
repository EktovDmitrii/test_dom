package com.custom.rgs_android_dom.domain.purchase_service

import org.joda.time.LocalDateTime
import java.io.Serializable

data class PurchaseDateTimeModel(
    val date: LocalDateTime,
    val period: String
) : Serializable
