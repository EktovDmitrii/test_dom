package com.custom.rgs_android_dom.data.providers.auth.constants

import android.net.Uri
import com.custom.rgs_android_dom.data.providers.auth.MSDAuthContentProvider
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.ENDPOINT_ACCESS_TOKEN
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.ENDPOINT_ACTIONS
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.ENDPOINT_IS_AUTHORIZED
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.ENDPOINT_REFRESH_TOKEN
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.ENDPOINT_REFRESH_TOKEN_EXPIRES_AT
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.RME_AUTHORITY

object UriHelper {

    private val urisMap = mutableMapOf<String, ArrayList<Uri>>().apply {
        put(ENDPOINT_IS_AUTHORIZED, arrayListOf(
            MSDAuthContentProvider.URI_IS_AUTHORIZED,
            Uri.parse("content://${RME_AUTHORITY}/$ENDPOINT_IS_AUTHORIZED")
        ))

        put(ENDPOINT_ACCESS_TOKEN, arrayListOf(
            MSDAuthContentProvider.URI_ACCESS_TOKEN,
            Uri.parse("content://${RME_AUTHORITY}/$ENDPOINT_ACCESS_TOKEN")
        ))

        put(ENDPOINT_REFRESH_TOKEN, arrayListOf(
            MSDAuthContentProvider.URI_REFRESH_TOKEN,
            Uri.parse("content://${RME_AUTHORITY}/$ENDPOINT_REFRESH_TOKEN")
        ))

        put(ENDPOINT_REFRESH_TOKEN_EXPIRES_AT, arrayListOf(
            MSDAuthContentProvider.URI_REFRESH_TOKEN_EXPIRES_AT,
            Uri.parse("content://${RME_AUTHORITY}/$ENDPOINT_REFRESH_TOKEN_EXPIRES_AT")
        ))

        put(ENDPOINT_ACTIONS, arrayListOf(
            MSDAuthContentProvider.URI_ACTIONS,
            Uri.parse("content://${RME_AUTHORITY}/$ENDPOINT_ACTIONS")
        ))
    }

    fun urisForEndpoint(endpoint: String): ArrayList<Uri> {
        return urisMap[endpoint] ?: arrayListOf()
    }

}