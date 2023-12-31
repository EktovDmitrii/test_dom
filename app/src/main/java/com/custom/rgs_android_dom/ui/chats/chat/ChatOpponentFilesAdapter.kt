package com.custom.rgs_android_dom.ui.chats.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.*
import com.custom.rgs_android_dom.domain.chat.models.*
import com.custom.rgs_android_dom.utils.*

class ChatOpponentFilesAdapter(
    private val onFileClick: (ChatFileModel) -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatFiles: ArrayList<ChatFileModel> = arrayListOf()

    companion object {
        private const val ITEM_TYPE_IMAGE = 1
        private const val ITEM_TYPE_DOCUMENT = 2
        private const val MIN_READABLE_FILE_SIZE = 0.01
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatFiles[position]
        when (holder){
            is OpponentImageViewHolder -> {
                holder.bind(message)
            }
            is OpponentDocumentViewHolder -> {
                holder.bind(message)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (chatFiles[position].hasPreviewImage) ITEM_TYPE_IMAGE else ITEM_TYPE_DOCUMENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_IMAGE -> {
                val binding = ItemChatImageOpponentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OpponentImageViewHolder(binding, onFileClick)
            }
            ITEM_TYPE_DOCUMENT -> {
                val binding = ItemChatDocumentOpponentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OpponentDocumentViewHolder(binding, onFileClick)
            }
            else -> {
                throw Exception("Unknown view holder")
            }
        }
    }

    override fun getItemCount(): Int {
        return chatFiles.size
    }

    fun setItems(chatFiles: List<ChatFileModel>){
        this.chatFiles.clear()
        this.chatFiles.addAll(chatFiles)
        notifyDataSetChanged()
    }


    inner class OpponentImageViewHolder(
        private val binding: ItemChatImageOpponentBinding,
        private val onFileClick: (ChatFileModel) -> Unit = {}
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatFileModel) {
            binding.opponentImageView.setImageBitmap(model.miniPreview)

            GlideApp.with(binding.opponentImageView.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(model.preview))
                .transform(CenterCrop(), RoundedCorners(16.dp(binding.opponentImageView.context)))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.opponentImageView)

            binding.dateTextView.text = model.createdAt.formatTo(DATE_PATTERN_TIME_ONLY_WITHOUT_SEC)

            binding.root.setOnDebouncedClickListener {
                onFileClick(model)
            }
        }
    }

    inner class OpponentDocumentViewHolder(
        private val binding: ItemChatDocumentOpponentBinding,
        private val onFileClick: (ChatFileModel) -> Unit = {}
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatFileModel) {
            binding.docNameTextView.text = model.name
            binding.sizeTextView.text = if (model.size.sizeInMb.roundTo(2) > MIN_READABLE_FILE_SIZE) {
                "${model.size.sizeInMb.roundTo(2)}mb"
            } else {
                "${MIN_READABLE_FILE_SIZE}mb"
            }
            binding.dateTextView.text = model.createdAt.formatTo(DATE_PATTERN_TIME_ONLY_WITHOUT_SEC)

            binding.root.setOnDebouncedClickListener {
                onFileClick(model)
            }
        }
    }


}