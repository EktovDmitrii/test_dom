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
        const val IMAGE_JPEG = "image/jpeg"
        const val IMAGE_JPG = "image/jpg"
        const val IMAGE_PNG = "image/png"
        const val IMAGE_BMP = "image/bmp"
        const val TEXT_TXT = "text/plain"
        const val TEXT_PDF = "application/pdf"
        const val TEXT_DOC = "application/msword"
        const val TEXT_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        const val TEXT_RTF = "application/rtf"
    }

    private val supportedImages = arrayOf(IMAGE_JPEG, IMAGE_JPG, IMAGE_PNG, IMAGE_BMP)
    private val supportedTextTypes = arrayOf(TEXT_TXT, TEXT_PDF, TEXT_DOC, TEXT_DOCX, TEXT_RTF)

    private val chooseMediaAction = registerForActivityResult(PropertyDocumentsActivityContract(supportedImages)) { uris ->
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