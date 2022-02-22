package com.custom.rgs_android_dom.data.db.models.chat

import org.joda.time.DateTime

data class SubtypeDbModel(
    val archivedAt: DateTime?,
    val internal: Boolean,
    val logo: String,
    val name: String,
    val subtype: String,
    val type: String
)