package com.custom.rgs_android_dom.ui.chat

import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.*
import com.custom.rgs_android_dom.domain.chat.models.*
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.recycler_view.VerticalItemDecoration

class ChatAdapter(
    private val onFileClick: (ChatFileModel) -> Unit = {},
    private val onProductClick: (productId: String?) -> Unit,
    private val onPayClick: (String, String, Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatItems: ArrayList<ChatItemModel> = arrayListOf()

    companion object {
        private const val ITEM_TYPE_MY_MESSAGE = 1
        private const val ITEM_TYPE_OPPONENT_MESSAGE = 2
        private const val ITEM_TYPE_DATE_DIVIDER = 3
        private const val ITEM_TYPE_WIDGET_PRODUCT = 4
        private const val ITEM_TYPE_WIDGET_ADDITIONAL_INVOICE = 5

        private const val WIDGET_TYPE_ADDITIONAL_INVOICE = "generalinvoicepayment"
        private const val WIDGET_TYPE_PRODUCT = "product"
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
            is WidgetProductViewHolder -> {
                holder.bind(message as ChatMessageModel)
            }
            is WidgetAdditionalInvoiceViewHolder -> {
                holder.bind(message as ChatMessageModel)
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val model = chatItems[position]) {
            is ChatMessageModel -> {
                if (model.sender == Sender.ME) {
                    ITEM_TYPE_MY_MESSAGE
                } else {
                    if (model.widget != null) {
                        when (model.widget.widgetType) {
                            WIDGET_TYPE_ADDITIONAL_INVOICE -> ITEM_TYPE_WIDGET_ADDITIONAL_INVOICE
                            WIDGET_TYPE_PRODUCT -> ITEM_TYPE_WIDGET_PRODUCT
                            else -> -1
                        }

                    } else {
                        ITEM_TYPE_OPPONENT_MESSAGE
                    }
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
            ITEM_TYPE_WIDGET_PRODUCT -> {
                val binding = ItemChatWidgetOrderProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WidgetProductViewHolder(binding, onProductClick)
            }
            ITEM_TYPE_WIDGET_ADDITIONAL_INVOICE -> {
                val binding = ItemChatWidgetAdditionalInvoiceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WidgetAdditionalInvoiceViewHolder(binding,onPayClick)
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

            decorateItem()

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

        private fun decorateItem() {

            val previousChatItemModel: ChatItemModel?
            val context = binding.messageContainerFrameLayout.context
            val marginTopToDateDivider = 22
            val marginTopToSenderMe = 4
            val marginTopToSenderOpponent = 24

            val allCornersTheSameRadiusDrawable = AppCompatResources.getDrawable(
                context,
                R.drawable.rectangle_filled_primary_500_radius_16dp
            )

            val topEndCornerWithDifferentRadiusDrawable = AppCompatResources.getDrawable(
                context,
                R.drawable.rectangle_filled_primary_500_radius_16dp_top_end_4dp
            )

            previousChatItemModel = chatItems[absoluteAdapterPosition - 1]

            if (previousChatItemModel is ChatMessageModel && previousChatItemModel.sender == Sender.OPPONENT
                || (previousChatItemModel is ChatDateDividerModel)
            ) {

                when (previousChatItemModel) {
                    is ChatMessageModel -> {
                        binding.myMessageContainerRelativeLayout.setMargins(top = marginTopToSenderOpponent)
                    }
                    is ChatDateDividerModel -> {
                        binding.myMessageContainerRelativeLayout.setMargins(top = marginTopToDateDivider)
                    }
                }
                binding.messageContainerFrameLayout.background = topEndCornerWithDifferentRadiusDrawable
            } else {
                binding.myMessageContainerRelativeLayout.setMargins(top = marginTopToSenderMe)
                binding.messageContainerFrameLayout.background = allCornersTheSameRadiusDrawable
            }
        }

    }

    inner class OpponentMessageViewHolder(
        private val binding: ItemChatMessageOpponentBinding,
        private val onFileClick: (ChatFileModel) -> Unit = {}
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {
            decorateItem()
            val member = model.member

            if (member == null) {
                binding.nameTextView.text = "Мой_Сервис Дом"
                binding.avatarImageView.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        binding.avatarImageView.resources,
                        R.drawable.ic_chat_avatar_stub,
                        null
                    )
                )
            } else {
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
                    binding.avatarImageView.setImageResource(R.drawable.ic_avatar_no_stroke)
                }
            }

            val files = model.files
            if (files != null && files.isNotEmpty()) {

                binding.attachedFilesContainerFrameLayout.visible()
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

                binding.attachedFilesContainerFrameLayout.gone()
                binding.messageContainerLinearLayout.visible()

                binding.messageTextView.text = model.message

                binding.timeTextView.text = model.createdAt.formatTo(DATE_PATTERN_TIME_ONLY_WITHOUT_SEC)

                Linkify.addLinks(binding.messageTextView, Linkify.ALL)
            }
        }

        private fun decorateItem() {

            val previousChatItemModel: ChatItemModel?
            val context = binding.messageContainerFrameLayout.context
            val marginTopToDateDivider = 22
            val marginTopToSenderMe = 30
            val marginTopToSenderOpponent = 4

            val allCornersTheSameRadiusDrawable = AppCompatResources.getDrawable(
                context,
                R.drawable.rectangle_filled_secondary_100_radius_16dp
            )

            val topStartCornerWithDifferentRadiusDrawable = AppCompatResources.getDrawable(
                context,
                R.drawable.rectangle_filled_secondary_100_radius_16dp_top_start_radius_4dp
            )

            val currentChatItemModel = chatItems[absoluteAdapterPosition]
            previousChatItemModel = chatItems[absoluteAdapterPosition - 1]

            if (previousChatItemModel is ChatMessageModel && previousChatItemModel.sender == Sender.ME
                || (previousChatItemModel is ChatDateDividerModel)
                || (previousChatItemModel as ChatMessageModel).type != (currentChatItemModel as ChatMessageModel).type
            ) {
                when (previousChatItemModel) {
                    is ChatMessageModel -> {
                        binding.opponentContainerLinearLayout.setMargins(top = marginTopToSenderMe)
                    }
                    is ChatDateDividerModel -> {
                        binding.opponentContainerLinearLayout.setMargins(top = marginTopToDateDivider)
                    }
                }
                binding.avatarImageView.visible()
                binding.nameTextView.visible()
                binding.messageContainerFrameLayout.background = topStartCornerWithDifferentRadiusDrawable
            } else {
                binding.opponentContainerLinearLayout.setMargins(top = marginTopToSenderOpponent)
                binding.avatarImageView.invisible()
                binding.nameTextView.gone()
                binding.messageContainerFrameLayout.background = allCornersTheSameRadiusDrawable
            }
        }
    }

    inner class DateDividerViewHolder(private val binding: ItemChatDateDividerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatDateDividerModel) {
            binding.dateTextView.text = model.date
        }
    }

    inner class WidgetProductViewHolder(
        private val binding: ItemChatWidgetOrderProductBinding,
        private val noProductClick: (productId: String?) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {

            binding.productNameTextView.text = model.widget?.name
            binding.priceTextView.text = model.widget?.price?.amount?.simplePriceFormat()

            val avatar = model.widget?.avatar
            if (!avatar.isNullOrEmpty()) {
                GlideApp.with(binding.productImageView.context)
                    .load(GlideUrlProvider.makeHeadersGlideUrl(avatar))
                    .transform(CenterCrop(), RoundedCorners(8.dp(binding.productImageView.context)))
                    .into(binding.productImageView)
            }

            binding.toProductLinearLayout.setOnDebouncedClickListener {
                noProductClick(model.widget?.productId)
            }
        }
    }

    inner class WidgetAdditionalInvoiceViewHolder(
        private val binding: ItemChatWidgetAdditionalInvoiceBinding,
        private val onPayClick: (String,String,Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {

            binding.serviceNameTextView.text = model.widget?.serviceName

            val serviceLogo = model.widget?.serviceLogo
            if (!serviceLogo.isNullOrEmpty()) {
                GlideApp.with(binding.serviceImageView.context)
                    .load(GlideUrlProvider.makeHeadersGlideUrl(serviceLogo))
                    .transform(CenterCrop(), RoundedCorners(8.dp(binding.serviceImageView.context)))
                    .into(binding.serviceImageView)
            }

            binding.totalPriceTextView.text = model.widget?.amount?.simplePriceFormat() ?: ""
            binding.amountToPayTextView.text = model.widget?.amount?.simplePriceFormat() ?: ""
            val adapter = WidgetAdditionalInvoiceItemsAdapter()
            binding.additionalItemsRecyclerView.adapter = adapter
            model.widget?.items?.let { adapter.setItems(it) }

            binding.orderLinearLayout.setOnDebouncedClickListener {
                Log.d("Syrgashev", "orderLinearLayout clicked: ")
                onPayClick(
                    model.widget?.invoiceId ?: "",
                    model.widget?.productId ?: "",
                    model.widget?.amount ?: 0
                )
            }
        }
    }

}