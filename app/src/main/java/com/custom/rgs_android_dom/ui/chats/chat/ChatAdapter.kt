package com.custom.rgs_android_dom.ui.chats.chat

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
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
    private val onWidgetProductClick: (widget: WidgetModel.WidgetOrderProductModel) -> Unit,
    private val onWidgetAdditionalInvoiceClick: (WidgetModel.WidgetAdditionalInvoiceModel) -> Unit,
    private val onWidgetOrderDefaultProductClick: (widget: WidgetModel.WidgetOrderDefaultProductModel) -> Unit,
    private val onWidgetOrderComplexProductClick: (widget: WidgetModel.WidgetOrderComplexProductModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatItems: ArrayList<ChatItemModel> = arrayListOf()

    companion object {
        private const val ITEM_TYPE_MY_MESSAGE = 1
        private const val ITEM_TYPE_OPPONENT_MESSAGE = 2
        private const val ITEM_TYPE_DATE_DIVIDER = 3
        private const val ITEM_TYPE_WIDGET_PRODUCT = 4
        private const val ITEM_TYPE_WIDGET_ADDITIONAL_INVOICE = 5
        private const val ITEM_TYPE_WIDGET_ORDER_DEFAULT_PRODUCT = 6
        private const val ITEM_TYPE_WIDGET_ORDER_COMPLEX_PRODUCT = 7

        private const val OBJECT_TYPE_APARTMENT = "apartment"

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
            is WidgetOrderDefaultProductViewHolder -> {
                holder.bind(message as ChatMessageModel)
            }
            is WidgetOrderComplexProductViewHolder -> {
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
                    if (model.widget == null) {
                        ITEM_TYPE_OPPONENT_MESSAGE
                    } else {
                        when (model.widget.widgetType) {
                            WidgetType.GeneralInvoicePayment -> ITEM_TYPE_WIDGET_ADDITIONAL_INVOICE
                            WidgetType.Product -> ITEM_TYPE_WIDGET_PRODUCT
                            WidgetType.OrderDefaultProduct -> ITEM_TYPE_WIDGET_ORDER_DEFAULT_PRODUCT
                            WidgetType.OrderComplexProduct -> ITEM_TYPE_WIDGET_ORDER_COMPLEX_PRODUCT
                        }
                    }
                }
            }
            is ChatDateDividerModel -> ITEM_TYPE_DATE_DIVIDER
            else -> ITEM_TYPE_MY_MESSAGE
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
                WidgetProductViewHolder(binding, onWidgetProductClick)
            }
            ITEM_TYPE_WIDGET_ADDITIONAL_INVOICE -> {
                val binding = ItemChatWidgetAdditionalInvoiceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WidgetAdditionalInvoiceViewHolder(binding,onWidgetAdditionalInvoiceClick)
            }
            ITEM_TYPE_WIDGET_ORDER_DEFAULT_PRODUCT -> {
                val binding = ItemChatWidgetOrderDefaultProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WidgetOrderDefaultProductViewHolder(binding,onWidgetOrderDefaultProductClick)
            }
            ITEM_TYPE_WIDGET_ORDER_COMPLEX_PRODUCT -> {
                val binding = ItemChatWidgetOrderComplexProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WidgetOrderComplexProductViewHolder(binding,onWidgetOrderComplexProductClick)
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
        private val onWidgetProductClick: (WidgetModel.WidgetOrderProductModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {
            val widget = model.widget as WidgetModel.WidgetOrderProductModel
            binding.productNameTextView.text = widget.name
            binding.priceTextView.text = widget.price?.amount?.simplePriceFormat()

            val avatar = widget.avatar
            if (!avatar.isNullOrEmpty()) {
                GlideApp.with(binding.productImageView.context)
                    .load(GlideUrlProvider.makeHeadersGlideUrl(avatar))
                    .transform(CenterCrop(), RoundedCorners(8.dp(binding.productImageView.context)))
                    .into(binding.productImageView)
            }

            binding.toProductLinearLayout.setOnDebouncedClickListener {
                onWidgetProductClick(widget)
            }
        }
    }

    inner class WidgetAdditionalInvoiceViewHolder(
        private val binding: ItemChatWidgetAdditionalInvoiceBinding,
        private val onWidgetAdditionalInvoiceClick: (WidgetModel.WidgetAdditionalInvoiceModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {
            val widget = model.widget as WidgetModel.WidgetAdditionalInvoiceModel
            binding.serviceNameTextView.text = widget.serviceName
            val serviceLogo = widget.serviceLogo
            if (!serviceLogo.isNullOrEmpty()) {
                GlideApp.with(binding.serviceImageView.context)
                    .load(GlideUrlProvider.makeHeadersGlideUrl(serviceLogo))
                    .transform(CenterCrop(), RoundedCorners(8.dp(binding.serviceImageView.context)))
                    .into(binding.serviceImageView)
            }

            binding.totalPriceTextView.text = widget.amount?.simplePriceFormat() ?: ""
            binding.amountToPayTextView.text = widget.amount?.simplePriceFormat() ?: ""
            val adapter = WidgetAdditionalInvoiceItemsAdapter()
            binding.additionalItemsRecyclerView.adapter = adapter
            widget.items?.let { adapter.setItems(it) }

            binding.orderLinearLayout.setOnDebouncedClickListener {
                onWidgetAdditionalInvoiceClick(widget)
            }
        }
    }

    inner class WidgetOrderDefaultProductViewHolder(
        private val binding: ItemChatWidgetOrderDefaultProductBinding,
        private val onWidgetOrderDefaultProductClick: (widget: WidgetModel.WidgetOrderDefaultProductModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {
            val widget = model.widget as WidgetModel.WidgetOrderDefaultProductModel
            if (widget.objId != null) {
                binding.propertyLinearLayout.visible()
                binding.propertyNameTextView.text = widget.objName
                binding.addressTextView.text = widget.objAddr
            } else {
                binding.propertyLinearLayout.gone()
            }
            binding.priceTextView.text = widget.price?.formatPriceGroupedByThousands()
            binding.amountToPayTextView.text = widget.price?.formatPriceGroupedByThousands()
            binding.timeTextView.text = widget.orderTime?.formatOrderTime()
            binding.dateTextView.text = widget.orderDate?.formatTo(DATE_PATTERN_DATE_FULL_MONTH)
            binding.serviceNameTextView.text = widget.name
            binding.durationTextView.text = widget.deliveryTime

            val icon = widget.icon
            if (!icon.isNullOrEmpty()) {
                GlideApp.with(binding.serviceImageView.context)
                    .load(GlideUrlProvider.makeHeadersGlideUrl(icon))
                    .transform(CenterCrop(), RoundedCorners(8.dp(binding.serviceImageView.context)))
                    .into(binding.serviceImageView)
            }

            val propertyIcon = widget.objPhotoLink
            if (!propertyIcon.isNullOrEmpty()) {
                GlideApp.with(binding.propertyImageView.context)
                    .load(GlideUrlProvider.makeHeadersGlideUrl(propertyIcon))
                    .transform(CenterCrop(), RoundedCorners(8.dp(binding.root.context)))
                    .into(binding.propertyImageView)
            } else {
                var iconResId = R.drawable.ic_type_home
                if (widget.objType == OBJECT_TYPE_APARTMENT) {
                    iconResId = R.drawable.ic_apartment_240px
                }
                binding.propertyImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,iconResId))
            }
            binding.orderLinearLayout.setOnDebouncedClickListener {
                onWidgetOrderDefaultProductClick(widget)
            }
        }

    }

    inner class WidgetOrderComplexProductViewHolder(
        private val binding: ItemChatWidgetOrderComplexProductBinding,
        private val onWidgetOrderComplexProductClick: (widget: WidgetModel.WidgetOrderComplexProductModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {
            val widget = model.widget as WidgetModel.WidgetOrderComplexProductModel
            binding.serviceNameTextView.text = widget.name
            binding.durationTextView.text = widget.deliveryTime?.formatDeliveryTime()
            binding.priceTextView.text = 0.simplePriceFormat()

            if (widget.objId != null) {
                binding.propertyLinearLayout.visible()
                binding.propertyNameTextView.text = widget.objName
                binding.addressTextView.text = widget.objAddr
            } else {
                binding.propertyLinearLayout.gone()
            }
            binding.timeTextView.text = widget.orderTime?.formatOrderTime()
            binding.dateTextView.text = widget.orderDate?.formatTo(DATE_PATTERN_DATE_FULL_MONTH)

            val propertyIcon = widget.objPhotoLink
            if (!propertyIcon.isNullOrEmpty()) {
                GlideApp.with(binding.propertyImageView.context)
                    .load(GlideUrlProvider.makeHeadersGlideUrl(propertyIcon))
                    .transform(CenterCrop(), RoundedCorners(8.dp(binding.root.context)))
                    .into(binding.propertyImageView)
            } else {
                var iconResId = R.drawable.ic_type_home
                if (widget.objType == OBJECT_TYPE_APARTMENT) {
                    iconResId = R.drawable.ic_apartment_240px
                }
                binding.propertyImageView.setImageDrawable(ContextCompat.getDrawable(binding.root.context,iconResId))
            }

            val icon = widget.icon
            if (!icon.isNullOrEmpty()) {
                GlideApp.with(binding.serviceImageView.context)
                    .load(GlideUrlProvider.makeHeadersGlideUrl(icon))
                    .transform(CenterCrop(), RoundedCorners(8.dp(binding.serviceImageView.context)))
                    .into(binding.serviceImageView)
            }
            binding.orderLinearLayout.setOnDebouncedClickListener {
                onWidgetOrderComplexProductClick(widget)
            }

        }
    }

}
