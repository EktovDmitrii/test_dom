package com.custom.rgs_android_dom.ui.navigation

import com.custom.rgs_android_dom.ui.base.BaseFragment

data class ScreenScope (
    val id: Int,
    val fragment: BaseFragment<*, *>
)