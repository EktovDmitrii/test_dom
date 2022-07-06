package com.custom.rgs_android_dom.ui.purchase

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentPurchaseBinding
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.domain.purchase.models.CardModel
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseDateTimeModel
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseModel
import com.custom.rgs_android_dom.domain.purchase.models.SavedCardModel
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.confirm.ConfirmBottomSheetFragment
import com.custom.rgs_android_dom.ui.constants.PERCENT_PROMO_CODE
import com.custom.rgs_android_dom.ui.constants.SALE_PROMO_CODE
import com.custom.rgs_android_dom.ui.constants.ZERO_COST_ORDER
import com.custom.rgs_android_dom.ui.purchase.add.agent.AddAgentFragment
import com.custom.rgs_android_dom.ui.purchase.add.agent.PurchaseAgentListener
import com.custom.rgs_android_dom.ui.purchase.add.comment.PurchaseCommentListener
import com.custom.rgs_android_dom.ui.purchase.add.email.PurchaseEmailListener
import com.custom.rgs_android_dom.ui.purchase.select.address.SelectPurchaseAddressListener
import com.custom.rgs_android_dom.ui.purchase.select.card.SelectCardFragment
import com.custom.rgs_android_dom.ui.purchase.select.date_time.PurchaseDateTimeFragment
import com.custom.rgs_android_dom.utils.*
import com.yandex.metrica.YandexMetrica
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PurchaseFragment : BaseBottomSheetFragment<PurchaseViewModel, FragmentPurchaseBinding>(),
    SelectPurchaseAddressListener,
    PurchaseCommentListener,
    PurchaseDateTimeFragment.PurchaseDateTimeListener,
    PurchaseEmailListener,
    SelectCardFragment.PurchaseCardListener,
    PurchaseAgentListener,
    ConfirmBottomSheetFragment.ConfirmListener {

    companion object {

        private const val ARG_PURCHASE_SERVICE_MODEL = "ARG_PURCHASE_SERVICE_MODEL"
        private const val ARG_PROMO_CODE_MODEL = "ARG_PROMO_CODE_MODEL"

        fun newInstance(
            purchaseModel: PurchaseModel,
            promoCodeItemModel: PromoCodeItemModel?
        ): PurchaseFragment {
            return PurchaseFragment().args {
                putSerializable(ARG_PURCHASE_SERVICE_MODEL, purchaseModel)
                if (promoCodeItemModel != null) putSerializable(ARG_PROMO_CODE_MODEL, promoCodeItemModel)
            }
        }
    }

    override val TAG = "PURCHASE_FRAGMENT"

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getSerializable(ARG_PURCHASE_SERVICE_MODEL) as PurchaseModel,
            if (requireArguments().containsKey(ARG_PROMO_CODE_MODEL)) requireArguments().getSerializable(
                ARG_PROMO_CODE_MODEL
            ) as PromoCodeItemModel else null
        )
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

    private val discountText = TranslationInteractor.getTranslation("app.product.purchase.layout_product_detail.cost_discount_text_view")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.layoutProperty.root.setOnDebouncedClickListener {
            viewModel.onAddressClick(childFragmentManager)
        }

        binding.layoutNoProperty.root.setOnDebouncedClickListener {
            viewModel.onAddressClick(childFragmentManager)
        }

        binding.layoutProperty.addCommentTextView.setOnDebouncedClickListener {
            viewModel.onAddCommentClick(childFragmentManager)
        }

        binding.layoutDateTime.root.setOnDebouncedClickListener {
            viewModel.onDateTimeClick(childFragmentManager)
        }

        binding.layoutPayment.root.setOnDebouncedClickListener {
            viewModel.onCardClick(childFragmentManager)
        }

        binding.layoutEmail.root.setOnDebouncedClickListener {
            viewModel.onEmailClick(childFragmentManager)

            YandexMetrica.reportEvent("product_order_progress_email")
        }

        binding.layoutCodeAgent.root.setOnDebouncedClickListener {
            viewModel.onCodeAgentClick(childFragmentManager)
        }

        binding.makeOrderButton.productArrangeBtn.setOnDebouncedClickListener {
            viewModel.makeOrder()
        }

        binding.layoutIncludedPromoCode.promoCodeLinearLayout.setOnDebouncedClickListener {
            viewModel.onAddPromoCodeClick(childFragmentManager)
        }

        binding.layoutIncludedPromoCode.deletePromoCodeImageView.setOnDebouncedClickListener {
           viewModel.onDeletePromoCodeClick()
        }

        subscribe(viewModel.hasPromoCodeObserver) { promoCodeModel ->
            if (promoCodeModel != null) {
                viewModel.checkPromoCode()
                binding.layoutIncludedPromoCode.root.setMargins(bottom = 200)
            }
            binding.makeOrderButton.discountLayout.visibleIf(promoCodeModel != null)
            binding.layoutIncludedPromoCode.apply {
                arrowPromoCodeImageView.visibleIf(promoCodeModel == null)
                deletePromoCodeImageView.visibleIf(promoCodeModel != null)
                labeledPromoCodeTextView.visibleIf(promoCodeModel != null)
            }
        }

        subscribe(viewModel.purchaseObserver) { purchase ->

            binding.makeOrderButton.btnTitle.text = if (purchase.defaultProduct) {
                TranslationInteractor.getTranslation("app.product_cards.service_detail_view.buy_button_order")
            } else {
                TranslationInteractor.getTranslation("app.product_cards.service_detail_view.buy_button_arrange")
            }

            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(purchase.logoSmall))
                .transform(RoundedCorners(20.dp(requireContext())))
                .into(binding.layoutPurchaseServiceHeader.logoImageView)

            binding.layoutPurchaseServiceHeader.nameTextView.text = purchase.name

            binding.layoutPurchaseServiceHeader.orderTitleTextView.text = if (purchase.defaultProduct){
                TranslationInteractor.getTranslation("app.order_detail.order_title")
            } else {
                TranslationInteractor.getTranslation("app.order_detail.service_title")
            }

            binding.layoutDateTime.root.visibleIf(purchase.defaultProduct)
            binding.layoutPurchaseServiceHeader.durationTextView.visibleIf(purchase.duration != null)

            if (purchase.defaultProduct) {
                binding.layoutPurchaseServiceHeader.durationTextView.text = TranslationInteractor.getTranslation("app.support_service.prefix_titles.work_estimation") + purchase.deliveryTime
            } else {
                binding.layoutPurchaseServiceHeader.durationTextView.text = "Действует ${purchase.duration}"
            }

            purchase.price?.amount?.let { amount ->
                val promoCodeItemModel = viewModel.hasPromoCodeObserver.value
                binding.makeOrderButton.btnPrice.text = amount.formatPrice(isFixed = purchase.price.fix)
                binding.layoutPurchaseServiceHeader.priceTextView.text = amount.formatPrice(isFixed = purchase.price.fix)
                if (promoCodeItemModel != null) {
                    binding.makeOrderButton.root.background = ContextCompat.getDrawable(requireContext(), R.drawable.rectangle_filled_white_top_radius_24dp)
                    binding.makeOrderButton.sumCostTextView.text = amount.formatPrice(isFixed = purchase.price.fix)
                    binding.layoutIncludedPromoCode.promoCodeTextView.gone()
                    binding.layoutIncludedPromoCode.selectedPromoCodeTextView.visible()
                    binding.layoutIncludedPromoCode.labeledPromoCodeTextView.text = promoCodeItemModel.code

                    when (promoCodeItemModel.type) {
                        SALE_PROMO_CODE -> {
                            binding.makeOrderButton.discountTextView.text = discountText.replace("%@", "${promoCodeItemModel.discountInRubles}₽")
                            binding.makeOrderButton.sumDiscountTextView.text = "-${(promoCodeItemModel.discountInRubles).formatPrice()}"
                            val resultCost = amount - (promoCodeItemModel.discountInRubles)
                            binding.makeOrderButton.resultSumTextView.text = if (resultCost < 0 ) ZERO_COST_ORDER else resultCost.formatPrice(isFixed = purchase.price.fix)
                            binding.makeOrderButton.btnPrice.text = if (resultCost < 0 ) ZERO_COST_ORDER else resultCost.formatPrice(isFixed = purchase.price.fix)
                        }
                        PERCENT_PROMO_CODE -> {
                            binding.makeOrderButton.discountTextView.text = discountText.replace("%@", "${promoCodeItemModel.discountInPercent}%")
                            val resultDiscountIn = ((promoCodeItemModel.discountInPercent.toDouble() / 100.toDouble()) * amount.toDouble()).toInt()
                            val resultCost = amount - resultDiscountIn
                            binding.makeOrderButton.sumDiscountTextView.text = "-${resultDiscountIn.formatPrice()}"
                            binding.makeOrderButton.resultSumTextView.text = resultCost.formatPrice(isFixed = purchase.price.fix)
                            binding.makeOrderButton.btnPrice.text = resultCost.formatPrice(isFixed = purchase.price.fix)
                        }
                    }
                }
            }

            purchase.email?.let { email ->
                binding.layoutEmail.emailTextView.text = email
                binding.layoutEmail.emailTextView.typeface = ResourcesCompat.getFont(requireContext(), R.font.suisse_semi_bold)
            }

            purchase.agentCode?.let { code ->
                binding.layoutCodeAgent.codeAgentTextView.text = code
                binding.layoutCodeAgent.codeAgentTextView.typeface = ResourcesCompat.getFont(requireContext(), R.font.suisse_semi_bold)
            }

            purchase.card?.let { card ->
                if (card is SavedCardModel) {
                    binding.layoutPayment.paymentCardGroup.visible()
                    binding.layoutPayment.paymentNewCardImageView.invisible()
                    binding.layoutPayment.paymentCardNumberTextView.text = card.number.takeLast(4)
                    binding.layoutPayment.paymentCardTextView.text = TranslationInteractor.getTranslation("app.product.purchase.card_payment")
                    binding.layoutPayment.paymentCardTextView.typeface = ResourcesCompat.getFont(requireContext(), R.font.suisse_semi_bold)
                } else {
                    binding.layoutPayment.paymentCardGroup.invisible()
                    binding.layoutPayment.paymentNewCardImageView.visible()
                    binding.layoutPayment.paymentCardTextView.text =TranslationInteractor.getTranslation("app.product.purchase.new_card_payment")
                    binding.layoutPayment.paymentCardTextView.typeface = ResourcesCompat.getFont(requireContext(), R.font.suisse_semi_bold)
                }
            }

            binding.layoutProperty.root.visibleIf(purchase.propertyItemModel != null)
            binding.layoutNoProperty.root.visibleIf(purchase.propertyItemModel == null)

            binding.layoutProperty.addCommentTextView.text = if (purchase.comment.isNullOrEmpty()){
                TranslationInteractor.getTranslation("app.product.purchase.property.leave_comment")
            } else {
                TranslationInteractor.getTranslation("app.product.purchase.property.edit_comment")
            }

            purchase.propertyItemModel?.let {
                binding.layoutProperty.propertyTypeTextView.text = it.name
                binding.layoutProperty.propertyAddressTextView.text =
                    it.address?.address
                if (it.photoLink != null) {
                    GlideApp.with(requireContext())
                        .load(GlideUrlProvider.makeHeadersGlideUrl(it.photoLink))
                        .transform(RoundedCorners(4.dp(requireContext())))
                        .into(binding.layoutProperty.propertyTypeImageView)
                } else {
                    when (it.type) {
                        PropertyType.HOUSE -> {
                            binding.layoutProperty.propertyTypeImageView.setImageResource(
                                R.drawable.ic_type_home
                            )
                        }
                        PropertyType.APARTMENT -> {
                            binding.layoutProperty.propertyTypeImageView.setImageResource(
                                R.drawable.ic_type_apartment_334px
                            )
                        }
                        else -> throw IllegalArgumentException("Wrong argument for ${it.type}")
                    }
                }
            }

            binding.layoutDateTime.filledDateTimeGroup.visibleIf(purchase.purchaseDateTimeModel != null)
            binding.layoutDateTime.chooseDateTimeTextView.goneIf(purchase.purchaseDateTimeModel != null)

            purchase.purchaseDateTimeModel?.selectedPeriodModel?.let {
                binding.layoutDateTime.timesOfDayTextView.text = "${it.timeFrom} – ${it.timeTo}"
            }
            binding.layoutDateTime.timeIntervalTextView.text =
                purchase.purchaseDateTimeModel?.selectedDate?.formatTo(DATE_PATTERN_DATE_FULL_MONTH)
        }

        subscribe(viewModel.hasCodeAgentObserver) {
            binding.layoutCodeAgent.root.goneIf(it)
        }

        subscribe(viewModel.isEnableButtonObserver) { isEnable ->
            binding.makeOrderButton.productArrangeBtn.isEnabled = isEnable
            if (isEnable) {
                binding.makeOrderButton.btnTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.makeOrderButton.btnPrice.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.makeOrderButton.productArrangeBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.rectangle_filled_primary_600_radius_12dp)
            } else {
                binding.makeOrderButton.btnTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary500))
                binding.makeOrderButton.btnPrice.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary500))
                binding.makeOrderButton.productArrangeBtn.background = ContextCompat.getDrawable(requireContext(), R.drawable.rectangle_filled_secondary_100_radius_12dp)
            }
        }
    }

    override fun onPropertySelected(propertyItemModel: PropertyItemModel) {
        viewModel.updateAddress(propertyItemModel)

        if (viewModel.purchaseObserver.value?.defaultProduct == true)
            YandexMetrica.reportEvent("service_order_progress_address")
        else
            YandexMetrica.reportEvent("product_order_progress_address")
    }

    override fun onCommentSelected(comment: String) {
        viewModel.updateComment(comment)
    }

    override fun onSelectDateTimeClick(purchaseDateTimeModel: PurchaseDateTimeModel) {
        viewModel.updateDateTime(purchaseDateTimeModel)

        YandexMetrica.reportEvent("service_order_progress_date")
    }

    override fun onSaveEmailClick(email: String) {
        viewModel.updateEmail(email)

        if (viewModel.purchaseObserver.value?.defaultProduct == true)
            YandexMetrica.reportEvent("service_order_progress_email")
        else
            YandexMetrica.reportEvent("product_order_progress_email")
    }

    override fun onSaveCardClick(card: CardModel) {
        viewModel.updateCard(card)

        if (viewModel.purchaseObserver.value?.defaultProduct == true)
            YandexMetrica.reportEvent("service_order_progress_payment_method")
        else
            YandexMetrica.reportEvent("product_order_progress_payment_method")
    }

    override fun onSaveCodeError() {
        val confirmDialog = ConfirmBottomSheetFragment.newInstance(
            icon = R.drawable.ic_broken_egg,
            title = "Невозможно обработать",
            description = "К сожалению, мы не смогли найти и привязать код агента. Пожалуйста, попробуйте еще раз",
            confirmText = "Попробовать ещё раз",
            cancelText = "Нет, спасибо"
        )
        confirmDialog.show(childFragmentManager, ConfirmBottomSheetFragment.TAG)
    }

    override fun onSaveCodeSuccess(code: String) {
        viewModel.updateAgentCode(code)

        YandexMetrica.reportEvent("service_order_progress_agent")
    }

    override fun onConfirmClick() {
        val addAgentBottomFragment = AddAgentFragment()
        addAgentBottomFragment.show(childFragmentManager, addAgentBottomFragment.TAG)
    }

    override fun isNavigationViewVisible(): Boolean {
        return false
    }

    override fun onLoading() {
        super.onLoading()
        binding.apply {
            makeOrderButton.loadingProgressBar.visible()
            makeOrderButton.btnPrice.gone()
            layoutIncludedPromoCode.errorTextView.gone()
            makeOrderButton.discountLayout.gone()
        }
    }

    override fun onError() {
        super.onError()
        binding.apply {
            layoutIncludedPromoCode.labeledPromoCodeTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            layoutIncludedPromoCode.root.setMargins(bottom = 0)
            makeOrderButton.loadingProgressBar.gone()
            makeOrderButton.btnPrice.visible()
            layoutIncludedPromoCode.errorTextView.visible()
            makeOrderButton.discountLayout.gone()
        }
    }

    override fun onContent() {
        super.onContent()
        binding.apply {
            makeOrderButton.loadingProgressBar.gone()
            makeOrderButton.btnPrice.visible()
            layoutIncludedPromoCode.errorTextView.gone()
            makeOrderButton.discountLayout.visible()
        }
    }
}
