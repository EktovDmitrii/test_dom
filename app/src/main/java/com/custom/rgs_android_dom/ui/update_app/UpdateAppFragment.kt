package com.custom.rgs_android_dom.ui.update_app

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentUpdateAppBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class UpdateAppFragment : BaseBottomSheetFragment<UpdateAppViewModel, FragmentUpdateAppBinding>() {

    override val TAG: String = "UPDATE_APP_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.notNowTextView.gone()

        binding.updateTextView.setOnDebouncedClickListener {
            viewModel.onUpdateClick()
        }

        subscribe(viewModel.startUpdateObserver) {
            navigateToGooglePlay()
        }

    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheet
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkAppUpdates()
    }

    private fun navigateToGooglePlay() {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=ru.moi_service.property")
                setPackage("com.android.vending")
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://play.google.com/store/apps/details?id=ru.moi_service.property")
                }
                startActivity(intent)
            } catch (err: Exception) {
                logException(this, err)
            }
        }
    }

}