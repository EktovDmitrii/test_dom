package com.custom.rgs_android_dom.ui.promo_code

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemPromoCodePercentBinding
import com.custom.rgs_android_dom.databinding.ItemPromoCodeSaleBinding
import com.custom.rgs_android_dom.databinding.ItemPromoCodeServiceBinding
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeItemModel
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.constants.PERCENT_PROMO_CODE
import com.custom.rgs_android_dom.ui.constants.SERVICE_PROMO_CODE
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_ONLY
import com.custom.rgs_android_dom.utils.formatTo
import com.custom.rgs_android_dom.utils.insertDate

class PromoCodesAdapter(private val onPromoCodesClick: (String, Boolean) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val promoCodes = mutableListOf<PromoCodeItemModel>()
    private val durationText =
        TranslationInteractor.getTranslation("app.promo_codes.agent_code_adapter.add_duration")
    private val titleText =
        TranslationInteractor.getTranslation("app.promo_codes.agent_code_adapter.add_second_title")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_SALE_PROMO_CODE -> {
                val binding = ItemPromoCodeSaleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PromoCodesSaleViewHolder(binding, onPromoCodesClick)
            }
            ITEM_TYPE_PERCENT_PROMO_CODE -> {
                val binding = ItemPromoCodePercentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PromoCodesPercentViewHolder(binding, onPromoCodesClick)
            }
            else -> {
                val binding = ItemPromoCodeServiceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PromoCodesServiceViewHolder(binding, onPromoCodesClick)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = promoCodes[position]

        when (holder) {
            is PromoCodesAdapter.PromoCodesSaleViewHolder -> holder.bind(model)
            is PromoCodesAdapter.PromoCodesPercentViewHolder -> holder.bind(model)
            is PromoCodesAdapter.PromoCodesServiceViewHolder -> holder.bind(model)
        }
    }

    override fun getItemCount(): Int = promoCodes.size

    override fun getItemViewType(position: Int): Int {
        return when (promoCodes[position].type) {
            PERCENT_PROMO_CODE -> ITEM_TYPE_PERCENT_PROMO_CODE
            SERVICE_PROMO_CODE -> ITEM_TYPE_SERVICE_PROMO_CODE
            else -> ITEM_TYPE_SALE_PROMO_CODE
        }
    }

    fun setItems(promoCodes: List<PromoCodeItemModel>) {
        this.promoCodes.clear()
        this.promoCodes.addAll(promoCodes)
        notifyDataSetChanged()
    }

    inner class PromoCodesSaleViewHolder(
        val binding: ItemPromoCodeSaleBinding,
        val onPromoCodesClick: (String, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PromoCodeItemModel) {
            binding.apply {
                val duration = durationText.replace("%@", " ${model.expiredAt?.formatTo(DATE_PATTERN_DATE_ONLY)}")
                titleText.insertDate(model.expiredAt, null)
                titleTextView.text = titleText.replace("%@", "${model.discountInRubles} Р")
                durationTextView.text = duration
            }
        }
    }

    inner class PromoCodesPercentViewHolder(
        val binding: ItemPromoCodePercentBinding,
        val onPromoCodesClick: (String, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PromoCodeItemModel) {
            val duration = durationText.replace("%@", " ${model.expiredAt?.formatTo(DATE_PATTERN_DATE_ONLY)}")
            binding.apply {
                subtitleTextView.text = model.code
                titleTextView.text = titleText.replace("%@", "${model.discountInPercent}%")
                durationTextView.text = duration
            }
        }
    }

    inner class PromoCodesServiceViewHolder(
        val binding: ItemPromoCodeServiceBinding,
        val onPromoCodesClick: (String, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PromoCodeItemModel) {
            val duration =
                durationText.replace("%@", " ${model.expiredAt?.formatTo(DATE_PATTERN_DATE_ONLY)}")
            binding.apply {
                subtitleTextView.text = model.code
                titleTextView.text = TranslationInteractor.getTranslation("app.promo_codes.agent_code_adapter.add_title")
                durationTextView.text = duration
            }
        }
    }

    companion object {
        private const val ITEM_TYPE_SALE_PROMO_CODE = 1
        private const val ITEM_TYPE_PERCENT_PROMO_CODE = 2
        private const val ITEM_TYPE_SERVICE_PROMO_CODE = 3
    }
}
