package com.custom.rgs_android_dom.ui.property.document.edit_document_list

import java.io.Serializable

interface EditDocumentListener : Serializable {
    fun changeDeleteButtonVisibility(
        isDeleteButtonVisible: Boolean
    )
}
