package com.custom.rgs_android_dom.ui.promo_code

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ItemPromoCodePercentBinding
import com.custom.rgs_android_dom.databinding.ItemPromoCodeSaleBinding
import com.custom.rgs_android_dom.databinding.ItemPromoCodeServiceBinding
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeItemModel
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.constants.PERCENT_PROMO_CODE
import com.custom.rgs_android_dom.ui.constants.SERVICE_PROMO_CODE
import com.custom.rgs_android_dom.utils.*

class PromoCodesAdapter(
    private val onPromoCodeClick: (PromoCodeItemModel) -> Unit,
    private val onShowApplyButton: (Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val promoCodes = mutableListOf<PromoCodeItemModel>()
    private var onPromoCode: PromoCodeItemModel? = null
    private var selectedPromoCodeItem: PromoCodeItemModel? = null
    private var isSelectedPromoCode = false
    private var isModalPromoCodes = false
    private val durationText = TranslationInteractor.getTranslation("app.promo_codes.agent_code_adapter.add_duration")
    private val titleText = TranslationInteractor.getTranslation("app.promo_codes.agent_code_adapter.add_second_title")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_SALE_PROMO_CODE -> {
                val binding = ItemPromoCodeSaleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PromoCodesSaleViewHolder(binding)
            }
            ITEM_TYPE_PERCENT_PROMO_CODE -> {
                val binding = ItemPromoCodePercentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PromoCodesPercentViewHolder(binding)
            }
            else -> {
                val binding = ItemPromoCodeServiceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PromoCodesServiceViewHolder(binding)
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

    fun setItems(
        promoCodes: List<PromoCodeItemModel>,
        isModalPromoCodes: Boolean,
        selectedPromoCodeItem: PromoCodeItemModel?
    ) {
        this.promoCodes.clear()
        this.promoCodes.addAll(promoCodes)
        this.isModalPromoCodes = isModalPromoCodes
        this.selectedPromoCodeItem = selectedPromoCodeItem
        if (selectedPromoCodeItem != null) onPromoCode = selectedPromoCodeItem
        notifyDataSetChanged()
    }

    inner class PromoCodesSaleViewHolder(
        val binding: ItemPromoCodeSaleBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PromoCodeItemModel) {
            binding.apply {
                subtitleTextView.text = model.code
                val duration = durationText.replace(
                    "%@",
                    " ${model.expiredAt?.formatTo(DATE_PATTERN_DATE_ONLY)}"
                )
                titleText.insertDate(model.expiredAt, null)
                titleTextView.text = titleText.replace("%@", "${model.discountInRubles} ₽")
                durationTextView.text = duration
                if (isModalPromoCodes) {
                    if (onPromoCode == model && isSelectedPromoCode || selectedPromoCodeItem != null && selectedPromoCodeItem == model) {
                        promoCodeCheckedImageView.visible()
                        pictureFrameLayout.background = ContextCompat.getDrawable(
                            binding.root.context,
                            R.drawable.ic_rectangle_orange_border_radius_24dp
                        )
                    } else {
                        promoCodeCheckedImageView.gone()
                        pictureFrameLayout.background = null
                    }
                }

                root.setOnDebouncedClickListener {
                    onClickedModelPromoCode(model)
                    notifyDataSetChanged()
                }
            }
        }
    }

    inner class PromoCodesPercentViewHolder(
        val binding: ItemPromoCodePercentBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PromoCodeItemModel) {
            val duration = durationText.replace("%@", " ${model.expiredAt?.formatTo(DATE_PATTERN_DATE_ONLY)}")

            binding.apply {
                subtitleTextView.text = model.code
                titleTextView.text = titleText.replace("%@", "${model.discountInPercent}%")
                durationTextView.text = duration
                if (isModalPromoCodes) {
                    if (onPromoCode == model && isSelectedPromoCode || selectedPromoCodeItem != null && selectedPromoCodeItem == model) {
                        promoCodeCheckedImageView.visible()
                        pictureFrameLayout.background = ContextCompat.getDrawable(
                            binding.root.context,
                            R.drawable.ic_rectangle_orange_border_radius_24dp
                        )
                    } else {
                        promoCodeCheckedImageView.gone()
                        pictureFrameLayout.background = null
                    }
                }

                root.setOnDebouncedClickListener {
                    onClickedModelPromoCode(model)
                    notifyDataSetChanged()
                }
            }
        }
    }

    inner class PromoCodesServiceViewHolder(
        val binding: ItemPromoCodeServiceBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PromoCodeItemModel) {
            val duration =
                durationText.replace("%@", " ${model.expiredAt?.formatTo(DATE_PATTERN_DATE_ONLY)}")
            binding.apply {
                subtitleTextView.text = model.code
                titleTextView.text = TranslationInteractor.getTranslation("app.promo_codes.agent_code_adapter.add_title")
                durationTextView.text = duration
                if (isModalPromoCodes) {
                    if (onPromoCode == model && isSelectedPromoCode || selectedPromoCodeItem != null && selectedPromoCodeItem == model) {
                        promoCodeCheckedImageView.visible()
                        pictureFrameLayout.background = ContextCompat.getDrawable(
                            binding.root.context,
                            R.drawable.ic_rectangle_orange_border_radius_24dp
                        )
                    } else {
                        promoCodeCheckedImageView.gone()
                        pictureFrameLayout.background = null
                    }
                }

                root.setOnDebouncedClickListener {
                    onClickedModelPromoCode(model)
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun onClickedModelPromoCode(model: PromoCodeItemModel) {
        if (!isModalPromoCodes) {
            onPromoCodeClick(model)
        } else {
            when {
                onPromoCode == null && !isSelectedPromoCode && selectedPromoCodeItem == null -> {
                    onPromoCodeClick(model)
                    onShowApplyButton(true)
                    onPromoCode = model
                    selectedPromoCodeItem = null
                    isSelectedPromoCode = true
                }
                onPromoCode == model && isSelectedPromoCode && selectedPromoCodeItem == null -> {
                    onShowApplyButton(false)
                    onPromoCode = null
                    selectedPromoCodeItem = null
                    isSelectedPromoCode = false
                }
                onPromoCode != model && isSelectedPromoCode && selectedPromoCodeItem == null -> {
                    onPromoCodeClick(model)
                    onShowApplyButton(true)
                    onPromoCode = model
                    selectedPromoCodeItem = null
                    isSelectedPromoCode = true
                }
                selectedPromoCodeItem != null && onPromoCode == model -> {
                    onShowApplyButton(false)
                    onPromoCode = null
                    selectedPromoCodeItem = null
                    isSelectedPromoCode = false
                }
                selectedPromoCodeItem != null && onPromoCode != model -> {
                    onPromoCodeClick(model)
                    onShowApplyButton(true)
                    onPromoCode = model
                    selectedPromoCodeItem = null
                    isSelectedPromoCode = true
                }
            }
        }
    }

    companion object {
        private const val ITEM_TYPE_SALE_PROMO_CODE = 1
        private const val ITEM_TYPE_PERCENT_PROMO_CODE = 2
        private const val ITEM_TYPE_SERVICE_PROMO_CODE = 3
    }
}
