package com.custom.rgs_android_dom.data.providers.auth

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.CONTENT_KEY_ACCESS_TOKEN
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.CONTENT_KEY_IS_AUTHORIZED
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.CONTENT_KEY_REFRESH_TOKEN
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.CONTENT_KEY_REFRESH_TOKEN_EXPIRES_AT
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.ENDPOINT_ACCESS_TOKEN
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.ENDPOINT_ACTIONS
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.ENDPOINT_IS_AUTHORIZED
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.ENDPOINT_REFRESH_TOKEN
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.ENDPOINT_REFRESH_TOKEN_EXPIRES_AT
import com.custom.rgs_android_dom.data.providers.auth.constants.AuthContentProviderConstants.MSD_AUTHORITY
import com.google.gson.Gson

class MSDAuthContentProvider : ContentProvider() {

    companion object {

        private const val CONTENT_CODE_ACTIONS = 0
        private const val CONTENT_CODE_IS_AUTHORIZED = 1
        private const val CONTENT_CODE_ACCESS_TOKEN = 2
        private const val CONTENT_CODE_REFRESH_TOKEN = 3
        private const val CONTENT_CODE_REFRESH_TOKEN_EXPIRES_AT = 4

        private const val TYPE_ACTIONS = "vnd.android.cursor.item/$MSD_AUTHORITY/$ENDPOINT_ACTIONS"
        private const val TYPE_QUERY = "vnd.android.cursor.item/$MSD_AUTHORITY/query"

        private const val CODE_OK = 1221

        val URI_ACTIONS: Uri = Uri.parse("content://$MSD_AUTHORITY/$ENDPOINT_ACTIONS")
        val URI_IS_AUTHORIZED: Uri  = Uri.parse("content://$MSD_AUTHORITY/$ENDPOINT_IS_AUTHORIZED")
        val URI_ACCESS_TOKEN: Uri  = Uri.parse("content://$MSD_AUTHORITY/$ENDPOINT_ACCESS_TOKEN")
        val URI_REFRESH_TOKEN: Uri  = Uri.parse("content://$MSD_AUTHORITY/$ENDPOINT_REFRESH_TOKEN")
        val URI_REFRESH_TOKEN_EXPIRES_AT: Uri  = Uri.parse("content://$MSD_AUTHORITY/$ENDPOINT_REFRESH_TOKEN_EXPIRES_AT")
    }

    private var authSharedPreferences: AuthSharedPreferences? = null
    private val matcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        matcher.addURI(
            MSD_AUTHORITY,
            ENDPOINT_ACTIONS,
            CONTENT_CODE_ACTIONS
        )
        matcher.addURI(
            MSD_AUTHORITY,
            ENDPOINT_IS_AUTHORIZED,
            CONTENT_CODE_IS_AUTHORIZED
        )
        matcher.addURI(
            MSD_AUTHORITY,
            ENDPOINT_ACCESS_TOKEN,
            CONTENT_CODE_ACCESS_TOKEN
        )
        matcher.addURI(
            MSD_AUTHORITY,
            ENDPOINT_REFRESH_TOKEN,
            CONTENT_CODE_REFRESH_TOKEN
        )
        matcher.addURI(
            MSD_AUTHORITY,
            ENDPOINT_REFRESH_TOKEN_EXPIRES_AT,
            CONTENT_CODE_REFRESH_TOKEN_EXPIRES_AT
        )
    }

    override fun onCreate(): Boolean {
        context?.let { context ->
            authSharedPreferences = AuthSharedPreferences(context, Gson())
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        return when (matcher.match(uri)){
            CONTENT_CODE_IS_AUTHORIZED -> {
                val cursor = MatrixCursor(arrayOf(CONTENT_KEY_IS_AUTHORIZED))
                if (authSharedPreferences?.getAccessToken().isNullOrEmpty()){
                    cursor.newRow().add(false)
                } else {
                    cursor.newRow().add(true)
                }
                cursor
            }
            CONTENT_CODE_ACCESS_TOKEN -> {
                val cursor = MatrixCursor(arrayOf(CONTENT_KEY_ACCESS_TOKEN))
                cursor.newRow().add(authSharedPreferences?.getAccessToken())
                cursor
            }
            CONTENT_CODE_REFRESH_TOKEN -> {
                val cursor = MatrixCursor(arrayOf(CONTENT_KEY_REFRESH_TOKEN, CONTENT_KEY_REFRESH_TOKEN_EXPIRES_AT))
                cursor.newRow().add(CONTENT_KEY_REFRESH_TOKEN, authSharedPreferences?.getRefreshToken())
                cursor
            }
            CONTENT_CODE_REFRESH_TOKEN_EXPIRES_AT -> {
                val cursor = MatrixCursor(arrayOf(CONTENT_KEY_REFRESH_TOKEN, CONTENT_KEY_REFRESH_TOKEN_EXPIRES_AT))
                cursor.newRow().add(CONTENT_KEY_REFRESH_TOKEN, authSharedPreferences?.getRefreshTokenExpiresAt())
                cursor
            }
            else -> {
                throw Exception("Failed to parse URI")
            }
        }
    }

    override fun getType(uri: Uri): String {
        return when (matcher.match(uri)){
            CONTENT_CODE_IS_AUTHORIZED,
            CONTENT_CODE_ACCESS_TOKEN,
            CONTENT_CODE_REFRESH_TOKEN,
            CONTENT_CODE_REFRESH_TOKEN_EXPIRES_AT -> {
                TYPE_QUERY
            }
            else -> {
                TYPE_ACTIONS
            }
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        values?.let { values->
            authSharedPreferences?.saveAuthCredentials(
                accessToken = values.getAsString(CONTENT_KEY_ACCESS_TOKEN),
                refreshToken = values.getAsString(CONTENT_KEY_REFRESH_TOKEN),
                refreshTokenExpiresAt = values.getAsString(CONTENT_KEY_REFRESH_TOKEN_EXPIRES_AT)
            )
        }
        return uri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        authSharedPreferences?.clear()
        return CODE_OK
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        values?.let { values->
            authSharedPreferences?.saveAuthCredentials(
                accessToken = values.getAsString(CONTENT_KEY_ACCESS_TOKEN),
                refreshToken = values.getAsString(CONTENT_KEY_REFRESH_TOKEN),
                refreshTokenExpiresAt = values.getAsString(CONTENT_KEY_REFRESH_TOKEN_EXPIRES_AT)
            )
        }
        return CODE_OK
    }
}