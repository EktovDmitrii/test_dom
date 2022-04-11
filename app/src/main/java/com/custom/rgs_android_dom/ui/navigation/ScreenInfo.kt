package com.custom.rgs_android_dom.ui.navigation

import java.io.Serializable

data class ScreenInfo(
    val targetScreen: TargetScreen,
    val params: Any?
): Serializable