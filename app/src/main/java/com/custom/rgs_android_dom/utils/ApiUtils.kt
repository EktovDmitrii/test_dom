package com.custom.rgs_android_dom.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun File.asRequestBody(): RequestBody {
    val mediaType = when (this.extension) {
        "png" -> "image/png"
        "jpeg" -> "image/jpeg"
        "jpg" -> "image/jpg"
        "gif" -> "image/gif"
        "doc" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        "docx" -> "application/msword"
        "txt" -> "text/plain"
        "xls" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        "xlsx" -> "application/vnd.ms-excel"
        "pdf" -> "application/pdf"
        else -> ""
    }

    return this.asRequestBody(mediaType.toMediaTypeOrNull())
}

fun File.toMultipartFormData(): MultipartBody.Part {
    val mediaType = when (this.extension) {
        "png" -> "image/png"
        "jpeg" -> "image/jpeg"
        "jpg" -> "image/jpg"
        "gif" -> "image/gif"
        "doc" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        "docx" -> "application/msword"
        "txt" -> "text/plain"
        "xls" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        "xlsx" -> "application/vnd.ms-excel"
        "pdf" -> "application/pdf"
        "rtf" -> "application/rtf"
        "bmp" -> "image/bmp"
        else -> ""
    }

    return MultipartBody.Part.createFormData(
        "file",
        this.name,
        this.asRequestBody(mediaType.toMediaTypeOrNull())
    )
}