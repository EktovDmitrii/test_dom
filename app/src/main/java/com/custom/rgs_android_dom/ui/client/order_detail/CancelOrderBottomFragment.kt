package com.custom.rgs_android_dom.ui.client.order_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentBottomOrderCancelBinding
import com.custom.rgs_android_dom.ui.property.document.edit_document.EditDocumentBottomSheetFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CancelOrderBottomFragment : BottomSheetDialogFragment() {

    private var cancelOrderListener: CancelOrderListener? = null

    private val binding: FragmentBottomOrderCancelBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    companion object {
        const val TAG: String = "CANCEL_ORDER_FRAGMENT"
        fun newInstance(): CancelOrderBottomFragment =
            CancelOrderBottomFragment()
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetTransparentBackground
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is EditDocumentBottomSheetFragment.EditDocumentListener) {
            cancelOrderListener = parentFragment as CancelOrderListener
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.applyTextView.setOnDebouncedClickListener {
            cancelOrderListener?.onCancelOrderClick()
            dismissAllowingStateLoss()
        }
        binding.cancelTextView.setOnDebouncedClickListener {
            dismissAllowingStateLoss()
        }
    }

    interface CancelOrderListener {
        fun onCancelOrderClick()
    }
}
