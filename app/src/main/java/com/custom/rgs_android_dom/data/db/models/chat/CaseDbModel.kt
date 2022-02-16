package com.custom.rgs_android_dom.data.db.models.chat

import androidx.room.Entity

@Entity(tableName = "cases", primaryKeys = ["channelId", "taskId"])
data class CaseDbModel(
    val channelId: String,
    val name: String,
    val subtype: SubtypeDbModel?,
    val taskId: String,
    val unreadPosts: Int,
    val isArchived: Boolean
)