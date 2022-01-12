package com.custom.rgs_android_dom.ui.property.document.edit_document

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.custom.rgs_android_dom.databinding.FragmentEditDocumentBottomSheetBinding
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable

class EditDocumentBottomSheetFragment : BottomSheetDialogFragment() {

    private var editDocumentListener: EditDocumentListener? = null

    private val binding: FragmentEditDocumentBottomSheetBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    companion object {
        const val TAG: String = "EDIT_DOCUMENT_FRAGMENT"
        fun newInstance(): EditDocumentBottomSheetFragment =
            EditDocumentBottomSheetFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is EditDocumentListener) {
            editDocumentListener = parentFragment as EditDocumentListener
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.reloadDocumentTextView.setOnDebouncedClickListener {
            editDocumentListener?.onReloadDocumentClick()
            dismissAllowingStateLoss()
        }

        binding.editDocumentNameTextView.setOnDebouncedClickListener {
            editDocumentListener?.onRenameDocumentClick()
            dismissAllowingStateLoss()
        }

        binding.removeDocumentTextView.setOnDebouncedClickListener {
            editDocumentListener?.onDeleteDocumentClick()
            dismissAllowingStateLoss()
        }

    }

    interface EditDocumentListener : Serializable {
        fun onDeleteDocumentClick()
        fun onRenameDocumentClick()
        fun onReloadDocumentClick()
    }
}
