package com.custom.rgs_android_dom.ui.purchase_service.add_purchase_service_comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPurchaseCommentBinding
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable

class PurchaseCommentFragment : BottomSheetDialogFragment() {

    private val binding: FragmentPurchaseCommentBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private var editPurchaseServiceCommentListener: EditPurchaseServiceCommentListener? = null

    companion object {
        const val TAG: String = "EDIT_PURCHASE_SERVICE_COMMENT_FRAGMENT"

        fun newInstance(): PurchaseCommentFragment =
            PurchaseCommentFragment()
    }

    override fun getTheme(): Int {
        return R.style.BottomSheet
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is EditPurchaseServiceCommentListener) {
            editPurchaseServiceCommentListener = parentFragment as EditPurchaseServiceCommentListener
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveTextView.setOnDebouncedClickListener {
            editPurchaseServiceCommentListener?.onSaveCommentClick(binding.commentTextInputLayout.getText())
            dismissAllowingStateLoss()
        }
    }

    interface EditPurchaseServiceCommentListener : Serializable {
        fun onSaveCommentClick(comment: String)
    }
}

