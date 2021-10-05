package com.custom.rgs_android_dom.utils

import android.widget.OverScroller
import androidx.customview.widget.ViewDragHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior

fun BottomSheetBehavior<*>.getViewDragHelper(): ViewDragHelper? = BottomSheetBehavior::class.java
    .getDeclaredField("viewDragHelper")
    .apply { isAccessible = true }
    .let { field -> field.get(this) as? ViewDragHelper? }

fun ViewDragHelper.getScroller(): OverScroller? = ViewDragHelper::class.java
    .getDeclaredField("mScroller")
    .apply { isAccessible = true }
    .let { field -> field.get(this) as? OverScroller? }