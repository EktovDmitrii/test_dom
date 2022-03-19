package com.custom.rgs_android_dom.ui.chats.chat

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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.DownloadManagerRequestProvider
import com.custom.rgs_android_dom.databinding.FragmentChatBinding
import com.custom.rgs_android_dom.domain.chat.models.CaseModel
import com.custom.rgs_android_dom.domain.chat.models.ChatFileModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.chats.chat.files.upload.UploadFilesFragment
import com.custom.rgs_android_dom.utils.*
import com.yandex.metrica.YandexMetrica
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class ChatFragment : BaseBottomSheetFragment<ChatViewModel, FragmentChatBinding>() {

    companion object{
        private const val UP_DIRECTION = 1
        private const val ARG_CASE = "ARG_CASE"

        fun newInstance(case: CaseModel): ChatFragment {
            return ChatFragment().args {
                putSerializable(ARG_CASE, case)
            }
        }

    }

    override val TAG: String = "CHAT_FRAGMENT"

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

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_CASE) as CaseModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        downloadManager = requireContext().getDownloadManager()

        val layoutManager = binding.messagesRecyclerView.layoutManager as LinearLayoutManager
        layoutManager.stackFromEnd = true
        binding.messagesRecyclerView.layoutManager = layoutManager

        binding.messagesRecyclerView.adapter = ChatAdapter(
            {
                viewModel.onFileClick(it)
                hideSoftwareKeyboard()
            },
            {
               viewModel.onProductClick(it)
                hideSoftwareKeyboard()
            },
            {
                if (!it.paymentUrl.isNullOrEmpty()) {
                    viewModel.onWidgetPayClick(it)
                }
                hideSoftwareKeyboard()
            },
            {
                viewModel.orderDefaultProduct(it)
                hideSoftwareKeyboard()
            },
            {
                viewModel.orderProductService(it)
                hideSoftwareKeyboard()
            }
        )

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.messageEditText.addTextChangedListener {
            viewModel.onUserTyping()
            binding.sendMessageImageView.isEnabled = it.toString().trim().isNotEmpty()
        }

        binding.sendMessageImageView.setOnDebouncedClickListener {
            viewModel.onSendMessageClick(binding.messageEditText.text.toString().trim())
            binding.messageEditText.text?.clear()
            hideSoftwareKeyboard()
        }

        binding.attachImageView.setOnDebouncedClickListener {
            val uploadFilesFragment = UploadFilesFragment()
            uploadFilesFragment.show(childFragmentManager, uploadFilesFragment.TAG)
        }

        binding.audioCallImageView.setOnDebouncedClickListener {
            viewModel.onAudioCallClick()
        }

        binding.videoCallImageView.setOnDebouncedClickListener {
            viewModel.onVideoCallClick()
        }

        binding.scrollDownImageView.setOnDebouncedClickListener {
            binding.messagesRecyclerView.smoothScrollToPosition(chatAdapter.itemCount - 1)
        }

        binding.messagesRecyclerView.addOnScrollListener ( object:OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 5 && ! binding.scrollDownImageView.isActivated) {
                    binding.scrollDownImageView.isActivated = true
                } else if (dy < 0 && binding.scrollDownImageView.isActivated) {
                    binding.scrollDownImageView.isActivated = false
                } else if (!recyclerView.canScrollVertically(UP_DIRECTION) && binding.scrollDownImageView.isActivated) {
                    binding.scrollDownImageView.isActivated = false
                }
            }

        })

        subscribe(viewModel.chatItemsObserver){
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

        subscribe(viewModel.caseObserver){
            binding.titleTextView.text = it.name

            binding.audioCallImageView.goneIf(it.isArchived)
            binding.videoCallImageView.goneIf(it.isArchived)
            binding.sendMessageBottomAppBar.goneIf(it.isArchived)

            binding.archivedFrameLayout.visibleIf(it.isArchived)

        }
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

    override fun isNavigationViewVisible(): Boolean {
        return false
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

    override fun onClose() {
        super.onClose()
        YandexMetrica.reportEvent("chat_end")
        viewModel.onChatClose()
    }

    private fun downloadFile(chatFile: ChatFileModel) {
        val url = "${BuildConfig.BASE_URL}/api/chat/users/me/files/${chatFile.id}"
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