package com.custom.rgs_android_dom.utils

import android.app.Activity
import android.content.ContentResolver.SCHEME_FILE
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun Uri.getFileName(context: Context): String? =
    if (SCHEME_FILE == scheme) lastPathSegment
    else context.contentResolver.query(
        this, null, null,
        null, null, null
    )?.use {
        it.takeIf { it.moveToFirst() }?.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }

fun Uri.convertToFile(activity: Activity): File? {
    val name = getFileName(activity) ?: ""
    val parcelFileDescriptor = activity.contentResolver.openFileDescriptor(this, "r", null)
    parcelFileDescriptor?.let {
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(activity.cacheDir, name)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        return file
    }
    return null
}