package com.custom.rgs_android_dom.domain.client.models

data class ActualAppVersionsModel(
    val clientCurrentVersion: Int,
    val appAndroidCurrentVersion: Int,
    val appAndroidCriticalVersion: Int,
)