package com.custom.rgs_android_dom.ui.chat

import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.*
import com.custom.rgs_android_dom.domain.chat.models.*
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.recycler_view.VerticalItemDecoration

class ChatAdapter(
    private val onFileClick: (ChatFileModel) -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatItems: ArrayList<ChatItemModel> = arrayListOf()

    companion object {
        private const val ITEM_TYPE_MY_MESSAGE = 1
        private const val ITEM_TYPE_OPPONENT_MESSAGE = 2
        private const val ITEM_TYPE_DATE_DIVIDER = 3
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatItems[position]

        when (holder) {
            is MyMessageViewHolder -> {
                holder.bind(message as ChatMessageModel)
            }
            is OpponentMessageViewHolder -> {
                holder.bind(message as ChatMessageModel)
            }
            is DateDividerViewHolder -> {
                holder.bind(message as ChatDateDividerModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (chatItems[position]) {
            is ChatMessageModel -> {
                if ((chatItems[position] as ChatMessageModel).sender == Sender.ME) {
                    ITEM_TYPE_MY_MESSAGE
                } else {
                    ITEM_TYPE_OPPONENT_MESSAGE
                }
            }
            is ChatDateDividerModel -> {
                ITEM_TYPE_DATE_DIVIDER
            }
            else -> {
                ITEM_TYPE_MY_MESSAGE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_MY_MESSAGE -> {
                val binding = ItemChatMessageMyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MyMessageViewHolder(binding, onFileClick)
            }
            ITEM_TYPE_OPPONENT_MESSAGE -> {
                val binding = ItemChatMessageOpponentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                OpponentMessageViewHolder(binding, onFileClick)
            }
            ITEM_TYPE_DATE_DIVIDER -> {
                val binding = ItemChatDateDividerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                DateDividerViewHolder(binding)
            }
            else -> {
                val binding = ItemChatMessageMyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MyMessageViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return chatItems.size
    }

    fun setItems(chatItems: List<ChatItemModel>) {
        this.chatItems.clear()
        this.chatItems.addAll(chatItems)
        notifyDataSetChanged()
    }

    fun addNewItems(chatItems: List<ChatItemModel>) {
        chatItems.forEach { chatItem ->
            this.chatItems.add(chatItem)
            notifyItemInserted(this.chatItems.size - 1)
        }

    }

    inner class MyMessageViewHolder(
        private val binding: ItemChatMessageMyBinding,
        private val onFileClick: (ChatFileModel) -> Unit = {}
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {

            drawItemBackground()

            val files = model.files
            if (files != null && files.isNotEmpty()) {

                binding.attachedFilesRecyclerView.visible()
                binding.messageContainerLinearLayout.gone()

                val adapter = ChatMyFilesAdapter() {
                    onFileClick(it)
                }

                binding.attachedFilesRecyclerView.adapter = adapter
                binding.attachedFilesRecyclerView.addItemDecoration(
                    VerticalItemDecoration(
                        gap = 12.dp(binding.attachedFilesRecyclerView.context)
                    )
                )
                adapter.setItems(files)
            } else {

                binding.attachedFilesRecyclerView.gone()
                binding.messageContainerLinearLayout.visible()

                binding.messageTextView.text = model.message
                binding.timeTextView.text =
                    model.createdAt.formatTo(DATE_PATTERN_TIME_ONLY_WITHOUT_SEC)
                Linkify.addLinks(binding.messageTextView, Linkify.ALL)
            }
        }

        private fun drawItemBackground() {

            val previousChatItemModel: ChatItemModel?
            val context = binding.messageContainerFrameLayout.context

            val allCornersTheSameRadiusDrawable = AppCompatResources.getDrawable(
                context,
                R.drawable.rectangle_filled_primary_500_radius_16dp
            )

            val topEndCornerWithDifferentRadiusDrawable = AppCompatResources.getDrawable(
                context,
                R.drawable.rectangle_filled_primary_500_radius_16dp_top_end_4dp
            )

            if (absoluteAdapterPosition > 0) {

                previousChatItemModel = chatItems[absoluteAdapterPosition - 1]

                if (previousChatItemModel is ChatMessageModel && previousChatItemModel.sender == Sender.OPPONENT
                    || (previousChatItemModel is ChatDateDividerModel)) {
                    binding.messageContainerFrameLayout.background = topEndCornerWithDifferentRadiusDrawable
                } else if (topEndCornerWithDifferentRadiusDrawable != null
                    && binding.messageContainerFrameLayout.background.constantState == topEndCornerWithDifferentRadiusDrawable.constantState) {
                    binding.messageContainerFrameLayout.background = allCornersTheSameRadiusDrawable
                }
            }
        }

    }

    inner class OpponentMessageViewHolder(
        private val binding: ItemChatMessageOpponentBinding,
        private val onFileClick: (ChatFileModel) -> Unit = {}
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {

            decorateItem()

            binding.nameTextView.text = "Мой_Сервис Дом"
            model.member?.let { member ->

                // TODO Replace with real position key (${member.type})
                binding.nameTextView.text = "${member.lastName} ${member.firstName}, консультант"

                if (member.avatar.isNotEmpty()) {
                    GlideApp.with(binding.avatarImageView)
                        .load(GlideUrlProvider.makeHeadersGlideUrl(member.avatar))
                        .circleCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_avatar_no_stroke)
                        .into(binding.avatarImageView)
                } else {
                    binding.avatarImageView.setImageResource(R.drawable.ic_chat_avatar_stub)
                }

            }
            val files = model.files
            if (files != null && files.isNotEmpty()) {

                binding.attachedFilesRecyclerView.visible()
                binding.messageContainerLinearLayout.gone()

                val adapter = ChatOpponentFilesAdapter() {
                    onFileClick(it)
                }

                binding.attachedFilesRecyclerView.adapter = adapter
                binding.attachedFilesRecyclerView.addItemDecoration(
                    VerticalItemDecoration(
                        gap = 12.dp(binding.attachedFilesRecyclerView.context)
                    )
                )
                adapter.setItems(files)

            } else {

                binding.attachedFilesRecyclerView.gone()
                binding.messageContainerLinearLayout.visible()

                binding.messageTextView.text = model.message

                binding.timeTextView.text = model.createdAt.formatTo(DATE_PATTERN_TIME_ONLY_WITHOUT_SEC)

                Linkify.addLinks(binding.messageTextView, Linkify.ALL)
            }
        }

        private fun decorateItem() {

            val previousChatItemModel: ChatItemModel?
            val context = binding.messageContainerFrameLayout.context

            val allCornersTheSameRadiusDrawable = AppCompatResources.getDrawable(
                context,
                R.drawable.rectangle_filled_secondary_100_radius_16dp
            )

            val topStartCornerWithDifferentRadiusDrawable = AppCompatResources.getDrawable(
                context,
                R.drawable.rectangle_filled_secondary_100_radius_16dp_top_start_radius_4dp
            )

            if (absoluteAdapterPosition > 0) {

                previousChatItemModel = chatItems[absoluteAdapterPosition - 1]

                if (previousChatItemModel is ChatMessageModel && previousChatItemModel.sender == Sender.ME
                    || (previousChatItemModel is ChatDateDividerModel)) {
                    binding.messageContainerFrameLayout.background = topStartCornerWithDifferentRadiusDrawable
                } else if (topStartCornerWithDifferentRadiusDrawable != null &&
                    binding.messageContainerFrameLayout.background.constantState == topStartCornerWithDifferentRadiusDrawable.constantState) {
                    binding.messageContainerFrameLayout.background = allCornersTheSameRadiusDrawable
                }

                if (previousChatItemModel is ChatMessageModel){
                    binding.avatarImageView.invisibleIf(previousChatItemModel.sender == Sender.OPPONENT)
                    binding.nameTextView.visibleIf(previousChatItemModel.sender == Sender.ME)

                    val topMargin = if (previousChatItemModel.sender == Sender.ME) 12.dp(context) else 4.dp(context)
                    binding.root.setMargins(top = topMargin)
                }
            } else {
                binding.avatarImageView.visible()
                binding.nameTextView.visible()

                val topMargin = 12.dp(context)
                binding.root.setMargins(top = topMargin)
            }
        }
    }

    inner class DateDividerViewHolder(private val binding: ItemChatDateDividerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatDateDividerModel) {
            binding.dateTextView.text = model.date
        }
    }

}