package com.custom.rgs_android_dom.ui.purchase.add.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentAddCommentBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.expand
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import java.io.Serializable

class AddCommentFragment : BaseBottomSheetModalFragment<AddCommentViewModel, FragmentAddCommentBinding>() {

    companion object {
        private const val ARG_COMMENT = "ARG_COMMENT"

        fun newInstance(comment: String?): AddCommentFragment {
            return AddCommentFragment().args {
                putString(ARG_COMMENT, comment)
            }
        }
    }

    override val TAG = "ADD_COMMENT_FRAGMENT"

    private var purchaseCommentListener: PurchaseCommentListener? = null

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_COMMENT))
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (parentFragment is PurchaseCommentListener) {
            purchaseCommentListener = parentFragment as PurchaseCommentListener
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expand()

        binding.saveTextView.setOnDebouncedClickListener {
            purchaseCommentListener?.onSaveCommentClick(binding.commentTextInputLayout.getText())
            onClose()
        }

        subscribe(viewModel.commentObserver){
            binding.commentTextInputLayout.setText(it)

            binding.commentTextInputLayout.setSelection(it.length)

            binding.commentTextInputLayout.addTextWatcher {
                binding.saveTextView.isEnabled = true
            }
        }

    }
}

interface PurchaseCommentListener : Serializable {
    fun onSaveCommentClick(comment: String)
}