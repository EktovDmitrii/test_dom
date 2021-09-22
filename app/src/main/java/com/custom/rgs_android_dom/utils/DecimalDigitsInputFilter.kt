package com.custom.rgs_android_dom.utils

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Matcher
import java.util.regex.Pattern

class DecimalDigitsInputFilter(var digitsBeforeZero: Int, digitsAfterZero: Int) :
    InputFilter {

    private var pattern: Pattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val matcher: Matcher = pattern.matcher(dest)
        return if (!matcher.matches()) {
            if (dest.toString().contains(".")) {
                if (dest.toString().substring(dest.toString().indexOf(".")).length > 2) {
                    ""
                } else null
            } else if (!Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}").matcher(dest)
                    .matches()
            ) {
                if (!dest.toString().contains(".")) {
                    if (source.toString().equals(".", ignoreCase = true)) {
                        return null
                    }
                }
                ""
            } else {
                null
            }
        } else null
    }

}