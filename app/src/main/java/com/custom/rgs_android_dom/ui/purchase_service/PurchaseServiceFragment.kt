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
import com.custom.rgs_android_dom.domain.purchase_service.PurchaseServiceModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.confirm.ConfirmBottomSheetFragment
import com.custom.rgs_android_dom.ui.purchase_service.add_purchase_service_comment.EditPurchaseServiceCommentFragment
import com.custom.rgs_android_dom.ui.purchase_service.add_purchase_service_email.EditPurchaseServiceEmailFragment
import com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_service_address.EditPurchaseServiceAddressFragment
import com.custom.rgs_android_dom.utils.DigitsFormatter
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PurchaseServiceFragment :
    BaseBottomSheetFragment<PurchaseServiceViewModel, FragmentPurchaseServiceBinding>(),
    EditPurchaseServiceAddressFragment.EditPurchaseServiceAddressListener,
    EditPurchaseServiceEmailFragment.EditPurchaseServiceEmailListener,
    EditPurchaseServiceCommentFragment.EditPurchaseServiceCommentListener,
    FragmentResultListener, ConfirmBottomSheetFragment.ConfirmListener {
    override val TAG: String = "PURCHASE_SERVICE_FRAGMENT"

    companion object {
        private const val ARG_PURCHASE_SERVICE_MODEL = "ARG_PURCHASE_SERVICE_MODEL"
        fun newInstance(
            purchaseServiceModel: PurchaseServiceModel
        ): PurchaseServiceFragment = PurchaseServiceFragment().args {
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

        initViews()

        childFragmentManager.setFragmentResultListener("email_request", viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener("agent_code_request", viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener("card_request", viewLifecycleOwner, this)

        binding.layoutPurchaseServiceProperty.layout.setOnDebouncedClickListener {
            viewModel.onAddressClick(childFragmentManager)
        }
        binding.layoutPurchaseServiceMail.layout.setOnDebouncedClickListener {
            val editPurchaseServiceEmail = EditPurchaseServiceEmailFragment.newInstance()
            editPurchaseServiceEmail.show(childFragmentManager, EditPurchaseServiceEmailFragment.TAG)
        }
        binding.addCommmentTextView.setOnDebouncedClickListener {
            val editPurchaseServiceComment = EditPurchaseServiceCommentFragment.newInstance()
            editPurchaseServiceComment.show(childFragmentManager, EditPurchaseServiceCommentFragment.TAG)
        }
        binding.layoutPurchaseServiceDateTime.layout.setOnDebouncedClickListener {
            //Todo добавлю выбор времени
        }
        binding.makeOrderButton.productArrangeBtn.setOnDebouncedClickListener {
            //Todo добавить потом
        }

        subscribe(viewModel.purchaseServiceControllerObserver) { purchaseService ->
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
                binding.layoutPurchaseServiceProperty.propertyTypeTextView.text = it.name
                binding.layoutPurchaseServiceProperty.propertyAddressTextView.text =
                    it.address?.address
                when (it.type) {
                    PropertyType.HOUSE -> {
                        binding.layoutPurchaseServiceProperty.propertyTypeImageView.setImageResource(
                            R.drawable.ic_type_home
                        )
                    }
                    PropertyType.APARTMENT -> {
                        binding.layoutPurchaseServiceProperty.propertyTypeImageView.setImageResource(
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

    override fun onSaveMailClick(email: String) {
        viewModel.updateEmail(email)
    }

    override fun onSaveCommentClick(comment: String) {
        viewModel.updateComment(comment)
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
