package com.custom.rgs_android_dom.utils

import android.content.ContentResolver.SCHEME_FILE
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.text.StringBuilder

fun Uri.getFileName(context: Context): String? =
    if (SCHEME_FILE == scheme) lastPathSegment
    else context.contentResolver.query(
        this, null, null,
        null, null, null
    )?.use {
        it.takeIf { it.moveToFirst() }?.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }

fun Uri.convertToFile(context: Context): File? {
    val name = getFileName(context) ?: ""
    val nameStringBuilder = StringBuilder()
        .append(name.substringBefore("."))
        .append(".")
        .append(name.substringAfter(".").toLowerCase(Locale.getDefault()))
    val parcelFileDescriptor = context.contentResolver.openFileDescriptor(this, "r", null)
    parcelFileDescriptor?.let {
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(context.cacheDir, nameStringBuilder.toString())
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        return file
    }
    return null
}