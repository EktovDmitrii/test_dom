package com.custom.rgs_android_dom.ui.alert

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.custom.rgs_android_dom.databinding.FragmentAlertDialogBinding
import com.custom.rgs_android_dom.ui.confirm.ConfirmBottomSheetFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visibleIf

class AlertDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "AlertDialogFragment"

        const val DEFAULT_REQUEST_CODE = -1

        private const val ARGUMENT_MESSAGE = "ARGUMENT_MESSAGE"
        private const val ARGUMENT_POSITIVE_TEXT = "ARGUMENT_POSITIVE_TEXT"
        private const val ARGUMENT_NEGATIVE_TEXT = "ARGUMENT_NEGATIVE_TEXT"
        private const val ARGUMENT_REQUEST_CODE = "ARGUMENT_REQUEST_CODE"

        fun newInstance(
            message: String? = null,
            positiveText: String,
            negativeText: String? = null,
            requestCode: Int = DEFAULT_REQUEST_CODE
        ): AlertDialogFragment {
            return AlertDialogFragment().apply {
                arguments = bundleOf(
                    ARGUMENT_MESSAGE to message,
                    ARGUMENT_POSITIVE_TEXT to positiveText,
                    ARGUMENT_NEGATIVE_TEXT to negativeText,
                    ARGUMENT_REQUEST_CODE to requestCode
                )
            }
        }
    }

    private val binding: FragmentAlertDialogBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private var confirmListener: ConfirmBottomSheetFragment.ConfirmListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (parentFragment is ConfirmBottomSheetFragment.ConfirmListener){
            confirmListener = parentFragment as ConfirmBottomSheetFragment.ConfirmListener
        }
        return binding.root
    }

    private var onAlertClickListener: OnAlertClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parentFragment = parentFragment
        if (parentFragment is OnAlertClickListener) {
            onAlertClickListener = parentFragment
        }
    }

    override fun onDetach() {
        super.onDetach()
        onAlertClickListener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.messageTextView.text = arguments?.getString(ARGUMENT_MESSAGE)

        binding.positiveTextView.text = arguments?.getString(ARGUMENT_POSITIVE_TEXT)
        binding.positiveTextView.visibleIf(binding.positiveTextView.text.isNotEmpty())

        binding.negativeTextView.text = arguments?.getString(ARGUMENT_NEGATIVE_TEXT)
        binding.negativeTextView.visibleIf(binding.negativeTextView.text.isNotEmpty())

        val requestCode = arguments?.getInt(ARGUMENT_REQUEST_CODE) ?: DEFAULT_REQUEST_CODE

        binding.positiveTextView.setOnDebouncedClickListener {
            onAlertClickListener?.onPositiveButtonClick(requestCode)
            dismissAllowingStateLoss()
        }

        binding.negativeTextView.setOnDebouncedClickListener {
            onAlertClickListener?.onNegativeButtonClick(requestCode)
            dismissAllowingStateLoss()
        }
    }
}

interface OnAlertClickListener {
    fun onPositiveButtonClick(requestCode: Int) {}
    fun onNegativeButtonClick(requestCode: Int) {}
}