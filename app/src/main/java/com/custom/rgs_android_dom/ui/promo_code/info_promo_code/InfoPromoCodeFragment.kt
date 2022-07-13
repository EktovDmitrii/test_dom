package com.custom.rgs_android_dom.ui.promo_code.info_promo_code

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentInfoPromoCodeBinding
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeItemModel
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.ui.constants.PERCENT_PROMO_CODE
import com.custom.rgs_android_dom.ui.constants.SERVICE_PROMO_CODE
import com.custom.rgs_android_dom.utils.*

class InfoPromoCodeFragment : BaseBottomSheetModalFragment<InfoPromoCodeViewModel, FragmentInfoPromoCodeBinding>() {

    companion object {

        private const val KEY_PROMO_CODE_INFO = "KEY_PROMO_CODE_INFO"

        fun newInstance(
            promoCode: PromoCodeItemModel
        ) =
            InfoPromoCodeFragment().args {
                putSerializable(KEY_PROMO_CODE_INFO, promoCode)
            }
    }

    override val TAG: String = "PROMO_CODE_INFO_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expand()

        val promoCode = requireArguments().getSerializable(KEY_PROMO_CODE_INFO) as PromoCodeItemModel
        val durationText = TranslationInteractor.getTranslation("app.promo_codes.agent_code_adapter.add_duration")
        val titleText = TranslationInteractor.getTranslation("app.promo_codes.agent_code_adapter.add_second_title")
        val duration = durationText.replace("%@", " ${promoCode.expiredAt?.formatTo(DATE_PATTERN_DATE_ONLY)}")

        binding.bindPolicySuccessLayout.apply {
            understandTextView.setOnDebouncedClickListener {
                viewModel.close()
            }

            when (promoCode.type) {
                PERCENT_PROMO_CODE -> {
                    itemPercentLayout.apply {
                        root.visible()
                        subtitleTextView.text = promoCode.code
                        titleTextView.text = titleText.replace("%@", "${promoCode.discountInPercent}%")
                        durationTextView.text = duration
                    }
                }
                SERVICE_PROMO_CODE -> {
                    itemServiceLayout.apply {
                        root.visible()
                        subtitleTextView.text = promoCode.code
                        titleTextView.text = TranslationInteractor.getTranslation("app.promo_codes.agent_code_adapter.add_title")
                        durationTextView.text = duration
                    }
                }
                else -> {
                    itemSaleLayout.apply {
                        root.visible()
                        subtitleTextView.text = promoCode.code
                        titleTextView.text = titleText.replace("%@", promoCode.discountInRubles.formatPrice())
                        durationTextView.text = duration
                    }
                }
            }
            descriptionTextView.text = promoCode.description
            titleSecondTextView.text = promoCode.name
        }
    }
}
