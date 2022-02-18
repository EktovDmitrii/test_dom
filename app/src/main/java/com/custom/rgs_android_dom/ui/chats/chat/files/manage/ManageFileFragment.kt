package com.custom.rgs_android_dom.ui.chats.chat.files.manage

import android.app.DownloadManager
import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.DownloadManagerRequestProvider
import com.custom.rgs_android_dom.databinding.FragmentManageFileBinding
import com.custom.rgs_android_dom.domain.chat.models.ChatFileModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.getDownloadManager
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class ManageFileFragment() : BaseBottomSheetModalFragment<ManageFileViewModel, FragmentManageFileBinding>() {

    override val TAG: String = "MANAGE_FILES_FRAGMENT"

    companion object {
        private const val ARG_FILE = "ARG_FILE"

        fun newInstance(chatFile: ChatFileModel): ManageFileFragment {
            return ManageFileFragment().args {
                putSerializable(ARG_FILE, chatFile)
            }
        }

    }

    private var downloadManager: DownloadManager? = null

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_FILE) as ChatFileModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        downloadManager = requireContext().getDownloadManager()

        subscribe(viewModel.downloadTextObserver){
            binding.downloadTextView.text = it
        }

        subscribe(viewModel.downloadFileObserver){
            downloadFile(it)
        }

        binding.downloadTextView.setOnDebouncedClickListener {
            viewModel.onDownloadClick()
        }

        binding.deleteTextView.setOnDebouncedClickListener {
            dismissAllowingStateLoss()
        }
    }

    private fun downloadFile(chatFile: ChatFileModel){
        val url = "${BuildConfig.BASE_URL}/api/chat/users/me/files/${chatFile.id}"
        val request = DownloadManagerRequestProvider.makeDownloadManagerRequest(
            url,
            chatFile.name,
            requireContext().getString(R.string.app_name),
            "Скачивание файла"
        )

        downloadManager?.enqueue(request)

        dismissAllowingStateLoss()
    }

}