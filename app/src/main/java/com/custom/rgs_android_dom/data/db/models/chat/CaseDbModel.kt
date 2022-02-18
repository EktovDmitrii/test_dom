package com.custom.rgs_android_dom.data.db.models.chat

import androidx.room.Entity
import com.custom.rgs_android_dom.domain.chat.models.CaseStatus
import com.custom.rgs_android_dom.domain.chat.models.CaseSubStatus
import org.joda.time.DateTime

@Entity(tableName = "cases", primaryKeys = ["channelId", "taskId"])
data class CaseDbModel(
    val channelId: String,
    val name: String,
    val subtype: SubtypeDbModel?,
    val taskId: String,
    val unreadPosts: Int,
    val isArchived: Boolean,
    val status: CaseStatus,
    val subStatus: CaseSubStatus,
    val reportedAt: DateTime
)