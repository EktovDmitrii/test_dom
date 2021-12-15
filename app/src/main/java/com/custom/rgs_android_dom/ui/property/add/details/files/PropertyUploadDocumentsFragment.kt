package com.custom.rgs_android_dom.ui.property.add.details.files

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentPropertyUploadDocumentsBinding
import com.custom.rgs_android_dom.ui.alert.OnAlertClickListener
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.activity.activity_contracts.PropertyDocumentsActivityContract
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class PropertyUploadDocumentsFragment : BaseBottomSheetModalFragment<PropertyUploadDocumentsViewModel, FragmentPropertyUploadDocumentsBinding>(),
    OnAlertClickListener {

    override val TAG: String = "PROPERTY_UPLOAD_DOCUMENTS_FRAGMENT"

    companion object {
        // Supported mime types
        private const val IMAGE_JPEG = "image/jpeg"
        private const val IMAGE_JPG = "image/jpg"
        private const val IMAGE_PNG = "image/png"
        private const val IMAGE_BMP = "image/bmp"
        private const val TEXT_TXT = "text/plain"
        private const val TEXT_PDF = "application/pdf"
        private const val TEXT_DOC = "application/msword"
        private const val TEXT_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        private const val TEXT_RTF = "application/rtf"
        private const val TEXT_RTF_2 = "text/rtf"

        private val supportedMediaTypes = arrayOf("image/*")

        private val supportedTextTypes = arrayOf(TEXT_TXT, TEXT_PDF, TEXT_DOC, TEXT_DOCX, TEXT_RTF, TEXT_RTF_2)
    }


    private val chooseMediaAction = registerForActivityResult(PropertyDocumentsActivityContract(supportedMediaTypes)) { uris ->
        viewModel.onDocumentsSelected(uris)
    }

    private val chooseDocumentsAction = registerForActivityResult(PropertyDocumentsActivityContract(supportedTextTypes)) { uris ->
            viewModel.onDocumentsSelected(uris)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.uploadMediaTextView.setOnDebouncedClickListener {
            chooseMediaAction.launch("*/*")
        }

        binding.uploadFilesTextView.setOnDebouncedClickListener {
            chooseDocumentsAction.launch("*/*")
        }

    }

}