package com.custom.rgs_android_dom.utils

import android.view.View
import android.widget.FrameLayout
import android.widget.OverScroller
import androidx.core.content.ContextCompat
import androidx.customview.widget.ViewDragHelper
import com.custom.rgs_android_dom.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

fun BottomSheetBehavior<*>.getViewDragHelper(): ViewDragHelper? = BottomSheetBehavior::class.java
    .getDeclaredField("viewDragHelper")
    .apply { isAccessible = true }
    .let { field -> field.get(this) as? ViewDragHelper? }

fun ViewDragHelper.getScroller(): OverScroller? = ViewDragHelper::class.java
    .getDeclaredField("mScroller")
    .apply { isAccessible = true }
    .let { field -> field.get(this) as? OverScroller? }

fun BottomSheetDialogFragment.expand() {
    dialog?.setOnShowListener {
        val mainExecutor = ContextCompat.getMainExecutor(requireContext())
        mainExecutor.execute {
            val bottomSheet = (dialog as? BottomSheetDialog)?.findViewById<View>(R.id.design_bottom_sheet) as? FrameLayout
            bottomSheet?.let {
                BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }
}

fun BottomSheetDialogFragment.expandDialogs() {
    val bottomSheet = (dialog as? BottomSheetDialog)?.findViewById<View>(R.id.design_bottom_sheet) as? FrameLayout
    bottomSheet?.let {
        BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED
    }
}