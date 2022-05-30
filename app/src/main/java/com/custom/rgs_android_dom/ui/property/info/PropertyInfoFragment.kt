package com.custom.rgs_android_dom.ui.property.info

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
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentPropertyInfoBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.property.add.details.files.PropertyUploadDocumentsFragment
import com.custom.rgs_android_dom.ui.property.info.more.PropertyMoreFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PropertyInfoFragment : BaseBottomSheetFragment<PropertyInfoViewModel, FragmentPropertyInfoBinding>() {

    companion object {
        private const val ARG_OBJECT_ID = "ARG_OBJECT_ID"

        fun newInstance(objectId: String): PropertyInfoFragment {
            return PropertyInfoFragment().args {
                putString(ARG_OBJECT_ID, objectId)
            }
        }
    }

    override val TAG: String = "PROPERTY_INFO_FRAGMENT"

    private val adapter: PropertyDocumentsAdapter
        get() = binding.documentsRecyclerView.adapter as PropertyDocumentsAdapter

    private var downloadManager: DownloadManager? = null

    private var downloadedFileId: Long? = null

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

        binding.documentsRecyclerView.adapter = PropertyDocumentsAdapter(
            onAddClick = {
                val propertyUploadFilesFragment = PropertyUploadDocumentsFragment()
                propertyUploadFilesFragment.show(childFragmentManager, propertyUploadFilesFragment.TAG)
            },
            onDocumentClick = {
                viewModel.onDocumentClick(it)
            }
        )

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.disposeAll()
            onClose()
        }

        binding.moreImageView.setOnDebouncedClickListener {
            viewModel.onMoreClick()
        }

        subscribe(viewModel.propertyItemObserver) { propertyItem ->
            when (propertyItem.type) {
                PropertyType.HOUSE -> {
                    binding.propertyImageView.setImageResource(R.drawable.ic_type_home)
                    binding.typeTextView.setValue(TranslationInteractor.getTranslation("app.object.main.info.property_type.house"))
                }
                PropertyType.APARTMENT -> {
                    binding.propertyImageView.setImageResource(R.drawable.ic_type_apartment_334px)
                    binding.typeTextView.setValue(TranslationInteractor.getTranslation("app.object.main.info.property_type.appartment"))
                }
            }
            binding.titleTextView.text = propertyItem.name
            binding.subtitleTextView.text = propertyItem.address?.address ?: ""
            binding.addressTextView.setValue(propertyItem.address?.address ?: "")
            propertyItem.photoLink?.let{
                GlideApp.with(requireContext())
                    .load(GlideUrlProvider.makeHeadersGlideUrl(it))
                    .into(binding.propertyImageView)
            }

            propertyItem.isOwn?.let { isOwn ->
                binding.isOwnTextView.setValue(if (isOwn) {
                    TranslationInteractor.getTranslation("app.object_detail.yes")
                }else{
                    TranslationInteractor.getTranslation("app.object_detail.no")
                })
            }
            propertyItem.isRent?.let { isRent ->
                binding.isRentTextView.setValue(if (isRent) {
                    TranslationInteractor.getTranslation("app.object_detail.yes")
                } else {
                    TranslationInteractor.getTranslation("app.object_detail.no")
                })
            }
            propertyItem.isTemporary?.let { isTemporary ->
                binding.isTemporaryTextView.setValue(if (isTemporary) {
                    TranslationInteractor.getTranslation("app.object_detail.yes")
                }
                else {
                    TranslationInteractor.getTranslation("app.object_detail.no")
                })
            }

            propertyItem.totalArea?.let { totalArea ->
                binding.totalAreaTextView.setValue("$totalArea м²")
            }
            if (propertyItem.comment.isNotEmpty()) {
                binding.commentTextView.setValue(propertyItem.comment)
            }

            adapter.setItems(propertyItem.documents)

            binding.allDocumentsTextView.setOnDebouncedClickListener {
                viewModel.onShowAllDocumentsClick()
            }
        }

        subscribe(viewModel.internetConnectionObserver) {
            adapter.onInternetConnectionChanged(it)
        }

        subscribe(viewModel.propertyMoreObserver){
            val propertyMoreFragment = PropertyMoreFragment.newInstance(it)
            propertyMoreFragment.show(childFragmentManager, propertyMoreFragment.TAG)
        }

        subscribe(viewModel.downloadFileObserver) {
            downloadFile(it)
        }

        subscribe(viewModel.requestReadExternalStoragePermObserver){
            requestReadStorageAction.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

    override fun isNavigationViewVisible(): Boolean {
        return false
    }

    private fun openFile(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        requireContext().startActivity(intent)
        requireContext().unregisterReceiver(onDownloadCompleteReceiver)
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

}
