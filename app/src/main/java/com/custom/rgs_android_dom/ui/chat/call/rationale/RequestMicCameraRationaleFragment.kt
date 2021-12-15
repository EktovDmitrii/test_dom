package com.custom.rgs_android_dom.ui.chat.call.rationale

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentRequestMicCameraRationaleBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.buildActivityResultRequest
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class RequestMicCameraRationaleFragment : BaseBottomSheetModalFragment<RequestMicCameraRationaleViewModel, FragmentRequestMicCameraRationaleBinding>() {

    companion object {
        private const val ARG_REQUEST_CODE = "ARG_REQUEST_CODE"

        fun newInstance(requestCode: Int): RequestMicCameraRationaleFragment {
            return RequestMicCameraRationaleFragment().args {
                putInt(ARG_REQUEST_CODE, requestCode)
            }
        }
    }

    override val TAG: String = "REQUEST_MIC_CAMERA_RATIONALE_FRAGMENT"

    private var onDialogDismissListener: OnDialogAskRationaleDismissListener? = null
    private var requestCode: Int? = null

    private var showAppPermissionsResult = buildActivityResultRequest {
        viewModel.onSettingsScreenClosed()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parentFragment = parentFragment
        if (parentFragment is OnDialogAskRationaleDismissListener) {
            onDialogDismissListener = parentFragment
        }

        requestCode = if (requireArguments().containsKey(ARG_REQUEST_CODE)){
            requireArguments().getInt(ARG_REQUEST_CODE)
        } else {
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.openLocationSettingsTextView.setOnDebouncedClickListener {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", requireContext().packageName, null)
            )
            showAppPermissionsResult.launch(intent)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogDismissListener?.onDialogAskRationaleDismiss(requestCode)
    }

    interface OnDialogAskRationaleDismissListener {
        fun onDialogAskRationaleDismiss(requestCode: Int?)
    }

}