package com.custom.rgs_android_dom.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentResolver.SCHEME_FILE
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*
import kotlin.text.StringBuilder

@SuppressLint("Range")
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

fun Uri.length(contentResolver: ContentResolver): Long {
    val assetFileDescriptor = try {
        contentResolver.openAssetFileDescriptor(this, "r")
    } catch (e: FileNotFoundException) {
        null
    }
    val length = assetFileDescriptor?.use { it.length } ?: -1L
    if (length != -1L) {
        return length
    }

    if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
        return contentResolver.query(this, arrayOf(OpenableColumns.SIZE), null, null, null)
            ?.use { cursor ->
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (sizeIndex == -1) {
                    return@use -1L
                }
                cursor.moveToFirst()
                return try {
                    cursor.getLong(sizeIndex)
                } catch (_: Throwable) {
                    -1L
                }
            } ?: -1L
    } else {
        return -1L
    }
}