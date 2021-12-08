package com.custom.rgs_android_dom.ui.property.add.details.files

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.custom.rgs_android_dom.databinding.FragmentPropertyUploadDocumentsBinding
import com.custom.rgs_android_dom.ui.alert.AlertDialogFragment
import com.custom.rgs_android_dom.ui.alert.OnAlertClickListener
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.convertToFile
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe

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

    private val chooseMediaAction = registerForActivityResult(MyContract(supportedImages)) { uris ->
        viewModel.onDocumentsSelected(uris)
    }

    private val chooseDocumentsAction =
        registerForActivityResult(MyContract(supportedTextTypes)) { uris ->
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

class MyContract(private val supported: Array<String>) :
    ActivityResultContracts.GetMultipleContents() {

    override fun createIntent(context: Context, input: String): Intent {
        return super.createIntent(context, input).putExtra(Intent.EXTRA_MIME_TYPES, supported)
    }
}