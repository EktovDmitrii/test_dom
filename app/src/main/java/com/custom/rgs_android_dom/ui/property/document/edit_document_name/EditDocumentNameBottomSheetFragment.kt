package com.custom.rgs_android_dom.ui.property.document.edit_document_name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.custom.rgs_android_dom.databinding.FragmentEditDocumentNameBottomSheetBinding
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable

class EditDocumentNameBottomSheetFragment : BottomSheetDialogFragment() {

    private var editDocumentNameListener: EditDocumentNameListener? = null

    private var documentName: String? = null

    private val binding: FragmentEditDocumentNameBottomSheetBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    companion object {
        const val TAG: String = "EDIT_DOCUMENT_FRAGMENT"
        private const val ARG_DOCUMENT_NAME = "ARG_DOCUMENT_NAME"

        fun newInstance(documentName: String): EditDocumentNameBottomSheetFragment =
            EditDocumentNameBottomSheetFragment().args {
                putString(ARG_DOCUMENT_NAME, documentName)

            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is EditDocumentNameListener) {
            editDocumentNameListener = parentFragment as EditDocumentNameListener
        }
        documentName = if (requireArguments().containsKey(ARG_DOCUMENT_NAME)) {
            requireArguments().getString(ARG_DOCUMENT_NAME)
        } else {
            null
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.documentNameTextInputLayout.setText(documentName.toString())

        binding.documentNameTextInputLayout.addTextWatcher { editedDocumentName ->
            binding.saveTextView.isEnabled = editedDocumentName != documentName && editedDocumentName.isNotEmpty()
            if (binding.saveTextView.isEnabled)
                documentName = editedDocumentName
        }

        binding.saveTextView.setOnDebouncedClickListener {
            documentName?.let { editDocumentNameListener?.onSaveDocumentNameClick(it) }
            dismissAllowingStateLoss()
        }
    }

    interface EditDocumentNameListener : Serializable {
        fun onSaveDocumentNameClick(documentName: String)
    }
}
