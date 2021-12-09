package com.custom.rgs_android_dom.utils.activity.activity_contracts

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts

class PropertyDocumentsActivityContract(private val supported: Array<String>) :
    ActivityResultContracts.GetMultipleContents() {

    override fun createIntent(context: Context, input: String): Intent {
        return super.createIntent(context, input).putExtra(Intent.EXTRA_MIME_TYPES, supported)
    }
}