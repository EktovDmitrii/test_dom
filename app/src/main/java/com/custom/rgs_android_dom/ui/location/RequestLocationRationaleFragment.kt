package com.custom.rgs_android_dom.ui.location

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentRequestLocationRationaleBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.buildActivityResultRequest
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class RequestLocationRationaleFragment : BaseBottomSheetModalFragment<RequestLocationRationaleViewModel, FragmentRequestLocationRationaleBinding>() {

    override val TAG: String = "REQUEST_LOCATION_RATIONALE_FRAGMENT"

    private var onDismissListener: OnDismissListener? = null

    private var showAppPermissionsResult = buildActivityResultRequest {
        viewModel.onSettingsScreenClosed()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parentFragment = parentFragment
        if (parentFragment is OnDismissListener) {
            onDismissListener = parentFragment
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
        Log.d("MyLog", "On dismiss dialog")
        onDismissListener?.onDismiss()
    }

    interface OnDismissListener {
        fun onDismiss()
    }

}