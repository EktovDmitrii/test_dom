package com.custom.rgs_android_dom.ui.client.order_detail

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.responses.OrderStatus
import com.custom.rgs_android_dom.databinding.FragmentOrderDetailBinding
import com.custom.rgs_android_dom.domain.client.models.Order
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
            binding.orderStateTextView.text = state.getOrderStateTitle()
            initStaticProgressView(state.orderStatus)
            binding.topPaymentStateTextView.text = state.paymentStatus
            binding.serviceNameTextView.text = state.serviceName
            binding.addressTextView.text = state.address
            binding.dateTimeTextView.text = state.dateTime
            binding.commentTextView.text = state.comment
            binding.commentTitleTextView.visibleIf(state.comment.isNotBlank())
            binding.commentTextView.visibleIf(state.comment.isNotBlank())
            initCancelOrderButton(state.orderStatus)
        }
    }

    private fun initCancelOrderButton(orderStatus: OrderStatus) {
         when (orderStatus) {
             OrderStatus.DRAFT -> binding.cancelOrderTextView.visible()
             OrderStatus.CONFIRMED -> binding.cancelOrderTextView.visible()
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
            }
        }
    }
}
