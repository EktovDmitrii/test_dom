package com.custom.rgs_android_dom.ui.purchase_service

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ItemPurchaseCardBinding
import com.custom.rgs_android_dom.databinding.ItemPurchaseNewCardBinding
import com.custom.rgs_android_dom.domain.purchase_service.model.SavedCardModel
import com.custom.rgs_android_dom.ui.base.BaseViewHolder
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visibleIf

class SelectCardAdapter(
    private val onCardSelected: () -> Unit = {},
    private val onNewSelected: () -> Unit = {}
) : RecyclerView.Adapter<BaseViewHolder<SavedCardModel>>() {

    companion object {
        private const val TYPE_SAVED_CARD_ITEM = 0
        private const val TYPE_NEW_CARD_ITEM = 1
    }

    private var cards: List<SavedCardModel> = emptyList()
    private var selectedPosition: Int? = null
    val selectedCardName: String
        get() {
            return when (selectedPosition) {
                null -> "no"
                cards.size - 1 -> "new"
                else -> cards[selectedPosition!!].number
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SavedCardModel> {
        return if (viewType == TYPE_SAVED_CARD_ITEM) {
            val binding =
                ItemPurchaseCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SavedCardItemViewHolder(binding, onCardSelected)
        } else {
            val binding =
                ItemPurchaseNewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            NewCardItemViewHolder(binding, onNewSelected)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<SavedCardModel>, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == cards.size - 1) TYPE_NEW_CARD_ITEM
        else TYPE_SAVED_CARD_ITEM
    }

    fun setItems(cards: List<SavedCardModel>){
        this.cards = cards.plus(SavedCardModel.getEmptyInstance())
        notifyDataSetChanged()
    }

    private fun toggleSelectedItems(currPosition: Int) {
        if (currPosition != selectedPosition) {
            selectedPosition?.let { lastPosition ->
                cards[lastPosition].isSelected = false
                notifyItemChanged(lastPosition)
            }
            cards[currPosition].isSelected = true
            notifyItemChanged(currPosition)
            selectedPosition = currPosition
        }
    }

    inner class NewCardItemViewHolder(
        private val binding: ItemPurchaseNewCardBinding,
        private val onNewSelected: () -> Unit
    ) : BaseViewHolder<SavedCardModel>(binding.root) {

        override fun bind(item: SavedCardModel) {
            binding.cardSaveLayout.visibleIf(item.isSelected)
            binding.cardCheckedImageView.visibleIf(item.isSelected)
            binding.root.setOnDebouncedClickListener {
                toggleSelectedItems(absoluteAdapterPosition)

                onNewSelected()
            }
        }

    }

    inner class SavedCardItemViewHolder(
        private val binding: ItemPurchaseCardBinding,
        private val onCardClick: () -> Unit
    ) : BaseViewHolder<SavedCardModel>(binding.root) {

        override fun bind(item: SavedCardModel) {
            GlideApp.with(binding.root.context)
                .load(
                    when (item.type) {
                        SavedCardModel.CardType.VISA -> R.mipmap.visa
                        SavedCardModel.CardType.MASTERCARD -> R.mipmap.mastercard
                    }
                )
                .into(binding.cardTypeImageView)
            binding.cardCheckedImageView.visibleIf(item.isSelected)
            binding.cardNumberTextView.text = "•••• ${item.number}"
            binding.root.setOnDebouncedClickListener {
                toggleSelectedItems(absoluteAdapterPosition)

                onCardClick()
            }
        }

    }

}