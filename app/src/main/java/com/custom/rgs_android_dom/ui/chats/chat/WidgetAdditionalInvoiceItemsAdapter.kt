package com.custom.rgs_android_dom.ui.chats.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemChatWidgetAdditionalInvoiceItemBinding
import com.custom.rgs_android_dom.domain.chat.models.WidgetAdditionalInvoiceItemModel
import com.custom.rgs_android_dom.utils.simplePriceFormat
import com.custom.rgs_android_dom.utils.visibleIf

class WidgetAdditionalInvoiceItemsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<WidgetAdditionalInvoiceItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WidgetAdditionalInvoiceItemViewHolder(
            ItemChatWidgetAdditionalInvoiceItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as WidgetAdditionalInvoiceItemViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<WidgetAdditionalInvoiceItemModel>) {
        this.items.clear()
        this.items.addAll(items)
    }

    inner class WidgetAdditionalInvoiceItemViewHolder(
        private val binding: ItemChatWidgetAdditionalInvoiceItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: WidgetAdditionalInvoiceItemModel) {
            binding.priceTextView.text = model.price.simplePriceFormat()
            binding.nameTextView.text = model.name
            binding.quantityLinearLayout.visibleIf(model.quantity > 1)
            binding.quantityTextView.text = model.quantity.toString()
        }

    }

}
