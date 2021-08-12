package com.custom.rgs_android_dom.utils

import android.text.SpannableString
import android.text.TextPaint
import android.text.style.URLSpan
import android.widget.TextView

fun TextView.stripUnderlines() {
    val s = SpannableString(text)
    val spans = s.getSpans(0, s.length, URLSpan::class.java)
    for (span in spans) {
        val start = s.getSpanStart(span)
        val end = s.getSpanEnd(span)
        s.removeSpan(span)
        val noUnderlinedSpan = URLSpanNoUnderline(span.url)
        s.setSpan(noUnderlinedSpan, start, end, 0)
    }
    text = s
}

class URLSpanNoUnderline(text: String) : URLSpan(text) {

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }

}