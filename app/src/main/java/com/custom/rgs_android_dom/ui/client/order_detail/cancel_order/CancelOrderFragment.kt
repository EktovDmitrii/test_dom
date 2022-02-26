package com.custom.rgs_android_dom.ui.client.order_detail.cancel_order

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentCancelOrderBinding
import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class CancelOrderFragment : BaseBottomSheetFragment<CancelOrderViewModel, FragmentCancelOrderBinding>() {

    companion object {
        private const val ARG_ORDER = "ARG_ORDER"

        fun newInstance(order: Order): CancelOrderFragment {
            return CancelOrderFragment().args {
                putSerializable(ARG_ORDER, order)
            }
        }
    }

    override val TAG = "CANCEL_ORDER_FRAGMENT"

    override fun getThemeResource(): Int {
        return R.style.BottomSheet
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_ORDER) as Order)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelOrderTextView.setOnDebouncedClickListener {
            viewModel.onCancelOrderClick()
        }

        binding.closeTextView.setOnDebouncedClickListener {
            dismissAllowingStateLoss()
        }

        binding.successTextView.setOnDebouncedClickListener {
            dismissAllowingStateLoss()
        }

        subscribe(viewModel.orderCancelledObserver){
            binding.closeTextView.gone()
            binding.cancelOrderTextView.gone()

            binding.successTextView.visible()
            binding.titleTextView.text = "Заявка отправлена.\nОператор свяжется с вами"
            binding.iconImageView.setImageResource(R.drawable.ic_success_question)
        }

    }

    override fun onLoading() {
        super.onLoading()

        binding.cancelOrderTextView.setLoading(true)

        binding.closeTextView.gone()
        binding.successTextView.gone()
    }

    override fun onError() {
        super.onError()

        binding.cancelOrderTextView.setLoading(false)

        binding.closeTextView.visible()
        binding.successTextView.gone()
    }

    override fun onContent() {
        super.onContent()

        binding.cancelOrderTextView.setLoading(false)

        binding.closeTextView.visible()
        binding.successTextView.gone()
    }

}
