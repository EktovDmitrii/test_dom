package com.custom.rgs_android_dom.ui.client.payment_methods

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ItemPaymentMethodBinding
import com.custom.rgs_android_dom.domain.purchase.models.CardType
import com.custom.rgs_android_dom.domain.purchase.models.SavedCardModel
import com.custom.rgs_android_dom.ui.client.payment_methods.error.ErrorDeletePaymentMethodFragment
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visibleIf

class PaymentMethodsAdapter(private val onDeleteClick: (SavedCardModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isInEditMode = false
    private var paymentMethods: List<SavedCardModel> = emptyList()

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): RecyclerView.ViewHolder {
        val binding = ItemPaymentMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentMethodViewHolder(binding) {
            onDeleteClick(it)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PaymentMethodViewHolder).bind(paymentMethods[position])
    }

    override fun getItemCount(): Int {
        return paymentMethods.size
    }

    fun setItems(paymentMethods: List<SavedCardModel>) {
        this.paymentMethods = paymentMethods
        notifyDataSetChanged()
    }

    fun setIsInEditMode(isInEditMode: Boolean){
        this.isInEditMode = isInEditMode
        notifyDataSetChanged()
    }

    inner class PaymentMethodViewHolder(
        private val binding: ItemPaymentMethodBinding,
        private val onDeleteClick: (SavedCardModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(paymentMethod: SavedCardModel) {
            GlideApp.with(binding.root.context)
            .load(
                when (paymentMethod.type) {
                    CardType.VISA -> R.mipmap.visa
                    CardType.MASTERCARD -> R.mipmap.mastercard
                }
            )
            .into(binding.cardTypeImageView)
            binding.cardNumberTextView.text = paymentMethod.number

            binding.deleteCardImageView.visibleIf(isInEditMode)

            binding.deleteCardImageView.setOnDebouncedClickListener {
                onDeleteClick(paymentMethod)
            }
        }

    }
}