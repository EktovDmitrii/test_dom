package com.custom.rgs_android_dom.utils.activity_contracts

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.phone.SmsRetriever
import java.util.regex.Matcher
import java.util.regex.Pattern

class OTCActivityContract() : ActivityResultContract<Intent, String?>() {

    override fun createIntent(context: Context, consentIntent: Intent): Intent {
        return consentIntent
    }

    override fun parseResult(resultCode: Int, data: Intent?): String? {
        return if (data != null){
            val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            parseOneTimeCode(message ?: "")
        } else {
            null
        }
    }

    private fun parseOneTimeCode(message: String): String? {
        val pattern: Pattern = Pattern.compile("(|^)\\d{4}")
        val matcher: Matcher = pattern.matcher(message)
        if (matcher.find()) {
            return matcher.group(0)
        }
        return null
    }
}