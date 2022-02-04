package com.custom.rgs_android_dom.ui.purchase.select.card

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ItemPurchaseCardBinding
import com.custom.rgs_android_dom.databinding.ItemPurchaseNewCardBinding
import com.custom.rgs_android_dom.domain.purchase.model.CardModel
import com.custom.rgs_android_dom.domain.purchase.model.CardType
import com.custom.rgs_android_dom.domain.purchase.model.NewCardModel
import com.custom.rgs_android_dom.domain.purchase.model.SavedCardModel
import com.custom.rgs_android_dom.ui.base.BaseViewHolder
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visibleIf

class CardsAdapter(
    private val onCardSelected: (CardModel) -> Unit = {}
) : RecyclerView.Adapter<BaseViewHolder<CardModel>>() {

    companion object {
        private const val TYPE_SAVED_CARD_ITEM = 0
        private const val TYPE_NEW_CARD_ITEM = 1
    }

    private var cards: List<CardModel> = listOf(NewCardModel())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<CardModel> {
        return if (viewType == TYPE_SAVED_CARD_ITEM) {
            val binding = ItemPurchaseCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SavedCardItemViewHolder(binding, onCardSelected)
        } else {
            val binding = ItemPurchaseNewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            NewCardItemViewHolder(binding, onCardSelected)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<CardModel>, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (cards[position] is SavedCardModel) TYPE_SAVED_CARD_ITEM
        else TYPE_NEW_CARD_ITEM
    }

    fun setItems(cards: List<CardModel>){
        this.cards = cards
        notifyDataSetChanged()
    }

    private fun toggleSelection(selectedCard: CardModel) {
        var currentSelectedCardPosition = cards.indexOf(cards.find { it.isSelected })
        if (currentSelectedCardPosition != -1){
            cards[currentSelectedCardPosition].isSelected = false
            notifyItemChanged(currentSelectedCardPosition)
        }

        selectedCard.isSelected = true
        val newSelectedCardPosition = cards.indexOf(selectedCard)
        notifyItemChanged(newSelectedCardPosition)
    }

    inner class NewCardItemViewHolder(
        private val binding: ItemPurchaseNewCardBinding,
        private val onCardSelect: (CardModel) -> Unit
    ) : BaseViewHolder<CardModel>(binding.root) {

        override fun bind(item: CardModel) {
            var newCard = item as NewCardModel
            binding.cardSaveLayout.visibleIf(newCard.isSelected)
            binding.cardCheckedImageView.visibleIf(newCard.isSelected)
            binding.saveCardSwitch.setOnCheckedChangeListener { button, isChecked ->
                newCard = newCard.copy(doSave = isChecked)

                onCardSelect(newCard)
            }
            binding.root.setOnDebouncedClickListener {
                toggleSelection(newCard)
                onCardSelect(newCard)
            }
        }

    }

    inner class SavedCardItemViewHolder(
        private val binding: ItemPurchaseCardBinding,
        private val onCardSelect: (CardModel) -> Unit
    ) : BaseViewHolder<CardModel>(binding.root) {

        override fun bind(item: CardModel) {
            val savedCard = item as SavedCardModel

            GlideApp.with(binding.root.context)
                .load(
                    when (savedCard.type) {
                        CardType.VISA -> R.mipmap.visa
                        CardType.MASTERCARD -> R.mipmap.mastercard
                    }
                )
                .into(binding.cardTypeImageView)
            binding.cardCheckedImageView.visibleIf(savedCard.isSelected)
            binding.cardNumberTextView.text = savedCard.number

            binding.root.setOnDebouncedClickListener {
                toggleSelection(savedCard)
                onCardSelect(savedCard)
            }
        }

    }

}