package com.custom.rgs_android_dom.domain.property.details.exceptions

import java.lang.RuntimeException

sealed class PropertyDocumentValidationException : RuntimeException() {
    class UnsupportedFileType(val extension: String) : PropertyDocumentValidationException()
    object FileSizeExceeded : PropertyDocumentValidationException()
    object TotalFilesSizeExceeded : PropertyDocumentValidationException()
}
