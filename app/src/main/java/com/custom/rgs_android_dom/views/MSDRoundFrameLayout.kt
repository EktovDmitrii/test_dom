package com.custom.rgs_android_dom.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.utils.dp

class MSDRoundFrameLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {

    private lateinit var rectF: RectF
    private val path = Path()
    private var cornerRadius = 1f

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDRoundFrameLayout, 0, 0)
        attrs.getDimension(R.styleable.MSDRoundFrameLayout_cornerRadius, 1f).let { radius ->
            cornerRadius = radius.dp(context)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF = RectF(0f, 0f, w.toFloat(), h.toFloat())
        this.path.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW);
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.clipPath(this.path)
        super.dispatchDraw(canvas)
    }
}