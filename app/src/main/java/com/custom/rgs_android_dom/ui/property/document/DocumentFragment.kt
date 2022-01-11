package com.custom.rgs_android_dom.ui.property.document

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentDocumentBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.property.add.details.files.PropertyUploadDocumentsAdapter
import com.custom.rgs_android_dom.ui.property.add.details.files.PropertyUploadDocumentsFragment
import com.custom.rgs_android_dom.ui.property.document.edit_document_list.EditDocumentListBottomSheetFragment
import com.custom.rgs_android_dom.ui.property.document.edit_document_list.EditDocumentListener
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.gone
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class DocumentFragment :
    BaseFragment<DocumentViewModel, FragmentDocumentBinding>(R.layout.fragment_document),
    EditDocumentListener {

    private val documentListAdapter: DocumentListAdapter
        get() = binding.listDocumentsRecyclerView.adapter as DocumentListAdapter

    companion object {
        private const val ARG_OBJECT_ID = "ARG_OBJECT_ID"
        private const val ARG_PROPERTY_MODEL = "ARG_PROPERTY_MODEL"

        fun newInstance(
            objectId: String,
            propertyItemModel: PropertyItemModel,
        ): DocumentFragment {
            return DocumentFragment().args {
                putString(ARG_OBJECT_ID, objectId)
                putSerializable(ARG_PROPERTY_MODEL, propertyItemModel)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(ARG_OBJECT_ID),
            requireArguments().getSerializable(ARG_PROPERTY_MODEL)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            onClose()
        }

        binding.addDocTextView.setOnDebouncedClickListener {
            val propertyUploadFilesFragment = PropertyUploadDocumentsFragment()
            propertyUploadFilesFragment.show(childFragmentManager, propertyUploadFilesFragment.TAG)
        }

        binding.listDocumentsRecyclerView.adapter = DocumentListAdapter{ documentIndex ->
            viewModel.deleteDocument(documentIndex)
        }

        binding.editDocumentListImageView.setOnDebouncedClickListener {
            val editDocumentListBottomSheetFragment =
                EditDocumentListBottomSheetFragment.newInstance(this)
            editDocumentListBottomSheetFragment.show(
                childFragmentManager,
                editDocumentListBottomSheetFragment.TAG
            )
        }
        binding.listDocumentsRecyclerView.apply {
            adapter = documentListAdapter
        }
        subscribe(viewModel.propertyDocumentsObserver) { propertyItem ->

            binding.editDocumentListImageView.visibleIf(propertyItem.documents.isNotEmpty())
            binding.emptyDocListGroup.visibleIf(propertyItem.documents.isEmpty())

            if (propertyItem.documents.isNotEmpty()) {
                documentListAdapter.setItems(
                    propertyItem.documents,
                    viewModel.isDeleteButtonVisible
                )
            }

        }
    }

    override fun changeDeleteButtonVisibility(isDeleteButtonVisible: Boolean) {
        documentListAdapter.showDeleteButton(isDeleteButtonVisible)
    }

}
