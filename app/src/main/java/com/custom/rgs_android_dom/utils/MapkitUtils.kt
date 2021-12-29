package com.custom.rgs_android_dom.utils

import android.graphics.Rect
import com.yandex.mapkit.ScreenPoint

fun ScreenPoint.copy(): ScreenPoint {
    return ScreenPoint(this.x, this.y)
}

fun Rect.toScreenPoint(): ScreenPoint {
    return ScreenPoint(this.exactCenterX(), this.exactCenterY())
}