package com.custom.rgs_android_dom.domain.chat.models

import org.joda.time.DateTime
import java.io.Serializable

data class CaseModel(
    val channelId: String,
    val name: String,
    val subtype: SubtypeModel?,
    val taskId: String,
    val unreadPosts: Int,
    val isArchived: Boolean,
    val status: CaseStatus,
    val subStatus: CaseSubStatus,
    val reportedAt: DateTime
): Serializable