package com.custom.rgs_android_dom.ui.purchase_service

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentResultListener
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentPurchaseServiceBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchaseDateTimeModel
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchaseServiceModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.confirm.ConfirmBottomSheetFragment
import com.custom.rgs_android_dom.ui.purchase_service.add_purchase_service_comment.PurchaseCommentFragment
import com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_date_time.PurchaseDateTimeFragment
import com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_service_address.PurchaseAddressFragment
import com.custom.rgs_android_dom.utils.DigitsFormatter
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
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
            purchaseServiceModel: PurchaseServiceModel
        ): PurchaseFragment = PurchaseFragment().args {
            putSerializable(ARG_PURCHASE_SERVICE_MODEL, purchaseServiceModel)
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getSerializable(ARG_PURCHASE_SERVICE_MODEL) as PurchaseServiceModel
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
            //Todo Нуржик доделает
        }
        binding.makeOrderButton.productArrangeBtn.setOnDebouncedClickListener {
            //Todo добавить потом
        }

        subscribe(viewModel.purchaseServiceObserver) { purchaseService ->
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(purchaseService.iconLink))
                .transform(RoundedCorners(20.dp(requireContext())))
                .into(binding.layoutPurchaseServiceHeader.orderImageView)

            binding.layoutPurchaseServiceHeader.orderNameTextView.text = purchaseService.name

            purchaseService.price?.amount?.let { amount ->
                binding.layoutPurchaseServiceHeader.orderCostTextView.text =
                    DigitsFormatter.priceFormat(amount)
                binding.makeOrderButton.btnPrice.text = DigitsFormatter.priceFormat(amount)
            }

            purchaseService.email?.let { email ->
                binding.layoutPurchaseServiceMail.sampleNameTextView.text = email
            }
            purchaseService.agentCode?.let { code ->
                binding.layoutPurchaseServiceAgentCode.sampleNameTextView.text = code
            }
            purchaseService.card?.let { card ->
                binding.layoutPurchaseServiceCardPayment.cardNumberTextView.text = card
            }

            purchaseService.propertyItemModel?.let {
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
        viewModel.updateDateTime(purchaseDateTimeModel)
    }

    //Todo подумал, что это удобно вынести в отдельный метод
    private fun initViews() {
        binding.layoutPurchaseServiceDateTime.sampleImageView.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_clock_black
            )
        )
        binding.layoutPurchaseServiceDateTime.sampleNameTextView.text = "Выберите дату и время"

        binding.layoutPurchaseServiceMail.sampleImageView.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_mail_black
            )
        )
        binding.layoutPurchaseServiceMail.sampleNameTextView.text = "Добавить почту для чека"

        binding.layoutPurchaseServiceAgentCode.sampleImageView.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_agent_black
            )
        )
        binding.layoutPurchaseServiceAgentCode.sampleNameTextView.text = "Я знаю код агента"
        binding.makeOrderButton.btnTitle.text = "Заказать"

        binding.layoutPurchaseServiceCardPayment.root.setOnDebouncedClickListener {
            val selectCardBottomFragment = SelectCardBottomFragment()
            selectCardBottomFragment.show(childFragmentManager, selectCardBottomFragment.TAG)
        }
        binding.layoutPurchaseServiceMail.root.setOnDebouncedClickListener {
            val addEmailBottomFragment = AddEmailBottomFragment()
            addEmailBottomFragment.show(childFragmentManager, addEmailBottomFragment.TAG)
        }
        binding.layoutPurchaseServiceAgentCode.root.setOnDebouncedClickListener {
            val addAgentBottomFragment = AddAgentBottomFragment()
            addAgentBottomFragment.show(childFragmentManager, addAgentBottomFragment.TAG)
        }
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
