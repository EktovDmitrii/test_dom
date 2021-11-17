package com.custom.rgs_android_dom.ui.alert

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.custom.my_service_android_client.R
import com.custom.my_service_android_client.utils.setOnDebouncedClickListener
import kotlinx.android.synthetic.main.fragment_alert_dialog.*

class AlertDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "ConfirmationDialog"

        const val DEFAULT_REQUEST_CODE = -1
        const val REQUEST_CODE_SHOW_APP_SETTINGS = 990765
        const val REQUEST_CODE_SHOW_LOCATION_SETTINGS = 99076599

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_alert_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageTextView.text = arguments?.getString(ARGUMENT_MESSAGE)
        positiveTextView.text = arguments?.getString(ARGUMENT_POSITIVE_TEXT)
        negativeTextView.text = arguments?.getString(ARGUMENT_NEGATIVE_TEXT)
        val requestCode = arguments?.getInt(ARGUMENT_REQUEST_CODE) ?: DEFAULT_REQUEST_CODE

        positiveTextView.setOnDebouncedClickListener {
            onAlertClickListener?.onPositiveButtonClick(requestCode)
            dismissAllowingStateLoss()
        }

        negativeTextView.setOnDebouncedClickListener {
            onAlertClickListener?.onNegativeButtonClick(requestCode)
            dismissAllowingStateLoss()
        }
    }
}

interface OnAlertClickListener {
    fun onPositiveButtonClick(requestCode: Int) {}
    fun onNegativeButtonClick(requestCode: Int) {}
}