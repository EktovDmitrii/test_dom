package com.custom.rgs_android_dom.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView


class MSDChatMessageItemLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val messageTextView by lazy { getChildAt(0) as TextView }
    private val timeTextView by lazy { getChildAt(1) as TextView }

    private val messageTextViewLayoutParams by lazy {
        messageTextView.layoutParams as LayoutParams
    }

    private val timeTextViewLayoutParams by lazy {
        timeTextView.layoutParams as LayoutParams
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var thisLayoutWidth = MeasureSpec.getSize(widthMeasureSpec)

        if (thisLayoutWidth <= 0) {
            return
        }

        val availableWidth = thisLayoutWidth - paddingLeft - paddingRight

        val messageTextViewWidth = messageTextView.measuredWidth +
                messageTextViewLayoutParams.leftMargin +
                messageTextViewLayoutParams.rightMargin

        val messageTextViewHeight = messageTextView.measuredHeight +
                messageTextViewLayoutParams.topMargin +
                messageTextViewLayoutParams.bottomMargin

        val timeTextViewWidth = timeTextView.measuredWidth +
                timeTextViewLayoutParams.leftMargin +
                timeTextViewLayoutParams.rightMargin

        val timeTextViewHeight = timeTextView.measuredHeight +
                timeTextViewLayoutParams.topMargin +
                timeTextViewLayoutParams.bottomMargin

        val messageTextViewLineCount = messageTextView.lineCount

        val messageTextViewLastLineWidth =
            if (messageTextViewLineCount > 0)
                messageTextView.layout.getLineWidth(messageTextViewLineCount - 1)
            else 0.0f

        thisLayoutWidth = paddingLeft + paddingRight

        var thisLayoutHeight = paddingTop + paddingBottom

        if (messageTextViewLineCount > 1 &&
            messageTextViewLastLineWidth + timeTextViewWidth < messageTextViewWidth
        ) {
            thisLayoutWidth += messageTextViewWidth
            thisLayoutHeight += messageTextViewHeight
        } else if (messageTextViewLineCount > 1 &&
            messageTextViewLastLineWidth + timeTextViewWidth >= availableWidth
        ) {
            thisLayoutWidth += messageTextViewWidth
            thisLayoutHeight += messageTextViewHeight + timeTextViewHeight
        } else if (messageTextViewLineCount == 1 &&
            messageTextViewLastLineWidth + timeTextViewWidth >= availableWidth
        ) {
            thisLayoutWidth += messageTextViewWidth
            thisLayoutHeight += messageTextViewHeight + timeTextViewHeight
        } else {
            thisLayoutWidth += messageTextViewWidth + timeTextViewWidth
            thisLayoutHeight += messageTextViewHeight
        }

        setMeasuredDimension(thisLayoutWidth, thisLayoutHeight)

        super.onMeasure(
            MeasureSpec.makeMeasureSpec(thisLayoutWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(thisLayoutHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        messageTextView.layout(
            paddingStart,
            paddingTop,
            messageTextView.width + paddingEnd,
            messageTextView.height + paddingBottom
        )

        timeTextView.layout(
            right - left - timeTextView.width - paddingRight,
            bottom - top - timeTextView.height - paddingBottom,
            right - left - paddingRight,
            bottom - top - paddingBottom
        )
    }
}