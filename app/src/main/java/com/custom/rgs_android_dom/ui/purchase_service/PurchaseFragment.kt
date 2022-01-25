package com.custom.rgs_android_dom.ui.purchase_service

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentResultListener
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentPurchaseServiceBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchaseDateTimeModel
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchaseModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.confirm.ConfirmBottomSheetFragment
import com.custom.rgs_android_dom.ui.purchase_service.add_purchase_service_comment.PurchaseCommentFragment
import com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_date_time.PurchaseDateTimeFragment
import com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_service_address.PurchaseAddressFragment
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_FULL_MONTH
import com.custom.rgs_android_dom.utils.DigitsFormatter
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.formatTo
import com.custom.rgs_android_dom.utils.gone
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visible
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PurchaseFragment :
    BaseBottomSheetFragment<PurchaseViewModel, FragmentPurchaseServiceBinding>(),
    PurchaseAddressFragment.PurchaseAddressListener,
    PurchaseCommentFragment.PurchaseCommentListener,
    PurchaseDateTimeFragment.PurchaseDateTimeListener,
    FragmentResultListener, ConfirmBottomSheetFragment.ConfirmListener {

    override val TAG: String = "PURCHASE_SERVICE_FRAGMENT"

    companion object {
        private const val ARG_PURCHASE_SERVICE_MODEL = "ARG_PURCHASE_SERVICE_MODEL"
        fun newInstance(
            purchaseModel: PurchaseModel
        ): PurchaseFragment = PurchaseFragment().args {
            putSerializable(ARG_PURCHASE_SERVICE_MODEL, purchaseModel)
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getSerializable(ARG_PURCHASE_SERVICE_MODEL) as PurchaseModel
        )
    }

    override fun getThemeResource(): Int = R.style.BottomSheetNoDim

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.setFragmentResultListener("email_request", viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener("agent_code_request", viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener("card_request", viewLifecycleOwner, this)

        binding.layoutProperty.root.setOnDebouncedClickListener {
            viewModel.onAddressClick(childFragmentManager)
        }
        binding.addCommmentTextView.setOnDebouncedClickListener {
            val editPurchaseServiceComment = PurchaseCommentFragment.newInstance()
            editPurchaseServiceComment.show(childFragmentManager, PurchaseCommentFragment.TAG)
        }
        binding.layoutDateTime.root.setOnDebouncedClickListener {
            viewModel.onDateTimeClick(childFragmentManager)
        }
        binding.layoutCardPayment.root.setOnDebouncedClickListener {
            viewModel.onCardClick(childFragmentManager)
        }
        binding.makeOrderButton.productArrangeBtn.setOnDebouncedClickListener {
            viewModel.makeOrder()
        }

        subscribe(viewModel.purchaseObserver) { purchase ->
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(purchase.iconLink))
                .transform(RoundedCorners(20.dp(requireContext())))
                .into(binding.layoutPurchaseServiceHeader.orderImageView)

            binding.layoutPurchaseServiceHeader.orderNameTextView.text = purchase.name

            binding.makeOrderButton

            purchase.price?.amount?.let { amount ->
                binding.layoutPurchaseServiceHeader.orderCostTextView.text =
                    DigitsFormatter.priceFormat(amount)
                binding.makeOrderButton.btnPrice.text = DigitsFormatter.priceFormat(amount)
            }

//            purchaseService.email?.let { email ->
//                binding.layoutEmail.sampleNameTextView.text = email
//            }
//            purchaseService.agentCode?.let { code ->
//                binding.layoutAgentCode.sampleNameTextView.text = code
//            }
            purchase.card?.let { card ->
                binding.layoutCardPayment.cardNumberTextView.text = card
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
                            R.drawable.ic_type_apartment
                        )
                    }
                }
            }

        }
    }

    override fun isNavigationViewVisible(): Boolean {
        return false
    }

    override fun onSelectPropertyClick(propertyItemModel: PropertyItemModel) {
        viewModel.updateAddress(propertyItemModel)
    }

    override fun onAddPropertyClick() {
        viewModel.onAddPropertyClick()
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

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            "email_request" -> {
                val email = result.getString("email_key")
                email?.let {
                    viewModel.updateEmail(it)
                }
            }
            "agent_code_request" -> {
                val status = result.getString("code_status_key")
                if (status == "1") {
                    val agentCode = result.getString("code_key")
                    agentCode?.let {
                        viewModel.updateAgentCode(it)
                    }
                } else {
                    val confirmDialog = ConfirmBottomSheetFragment.newInstance(
                        icon = R.drawable.ic_broken_egg,
                        title = "Невозможно обработать",
                        description = "К сожалению, мы не смогли найти и привязать код агента. Пожалуйста, попробуйте еще раз",
                        confirmText = "Попробовать ещё раз",
                        cancelText = "Нет, спасибо"
                    )
                    confirmDialog.show(childFragmentManager, ConfirmBottomSheetFragment.TAG)
                }
            }
            "card_request" -> {
                val card = result.getString("card_key")
                card?.let {
                    viewModel.updateCard(it)
                }
            }
        }
    }

    override fun onConfirmClick() {
        val addAgentBottomFragment = AddAgentBottomFragment()
        addAgentBottomFragment.show(childFragmentManager, addAgentBottomFragment.TAG)
    }

}
