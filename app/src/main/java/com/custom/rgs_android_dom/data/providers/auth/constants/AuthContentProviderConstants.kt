package com.custom.rgs_android_dom.data.providers.auth.constants

import com.custom.rgs_android_dom.BuildConfig

object AuthContentProviderConstants {

    const val MSD_AUTHORITY = "${BuildConfig.APPLICATION_ID}.data.providers"
    const val RME_AUTHORITY = "${BuildConfig.RME_APP_ID}.data.providers"

    const val ENDPOINT_ACTIONS = "auth/actions"
    const val ENDPOINT_IS_AUTHORIZED = "auth/is_authorized"
    const val ENDPOINT_ACCESS_TOKEN = "auth/access_token"
    const val ENDPOINT_REFRESH_TOKEN = "auth/refresh_token"
    const val ENDPOINT_REFRESH_TOKEN_EXPIRES_AT = "auth/refresh_token_expires_at"

    const val CONTENT_KEY_ACCESS_TOKEN = "CONTENT_KEY_ACCESS_TOKEN"
    const val CONTENT_KEY_REFRESH_TOKEN = "CONTENT_KEY_REFRESH_TOKEN"
    const val CONTENT_KEY_REFRESH_TOKEN_EXPIRES_AT = "CONTENT_KEY_REFRESH_TOKEN_EXPIRES_AT"
    const val CONTENT_KEY_IS_AUTHORIZED = "CONTENT_KEY_IS_AUTHORIZED"

}