package com.custom.rgs_android_dom.ui.purchase.add.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentAddCommentBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.ui.purchase.add.email.AddEmailViewModel
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import java.io.Serializable

class AddCommentFragment : BaseBottomSheetModalFragment<AddEmailViewModel, FragmentAddCommentBinding>() {

    private var purchaseCommentListener: PurchaseCommentListener? = null

    override val TAG = "ADD_COMMENT_FRAGMENT"

    override fun getTheme(): Int {
        return R.style.BottomSheet
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (parentFragment is PurchaseCommentListener) {
            purchaseCommentListener = parentFragment as PurchaseCommentListener
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.commentTextInputLayout.addTextWatcher {
            binding.saveTextView.isEnabled = it.isNotEmpty()
        }

        binding.saveTextView.setOnDebouncedClickListener {
            purchaseCommentListener?.onSaveCommentClick(binding.commentTextInputLayout.getText())
            dismissAllowingStateLoss()
        }
    }

    interface PurchaseCommentListener : Serializable {
        fun onSaveCommentClick(comment: String)
    }
}

