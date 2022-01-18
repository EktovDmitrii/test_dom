package com.custom.rgs_android_dom.ui.rationale

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.content.ContextCompat
import com.custom.rgs_android_dom.databinding.FragmentRequestRationaleBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.buildActivityResultRequest
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class RequestRationaleFragment : BaseBottomSheetModalFragment<RequestRationaleViewModel, FragmentRequestRationaleBinding>() {

    companion object {
        private const val ARG_REQUEST_CODE = "ARG_REQUEST_CODE"
        private const val ARG_DESCRIPTION = "ARG_DESCRIPTION"
        private const val ARG_ICON = "ARG_ICON"

        fun newInstance(requestCode: Int, description: String, icon: Int): RequestRationaleFragment {
            return RequestRationaleFragment().args {
                putInt(ARG_REQUEST_CODE, requestCode)
                putString(ARG_DESCRIPTION, description)
                putInt(ARG_ICON, icon)
            }
        }
    }

    override val TAG: String = "REQUEST_RATIONALE_FRAGMENT"

    private var onRequestRationaleDismissListener: OnRequestRationaleDismissListener? = null
    private var requestCode: Int? = null

    private var showAppPermissionsResult = buildActivityResultRequest {
        viewModel.onSettingsScreenClosed()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parentFragment = parentFragment
        if (parentFragment is OnRequestRationaleDismissListener) {
            onRequestRationaleDismissListener = parentFragment
        }

        requestCode = if (requireArguments().containsKey(ARG_REQUEST_CODE)){
            requireArguments().getInt(ARG_REQUEST_CODE)
        } else {
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val description = requireArguments().getString(ARG_DESCRIPTION)
        val icon = requireArguments().getInt(ARG_ICON)

        binding.descriptionTextView.text = description
        binding.iconImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), icon))

        binding.openSettingsTextView.setOnDebouncedClickListener {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", requireContext().packageName, null)
            )
            showAppPermissionsResult.launch(intent)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onRequestRationaleDismissListener?.onRequestRationaleDismiss(requestCode)
    }

    interface OnRequestRationaleDismissListener {
        fun onRequestRationaleDismiss(requestCode: Int?)
    }

}