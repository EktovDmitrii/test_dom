package com.custom.rgs_android_dom.ui.client.order_detail

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentOrderDetailBinding
import com.custom.rgs_android_dom.databinding.ItemOrderGeneralInvoiceBinding
import com.custom.rgs_android_dom.databinding.ItemOrderGeneralInvoiceServiceBinding
import com.custom.rgs_android_dom.domain.client.models.*
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class OrderDetailFragment :
    BaseFragment<OrderDetailViewModel, FragmentOrderDetailBinding>(R.layout.fragment_order_detail),
    CancelOrderBottomFragment.CancelOrderListener {

    companion object {
        private const val ARG_ORDER_ID = "ARG_ORDER_ID"

        fun newInstance(order: Order): OrderDetailFragment {
            return OrderDetailFragment().args {
                putSerializable(ARG_ORDER_ID, order)
            }
        }
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary400)
    }

    override fun onCancelOrderClick() {
        viewModel.onCancelOrderClick()
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getSerializable(ARG_ORDER_ID)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }
        binding.feedbackImageView.setOnDebouncedClickListener {
            viewModel.onFeedbackClick()
        }
        binding.cancelOrderTextView.setOnDebouncedClickListener {
            val cancelOrderBottomSheetFragment = CancelOrderBottomFragment.newInstance()
            cancelOrderBottomSheetFragment.show(
                childFragmentManager,
                CancelOrderBottomFragment.TAG
            )
        }

        subscribe(viewModel.orderViewStateObserver) { state ->
            val service = if (state.services?.isNotEmpty() == true) state.services[0] else null
            binding.orderStateTextView.text = state.getOrderStateTitle()
            initStaticProgressView(state.status)
            binding.topPaymentStateTextView.text = state.getPaymentState()
            binding.serviceNameTextView.text = service?.serviceName
            binding.addressTextView.text = "${state.address?.objectName} (${state.address?.address})"
            binding.addressTitleTextView.visibleIf(state.address?.address?.isNotBlank() == true)
            binding.addressTextView.visibleIf(state.address?.address?.isNotBlank() == true)
            binding.dateTimeTextView.text = state.getDateTime()
            binding.commentTextView.text = state.comment
            binding.commentTitleTextView.visibleIf(state.comment?.isNotBlank() == true)
            binding.commentTextView.visibleIf(state.comment?.isNotBlank() == true)
            binding.productTitleTextView.text = service?.productName
            service?.productIcon?.let { logo ->
                GlideApp.with(requireContext())
                    .load(GlideUrlProvider.makeHeadersGlideUrl(logo))
                    .transform(RoundedCorners(4.dp(requireContext())))
                    .into(binding.productIconImageView)
            }
            binding.priceTextView.text = (service?.productPrice ?: 0).formatPrice()
            binding.paymentStateTextView.text = Html.fromHtml(
                state.getPaymentStateWithDate(),
                Html.FROM_HTML_MODE_LEGACY
            )
            val isDefaultProduct = service?.defaultProduct ?: false
            val orderStateForPay = state.status == OrderStatus.DRAFT
            binding.billPayTextView.visibleIf(isDefaultProduct && orderStateForPay)
            binding.productContainer.visibleIf(!isDefaultProduct)
            binding.paymentTypeTitleTextView.visibleIf(!isDefaultProduct)
            binding.priceContainer.goneIf(!isDefaultProduct)
            binding.priceTitleTextView.goneIf(!isDefaultProduct)
            initGeneralInvoices(state.status, state.generalInvoice)
            initCancelOrderButton(state.status)
            binding.feedbackImageView.setOnDebouncedClickListener {
                viewModel.onFeedbackClick()
            }
        }
    }

    private fun initGeneralInvoices(status: OrderStatus, invoices: List<GeneralInvoice>?) {
        binding.generalInvoicesContainer.removeAllViews()
        invoices?.forEach {
            ItemOrderGeneralInvoiceBinding.inflate(LayoutInflater.from(context), binding.generalInvoicesContainer, false).apply {
                this.fullPriceTextView.text = it.getFullPrice().formatPrice()
                paymentStateTextView.visibleIf(status == OrderStatus.CANCELLED)
                paymentStateTextView.text = "Счёт аннулирован"
                it.items.map { invoiceItem ->
                    initServiceItem(servicesContainer, invoiceItem)
                }
                binding.generalInvoicesContainer.addView(root)
            }
        }
    }

    private fun initServiceItem(container: LinearLayoutCompat, invoiceItemModel: GeneralInvoiceItem) {
        ItemOrderGeneralInvoiceServiceBinding.inflate(LayoutInflater.from(context), container, false).apply {
            serviceNameTextView.text = invoiceItemModel.getNameWithAmount()
            counterTextView.visibleIf(invoiceItemModel.quantity != null && invoiceItemModel.quantity > 1)
            counterTextView.text = invoiceItemModel.quantity?.toString()
            priceTextView.text = invoiceItemModel.getPriceText()
            container.addView(root)
        }
    }

    private fun initCancelOrderButton(orderStatus: OrderStatus) {
         when (orderStatus) {
             OrderStatus.DRAFT, OrderStatus.CONFIRMED -> binding.cancelOrderTextView.visible()
             else -> binding.cancelOrderTextView.gone()
         }
    }

    private fun initStaticProgressView(orderStatus: OrderStatus) {
        val activeColor = ContextCompat.getColor(requireContext(), R.color.primary500)
        val inactiveColor = ContextCompat.getColor(requireContext(), R.color.secondary100)
        val changeFirstLineColor: (Int) -> Unit = {
            binding.staticProgress.firstLine.backgroundTintList = ColorStateList.valueOf(it)
        }
        val changeSecondLineColor: (Int) -> Unit = {
            binding.staticProgress.secondLine.backgroundTintList = ColorStateList.valueOf(it)
        }
        val changeThirdLineColor: (Int) -> Unit = {
            binding.staticProgress.thirdLine.backgroundTintList = ColorStateList.valueOf(it)
        }
        val changeFourthLineColor: (Int) -> Unit = {
            binding.staticProgress.fourthLine.backgroundTintList = ColorStateList.valueOf(it)
        }
        when (orderStatus) {
            OrderStatus.DRAFT -> {
                changeFirstLineColor(activeColor)
                changeSecondLineColor(inactiveColor)
                changeThirdLineColor(inactiveColor)
                changeFourthLineColor(inactiveColor)
            }
            OrderStatus.CONFIRMED -> {
                changeFirstLineColor(activeColor)
                changeSecondLineColor(activeColor)
                changeThirdLineColor(inactiveColor)
                changeFourthLineColor(inactiveColor)
            }
            OrderStatus.ACTIVE -> {
                changeFirstLineColor(activeColor)
                changeSecondLineColor(activeColor)
                changeThirdLineColor(activeColor)
                changeFourthLineColor(inactiveColor)
            }
            OrderStatus.RESOLVED -> {
                changeFirstLineColor(activeColor)
                changeSecondLineColor(activeColor)
                changeThirdLineColor(activeColor)
                changeFourthLineColor(activeColor)
            }
            else -> {
                val canceledColor = ContextCompat.getColor(requireContext(), R.color.secondary400)
                changeFirstLineColor(canceledColor)
                changeSecondLineColor(canceledColor)
                changeThirdLineColor(canceledColor)
                changeFourthLineColor(canceledColor)

                binding.topPaymentStateTextView.setTextColor(canceledColor)
            }
        }
    }
}
