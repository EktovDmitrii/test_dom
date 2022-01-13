package com.custom.rgs_android_dom.ui.property.document.detail_document

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentDetailDocumentBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.confirm.ConfirmBottomSheetFragment
import com.custom.rgs_android_dom.ui.property.document.edit_document.EditDocumentBottomSheetFragment
import com.custom.rgs_android_dom.ui.property.document.edit_document_name.EditDocumentNameBottomSheetFragment
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class DetailDocumentFragment :
    BaseFragment<DetailDocumentViewModel, FragmentDetailDocumentBinding>(R.layout.fragment_detail_document),
    EditDocumentBottomSheetFragment.EditDocumentListener,
    ConfirmBottomSheetFragment.ConfirmListener,
    EditDocumentNameBottomSheetFragment.EditDocumentNameListener {

    private var currentPropertyDocument: PropertyDocument? = null

    companion object {
        private const val ARG_FILE = "ARG_FILE"
        private const val ARG_OBJECT_ID = "ARG_OBJECT_ID"
        private const val ARG_DOCUMENT_INDEX = "ARG_DOCUMENT_INDEX"

        fun newInstance(
            objectId: String,
            documentIndex: Int,
            propertyItemModel: PropertyItemModel?
        ): DetailDocumentFragment {
            return DetailDocumentFragment().args {
                putString(ARG_OBJECT_ID, objectId)
                putInt(ARG_DOCUMENT_INDEX, documentIndex)
                putSerializable(ARG_FILE, propertyItemModel)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(ARG_OBJECT_ID),
            requireArguments().getInt(ARG_DOCUMENT_INDEX),
            requireArguments().getSerializable(ARG_FILE) as PropertyItemModel?
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            onClose()
        }

        binding.editDocumentImageView.setOnDebouncedClickListener {
            val editDocumentBottomSheetFragment = EditDocumentBottomSheetFragment.newInstance()
            editDocumentBottomSheetFragment.show(
                childFragmentManager,
                EditDocumentBottomSheetFragment.TAG
            )
        }

        subscribe(viewModel.propertyDocumentObserver) { propertyDocument ->
            currentPropertyDocument = propertyDocument

            val documentId = propertyDocument.link.substringAfterLast("/", "missing")
            val url = "${BuildConfig.BASE_URL}/api/store/${documentId}"

            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(url))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.documentImageView)

            binding.documentNameTextView.text = propertyDocument.name
        }
    }

    override fun onDeleteDocumentClick() {
        val confirmDialog = ConfirmBottomSheetFragment.newInstance(
            icon = R.drawable.ic_confirm_delete_document,
            title = "Удалить документ?",
            description = "Документ будет удален безвозвратно. В случае необходимости вы сможете загрузить его заново",
            confirmText = "Да, удалить",
            cancelText = "Нет, оставить"
        )
        confirmDialog.show(childFragmentManager, ConfirmBottomSheetFragment.TAG)
    }

    override fun onConfirmClick() {
        viewModel.deleteDocument()
        onClose()
    }

    override fun onRenameDocumentClick() {
        val editDocumentNameDialog = currentPropertyDocument?.name?.let {
            EditDocumentNameBottomSheetFragment.newInstance(
                it
            )
        }
        editDocumentNameDialog?.show(childFragmentManager, EditDocumentNameBottomSheetFragment.TAG)
    }

    override fun onReloadDocumentClick() {
        //Todo не описано в задаче, но в дизайне есть. Там есть несколько возможных решений, дождаться ТЗ
    }

    override fun onSaveDocumentNameClick(documentName: String) {
        viewModel.renameDocument(documentName)
    }
}
