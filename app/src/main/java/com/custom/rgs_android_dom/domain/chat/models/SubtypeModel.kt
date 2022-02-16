package com.custom.rgs_android_dom.domain.chat.models

import org.joda.time.DateTime
import java.io.Serializable

data class SubtypeModel(
    val archivedAt: DateTime?,
    val internal: Boolean,
    val logo: String,
    val name: String,
    val subtype: String,
    val type: String
): Serializable