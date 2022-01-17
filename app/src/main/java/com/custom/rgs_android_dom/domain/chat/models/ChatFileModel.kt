package com.custom.rgs_android_dom.domain.chat.models

import android.graphics.Bitmap
import org.joda.time.LocalDateTime
import java.io.Serializable

data class ChatFileModel(
    val senderId: String,
    val extension: String,
    val hasPreviewImage: Boolean,
    val height: Int,
    val id: String,
    val mimeType: String,
    val miniPreview: Bitmap,
    var preview: String,
    val name: String,
    val size: Long,
    val width: Long,
    val createdAt: LocalDateTime
): Serializable