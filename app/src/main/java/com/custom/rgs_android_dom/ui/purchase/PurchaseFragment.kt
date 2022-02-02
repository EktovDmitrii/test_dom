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
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchaseDateTimeModel
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchaseModel
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.domain.purchase_service.model.*
import com.custom.rgs_android_dom.ui.confirm.ConfirmBottomSheetFragment
import com.custom.rgs_android_dom.ui.purchase.add_purchase_service_comment.PurchaseCommentFragment
import com.custom.rgs_android_dom.ui.purchase.edit_purchase_date_time.PurchaseDateTimeFragment
import com.custom.rgs_android_dom.ui.purchase.select.address.SelectPurchaseAddressListener
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PurchaseFragment : BaseFragment<PurchaseViewModel, FragmentPurchaseBinding>(R.layout.fragment_purchase),
    SelectPurchaseAddressListener,
    PurchaseCommentFragment.PurchaseCommentListener,
    PurchaseDateTimeFragment.PurchaseDateTimeListener,
    AddEmailBottomFragment.PurchaseEmailListener,
    SelectCardBottomFragment.PurchaseCardListener,
    AddAgentBottomFragment.PurchaseAgentCodeListener,
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
            val editPurchaseServiceComment = PurchaseCommentFragment.newInstance()
            editPurchaseServiceComment.show(childFragmentManager, PurchaseCommentFragment.TAG)
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

        binding.makeOrderButton.btnTitle.text = "Заказать"

        subscribe(viewModel.purchaseObserver) { purchase ->
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(purchase.iconLink))
                .transform(RoundedCorners(20.dp(requireContext())))
                .into(binding.layoutPurchaseServiceHeader.orderImageView)

            binding.layoutPurchaseServiceHeader.orderNameTextView.text = purchase.name

            purchase.price?.amount?.let { amount ->
                binding.layoutPurchaseServiceHeader.orderCostTextView.text = amount.formatPrice()
                binding.makeOrderButton.btnPrice.text = amount.formatPrice()
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
                    binding.layoutPayment.paymentCardTextView.text = (card as NewCardModel).title
                    binding.layoutPayment.paymentCardTextView.typeface = ResourcesCompat.getFont(requireContext(), R.font.suisse_semi_bold)
                }
            }

            binding.layoutProperty.root.visibleIf(purchase.propertyItemModel != null)
            binding.layoutNoProperty.root.visibleIf(purchase.propertyItemModel == null)

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
        binding.layoutDateTime.timesOfDayTextView.text = purchaseDateTimeModel.selectedPeriodModel?.timeInterval
        binding.layoutDateTime.timeIntervalTextView.text = purchaseDateTimeModel.date.formatTo(DATE_PATTERN_DATE_FULL_MONTH)
        viewModel.updateDateTime(purchaseDateTimeModel)
    }

    override fun onConfirmClick() {
        val addAgentBottomFragment = AddAgentBottomFragment()
        addAgentBottomFragment.show(childFragmentManager, addAgentBottomFragment.TAG)
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

}
