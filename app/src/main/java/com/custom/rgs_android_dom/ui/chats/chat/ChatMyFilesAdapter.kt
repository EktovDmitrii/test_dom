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

class ChatMyFilesAdapter(
    private val onFileClick: (ChatFileModel) -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatFiles: ArrayList<ChatFileModel> = arrayListOf()

    companion object {
        private const val ITEM_TYPE_IMAGE = 1
        private const val ITEM_TYPE_DOCUMENT = 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatFiles[position]
        when (holder){
            is MyImageViewHolder -> {
                holder.bind(message)
            }
            is MyDocumentViewHolder -> {
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
                val binding = ItemChatImageMyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyImageViewHolder(binding, onFileClick)
            }
            ITEM_TYPE_DOCUMENT -> {
                val binding = ItemChatDocumentMyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyDocumentViewHolder(binding, onFileClick)
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


    inner class MyImageViewHolder(
        private val binding: ItemChatImageMyBinding,
        private val onFileClick: (ChatFileModel) -> Unit = {}
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatFileModel) {
            binding.myImageView.setImageBitmap(model.miniPreview)

            GlideApp.with(binding.myImageView.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(model.preview))
                .transform(CenterCrop(), RoundedCorners(16.dp(binding.myImageView.context)))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.myImageView)

            binding.dateTextView.text = model.createdAt.formatTo(DATE_PATTERN_TIME_ONLY_WITHOUT_SEC)

            binding.root.setOnDebouncedClickListener {
                onFileClick(model)
            }
        }
    }

    inner class MyDocumentViewHolder(
        private val binding: ItemChatDocumentMyBinding,
        private val onFileClick: (ChatFileModel) -> Unit = {}
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatFileModel) {
            binding.docNameTextView.text = model.name
            binding.sizeTextView.text = if (model.size.sizeInMb.roundTo(2) > 0.01) {
                "${model.size.sizeInMb.roundTo(2)}mb"
            } else {
                "0.01mb"
            }
            binding.dateTextView.text = model.createdAt.formatTo(DATE_PATTERN_TIME_ONLY_WITHOUT_SEC)

            binding.root.setOnDebouncedClickListener {
                onFileClick(model)
            }
        }
    }


}