package com.custom.rgs_android_dom.ui.purchase

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentPurchaseBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.domain.purchase.model.PurchaseDateTimeModel
import com.custom.rgs_android_dom.domain.purchase.model.PurchaseModel
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.domain.purchase.model.*
import com.custom.rgs_android_dom.ui.confirm.ConfirmBottomSheetFragment
import com.custom.rgs_android_dom.ui.purchase.add.agent.AddAgentFragment
import com.custom.rgs_android_dom.ui.purchase.add.agent.PurchaseAgentListener
import com.custom.rgs_android_dom.ui.purchase.add.comment.PurchaseCommentListener
import com.custom.rgs_android_dom.ui.purchase.select.date_time.PurchaseDateTimeFragment
import com.custom.rgs_android_dom.ui.purchase.select.address.SelectPurchaseAddressListener
import com.custom.rgs_android_dom.ui.purchase.select.card.SelectCardFragment
import com.custom.rgs_android_dom.ui.purchase.add.email.PurchaseEmailListener
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PurchaseFragment : BaseFragment<PurchaseViewModel, FragmentPurchaseBinding>(R.layout.fragment_purchase),
    SelectPurchaseAddressListener,
    PurchaseCommentListener,
    PurchaseDateTimeFragment.PurchaseDateTimeListener,
    PurchaseEmailListener,
    SelectCardFragment.PurchaseCardListener,
    PurchaseAgentListener,
    ConfirmBottomSheetFragment.ConfirmListener {

    companion object {
        private const val ARG_PURCHASE_SERVICE_MODEL = "ARG_PURCHASE_SERVICE_MODEL"

        fun newInstance(purchaseModel: PurchaseModel): PurchaseFragment {
            return PurchaseFragment().args {
                putSerializable(ARG_PURCHASE_SERVICE_MODEL, purchaseModel)
            }
        }
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary500)
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_PURCHASE_SERVICE_MODEL) as PurchaseModel)
    }

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
        }

        binding.layoutCodeAgent.root.setOnDebouncedClickListener {
            viewModel.onCodeAgentClick(childFragmentManager)
        }

        binding.makeOrderButton.productArrangeBtn.setOnDebouncedClickListener {
            viewModel.makeOrder(getNavigateId())
        }

        subscribe(viewModel.purchaseObserver) { purchase ->

            binding.makeOrderButton.btnTitle.text =  if (purchase.defaultProduct){
                "Заказать"
            } else {
                "Оформить продукт"
            }

            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(purchase.logoSmall))
                .transform(RoundedCorners(20.dp(requireContext())))
                .into(binding.layoutPurchaseServiceHeader.logoImageView)

            binding.layoutPurchaseServiceHeader.nameTextView.text = purchase.name

            binding.layoutPurchaseServiceHeader.orderTitleTextView.text = if (purchase.defaultProduct){
                "Ваш заказ"
            } else {
                "Оформление продукта"
            }

            binding.layoutDateTime.root.visibleIf(purchase.defaultProduct)
            binding.layoutPurchaseServiceHeader.durationTextView.visibleIf(purchase.duration != null)

            if (purchase.defaultProduct){
                binding.layoutPurchaseServiceHeader.durationTextView.text = purchase.deliveryTime
            } else {
                binding.layoutPurchaseServiceHeader.durationTextView.text = "Действует ${purchase.duration}"
            }

            purchase.price?.amount?.let { amount ->
                binding.layoutPurchaseServiceHeader.priceTextView.text = amount.formatPrice(isFixed = purchase.price.fix)
                binding.makeOrderButton.btnPrice.text = amount.formatPrice(isFixed = purchase.price.fix)
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
                    binding.layoutPayment.paymentCardTextView.text = "Оплата картой"
                    binding.layoutPayment.paymentCardTextView.typeface = ResourcesCompat.getFont(requireContext(), R.font.suisse_semi_bold)
                } else {
                    binding.layoutPayment.paymentCardGroup.invisible()
                    binding.layoutPayment.paymentNewCardImageView.visible()
                    binding.layoutPayment.paymentCardTextView.text = "Оплата новой картой"
                    binding.layoutPayment.paymentCardTextView.typeface = ResourcesCompat.getFont(requireContext(), R.font.suisse_semi_bold)
                }
            }

            binding.layoutProperty.root.visibleIf(purchase.propertyItemModel != null)
            binding.layoutNoProperty.root.visibleIf(purchase.propertyItemModel == null)

            binding.layoutProperty.addCommentTextView.text = if (purchase.comment.isNullOrEmpty()){
                "Добавить комментарий"
            } else {
                "Редактировать комментарий"
            }

            purchase.propertyItemModel?.let {
                binding.layoutProperty.propertyTypeTextView.text = it.name
                binding.layoutProperty.propertyAddressTextView.text =
                    it.address?.address
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
                }
            }

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
    }

    override fun onSaveCommentClick(comment: String) {
        viewModel.updateComment(comment)
    }

    override fun onSelectDateTimeClick(purchaseDateTimeModel: PurchaseDateTimeModel) {
        binding.layoutDateTime.filledDateTimeGroup.visible()
        binding.layoutDateTime.chooseDateTimeTextView.gone()
        binding.layoutDateTime.timesOfDayTextView.text = purchaseDateTimeModel.selectedPeriodModel?.displayTime
        binding.layoutDateTime.timeIntervalTextView.text = purchaseDateTimeModel.date.formatTo(DATE_PATTERN_DATE_FULL_MONTH)
        viewModel.updateDateTime(purchaseDateTimeModel)
    }

    override fun onSaveEmailClick(email: String) {
        viewModel.updateEmail(email)
    }

    override fun onSaveCardClick(card: CardModel) {
        viewModel.updateCard(card)
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
    }

    override fun onConfirmClick() {
        val addAgentBottomFragment = AddAgentFragment()
        addAgentBottomFragment.show(childFragmentManager, addAgentBottomFragment.TAG)
    }
}
