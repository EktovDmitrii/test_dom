package com.custom.rgs_android_dom.ui.update_app

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentUpdateAppBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.*
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class UpdateAppFragment : BaseBottomSheetFragment<UpdateAppViewModel, FragmentUpdateAppBinding>() {

    override val TAG: String = "UPDATE_APP_FRAGMENT"

    companion object {
        private const val ARG_IS_UPDATE_FORCED = "ARG_IS_UPDATE_FORCED"
        private const val REQUEST_CODE_UPDATE_APP = 10081988

        fun newInstance(isUpdateForced: Boolean): UpdateAppFragment {
            return UpdateAppFragment().args {
                putBoolean(ARG_IS_UPDATE_FORCED, isUpdateForced)
            }
        }
    }

    private var appUpdateManager: AppUpdateManager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = !requireArguments().getBoolean(ARG_IS_UPDATE_FORCED)

        appUpdateManager = AppUpdateManagerFactory.create(requireActivity())

        binding.updateTextView.setOnDebouncedClickListener {
            viewModel.onUpdateClick()
        }

        binding.notNowTextView.setOnDebouncedClickListener {
            viewModel.onNotNowClick()
        }

        subscribe(viewModel.startUpdateObserver) {
            startUpdateFlow()
        }
    }

    override fun getThemeResource(): Int {
        return if (requireArguments().getBoolean(ARG_IS_UPDATE_FORCED)) {
            R.style.BottomSheet
        } else {
            super.getTheme()
        }
    }

    private fun startUpdateFlow() {
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo->
            appUpdateManager?.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.IMMEDIATE,
                requireActivity(),
                REQUEST_CODE_UPDATE_APP)
        }

    }

    override fun onResume() {
        super.onResume()
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager?.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    requireActivity(),
                    REQUEST_CODE_UPDATE_APP)
            } else {
                viewModel.close()
            }
        }
    }


}