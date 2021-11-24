package com.custom.rgs_android_dom.ui.chat

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.DownloadManagerRequestProvider
import com.custom.rgs_android_dom.databinding.FragmentChatBinding
import com.custom.rgs_android_dom.domain.chat.models.ChatFileModel
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.chat.files.upload.UploadFilesFragment
import com.custom.rgs_android_dom.utils.*

class ChatFragment : BaseFragment<ChatViewModel, FragmentChatBinding>(R.layout.fragment_chat) {

    private val chatAdapter: ChatAdapter
        get() = binding.messagesRecyclerView.adapter as ChatAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        downloadManager = requireContext().getDownloadManager()

        val layoutManager = binding.messagesRecyclerView.layoutManager as LinearLayoutManager
        layoutManager.stackFromEnd = true
        binding.messagesRecyclerView.layoutManager = layoutManager

        binding.messagesRecyclerView.adapter = ChatAdapter() {
            viewModel.onFileClick(it)
        }

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.messageEditText.addTextChangedListener {
            binding.sendMessageImageView.isEnabled = it.toString().isNotEmpty()
        }

        binding.sendMessageImageView.setOnDebouncedClickListener {
            viewModel.onSendMessageClick(binding.messageEditText.text.toString().trim())
            binding.messageEditText.text?.clear()
        }

        binding.attachImageView.setOnDebouncedClickListener {
            val uploadFilesFragment = UploadFilesFragment()
            uploadFilesFragment.show(childFragmentManager, uploadFilesFragment.TAG)
        }

        subscribe(viewModel.chatItemsObserver) {
            chatAdapter.setItems(it)
            binding.messagesRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
        }

        subscribe(viewModel.newItemsObserver) {
            chatAdapter.addNewItems(it)
            binding.messagesRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
        }

        subscribe(viewModel.networkErrorObserver) {
            toast(it)
        }

        subscribe(viewModel.downloadFileObserver) {
            downloadFile(it)
        }

    }

    override fun onLoading() {
        super.onLoading()
        binding.loadingProgressBar.visible()
        binding.sendMessageBottomAppBar.gone()
    }

    override fun onContent() {
        super.onContent()
        binding.loadingProgressBar.gone()
        binding.sendMessageBottomAppBar.visible()
    }

    override fun onError() {
        super.onError()
        binding.loadingProgressBar.gone()
        binding.sendMessageBottomAppBar.visible()
    }

    private fun downloadFile(chatFile: ChatFileModel) {
        val url = "${BuildConfig.BASE_URL}/api/chat/users/${chatFile.senderId}/files/${chatFile.id}"
        val request = DownloadManagerRequestProvider.makeDownloadManagerRequest(
            url,
            chatFile.name,
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

}