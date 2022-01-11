package com.custom.rgs_android_dom.ui.property.document.edit_document_list

import android.content.Context
import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentEditDocumentListBottomSheetBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.ui.property.add.details.files.PropertyUploadDocumentsFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class EditDocumentListBottomSheetFragment :
    BaseBottomSheetModalFragment<EditDocumentListViewModel, FragmentEditDocumentListBottomSheetBinding>() {

    private var listener: EditDocumentListener? = null

    companion object {
        private const val ARG_LISTENER = "ARG_LISTENER"

        fun newInstance(editDocumentListener: EditDocumentListener): EditDocumentListBottomSheetFragment {
            return EditDocumentListBottomSheetFragment().args {
                putSerializable(ARG_LISTENER, editDocumentListener)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getSerializable(ARG_LISTENER)
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (requireArguments().containsKey(ARG_LISTENER)) {
            requireArguments().getSerializable(ARG_LISTENER) as EditDocumentListener
        } else {
            null
        }
    }

    override val TAG: String = "EDIT_DOC_LIST_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addDocumentTextView.setOnDebouncedClickListener {
            val propertyUploadFilesFragment = PropertyUploadDocumentsFragment()
            propertyUploadFilesFragment.show(childFragmentManager, propertyUploadFilesFragment.TAG)
        }

        binding.editDocumentListTextView.setOnDebouncedClickListener {
            listener?.changeDeleteButtonVisibility(true)
            dialog?.dismiss()
        }
    }
}
