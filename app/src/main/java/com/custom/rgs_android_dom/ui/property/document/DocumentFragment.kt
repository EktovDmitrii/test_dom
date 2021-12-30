package com.custom.rgs_android_dom.ui.property.document

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentDocumentBinding
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyDocumentsModel
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.property.add.details.PropertyDetailsFragment
import com.custom.rgs_android_dom.ui.property.add.details.files.PropertyUploadDocumentsFragment
import com.custom.rgs_android_dom.ui.rationale.RequestRationaleFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.gone
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visible
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class DocumentFragment :
    BaseFragment<DocumentViewModel, FragmentDocumentBinding>(R.layout.fragment_document) {

    private var propertyDocumentsModel: PropertyDocumentsModel? = null

    private var adapter: DocumentListAdapter? = null

    private var gridLayoutManager: GridLayoutManager? = null

    companion object {
        private const val ARG_PROPERTY_MODEL = "ARG_PROPERTY_MODEL"

        fun newInstance(
            propertyDocumentsModel: PropertyDocumentsModel
        ): DocumentFragment {
            return DocumentFragment().args {
                putSerializable(ARG_PROPERTY_MODEL, propertyDocumentsModel)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(ARG_PROPERTY_MODEL)
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        propertyDocumentsModel = if (requireArguments().containsKey(ARG_PROPERTY_MODEL))
            requireArguments().getSerializable(ARG_PROPERTY_MODEL) as PropertyDocumentsModel
        else
            null

        propertyDocumentsModel?.let { viewModel.initData(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObserver()
        initButtons()
    }

    private fun initButtons() = with(binding) {
        backImageView.setOnDebouncedClickListener {
            onClose()
        }

        addDocTextView.setOnDebouncedClickListener {
            val propertyUploadFilesFragment = PropertyUploadDocumentsFragment()
            propertyUploadFilesFragment.show(childFragmentManager, propertyUploadFilesFragment.TAG)
        }
    }

    private fun initObserver() = with(binding) {
        subscribe(viewModel.propertyDocumentsObserver) { propertyItem ->

            if (propertyItem.propertyDocuments.isEmpty()) {
                editDocumentListImageView.gone()
                emptyDocListGroup.visible()
            } else {
                editDocumentListImageView.visible()
                emptyDocListGroup.gone()
            }

            if (propertyItem.propertyDocuments.isNotEmpty())
                adapter?.submitList(propertyItem.propertyDocuments)

        }
    }

    private fun initAdapter() {
        gridLayoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
        binding.listDocumentsRecyclerView.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
        }
        adapter = DocumentListAdapter(::onDocumentClick, ::onDeleteClick)
        binding.listDocumentsRecyclerView.adapter = adapter
    }

    private fun onDocumentClick(docUrl: String) {
        viewModel.openDocument(docUrl)
    }

    private fun onDeleteClick(docUrl: String) {
        viewModel.deleteDocument()

    }
}
