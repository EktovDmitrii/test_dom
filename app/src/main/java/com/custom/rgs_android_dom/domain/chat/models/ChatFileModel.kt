package com.custom.rgs_android_dom.domain.chat.models

data class ChatFileModel(
    val extension: String,
    val hasPreviewImage: Boolean,
    val height: Int,
    val id: String,
    val mimeType: String,
    val miniPreview: String?,
    val name: String,
    val size: Long,
    val width: Long
)