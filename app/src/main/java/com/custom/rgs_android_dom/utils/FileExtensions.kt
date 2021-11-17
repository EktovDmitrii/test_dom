package com.custom.rgs_android_dom.utils

import java.io.File
import java.util.*

fun File.isImage(): Boolean{
    val validExtension = listOf("png", "jpg", "jpeg", "bmp")
    val filenameArray = this.name.split(".")
    val extension = filenameArray[filenameArray.size - 1].toLowerCase(Locale.getDefault())
    return validExtension.contains(extension)
}

val File.size get() = if (!exists()) 0.0 else length().toDouble()

val File.sizeInKb get() = size / 1024

val File.sizeInMb get() = sizeInKb / 1024

val Long.sizeInMb get() = ((this.toDouble()) / 1024) / 1024