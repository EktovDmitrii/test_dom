package com.custom.rgs_android_dom.ui.property.document

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.DownloadManagerRequestProvider
import com.custom.rgs_android_dom.databinding.FragmentDocumentBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.confirm.ConfirmBottomSheetFragment
import com.custom.rgs_android_dom.ui.property.add.details.files.PropertyUploadDocumentsFragment
import com.custom.rgs_android_dom.ui.property.document.edit_document_list.EditDocumentListBottomSheetFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.getDownloadManager
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class DocumentListFragment :
    BaseFragment<DocumentViewModel, FragmentDocumentBinding>(R.layout.fragment_document),
    EditDocumentListBottomSheetFragment.EditDocumentListListener,
    ConfirmBottomSheetFragment.ConfirmListener {

    companion object {
        private const val ARG_OBJECT_ID = "ARG_OBJECT_ID"

        fun newInstance(
            objectId: String
        ): DocumentListFragment {
            return DocumentListFragment().args {
                putString(ARG_OBJECT_ID, objectId)
            }
        }
    }

    private val documentListAdapter: DocumentListAdapter
        get() = binding.listDocumentsRecyclerView.adapter as DocumentListAdapter

    private var downloadManager: DownloadManager? = null

    private var downloadedFileId: Long? = null

    private var propertyDocumentForDelete: PropertyDocument? = null

    private var onDownloadCompleteReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, downloadIntent: Intent) {
            if (downloadIntent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                downloadIntent.extras?.let {
                    val fileId = it.getLong(DownloadManager.EXTRA_DOWNLOAD_ID)
                    if (fileId == downloadedFileId) {
                        downloadManager?.getUriForDownloadedFile(fileId)?.let { uri ->
                            openFile(uri)
                        }
                    }
                }
            }
        }
    }

    private val requestReadStorageAction =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    viewModel.onReadExternalStoragePermGranted()
                }
                !ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    viewModel.onShouldRequestReadExternalStoragePermRationale()
                }
            }
        }



    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_OBJECT_ID))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        downloadManager = requireContext().getDownloadManager()

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.disposeAll()
            onClose()
        }

        binding.addDocumentTextView.setOnDebouncedClickListener {
            viewModel.changeDeleteButtonsVisibility(false)
            openPropertyUploadDocumentsScreen()
        }

        binding.saveDocumentListImageView.setOnDebouncedClickListener {
            viewModel.changeDeleteButtonsVisibility(false)
        }

        binding.listDocumentsRecyclerView.adapter =
            DocumentListAdapter(onDeleteClick = { propertyDocument ->
                propertyDocumentForDelete = propertyDocument

                val confirmDialog = ConfirmBottomSheetFragment.newInstance(
                    icon = R.drawable.ic_confirm_delete_document,
                    title = TranslationInteractor.getTranslation("app.document.delete_document_question"),
                    description = TranslationInteractor.getTranslation("app.document.document_description"),
                    confirmText = TranslationInteractor.getTranslation("app.document.yes_delete"),
                    cancelText = TranslationInteractor.getTranslation("app.document.no_stay")
                )
                confirmDialog.show(childFragmentManager, ConfirmBottomSheetFragment.TAG)
            },
                onDocumentClick = { documentUrl ->
                    viewModel.onFileClick(documentUrl)
                }
            )

        binding.editDocumentListImageView.setOnDebouncedClickListener {
            val editDocumentListBottomSheetFragment =
                EditDocumentListBottomSheetFragment.newInstance()
            editDocumentListBottomSheetFragment.show(
                childFragmentManager,
                EditDocumentListBottomSheetFragment.TAG
            )
        }
        binding.listDocumentsRecyclerView.apply {
            adapter = documentListAdapter
        }
        subscribe(viewModel.propertyDocumentsObserver) { propertyItem ->

            binding.emptyDocListGroup.visibleIf(propertyItem.documents.isEmpty())
            binding.listDocumentsRecyclerView.visibleIf(propertyItem.documents.isNotEmpty())

            if (propertyItem.documents.isNotEmpty()) {
                documentListAdapter.setItems(
                    propertyItem.documents
                )
            } else {
                viewModel.changeDeleteButtonsVisibility(false)
            }
        }
        subscribe(viewModel.downloadFileObserver) {
            downloadFile(it)
        }

        subscribe(viewModel.isDeleteButtonVisibleObserver) {
            changeDeleteButtonVisibility(it)
        }

        subscribe(viewModel.requestReadExternalStoragePermObserver){
            requestReadStorageAction.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun changeDeleteButtonVisibility(isDeleteButtonVisible: Boolean) {
        binding.editDocumentListImageView.visibleIf(!isDeleteButtonVisible)
        binding.saveDocumentListImageView.visibleIf(isDeleteButtonVisible)
        documentListAdapter.showDeleteButton(isDeleteButtonVisible)
    }

    override fun addDocumentToList() {
        openPropertyUploadDocumentsScreen()
    }

    override fun onConfirmClick() {
        propertyDocumentForDelete?.let { viewModel.deleteDocument(it) }
    }

    private fun downloadFile(documentUrl: PropertyDocument) {
        val documentId = documentUrl.link.substringAfterLast("/", "missing")
        val url = "${BuildConfig.BASE_URL}/api/store/${documentId}"
        val request = DownloadManagerRequestProvider.makeDownloadManagerRequest(
            url,
            documentUrl.name,
            requireContext().getString(R.string.app_name),
            "Скачивание файла"
        )

        requireContext().registerReceiver(
            onDownloadCompleteReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

        downloadedFileId = downloadManager?.enqueue(request)
    }

    private fun openFile(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        requireContext().startActivity(intent)
        requireContext().unregisterReceiver(onDownloadCompleteReceiver)
    }

    private fun openPropertyUploadDocumentsScreen() {
        val propertyUploadFilesFragment = PropertyUploadDocumentsFragment()
        propertyUploadFilesFragment.show(childFragmentManager, propertyUploadFilesFragment.TAG)
    }
}
