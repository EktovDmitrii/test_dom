package com.custom.rgs_android_dom.domain.purchase_service.model

import org.joda.time.LocalDateTime
import java.io.Serializable

data class PurchaseDateTimeModel(
    var date: LocalDateTime = LocalDateTime.now(),
    var selectedPeriodModel: PurchasePeriodModel? = null,
) : Serializable
