package com.custom.rgs_android_dom.ui.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentMainBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.NavigationScope

class MainFragment : BaseBottomSheetFragment<MainViewModel, FragmentMainBinding>() {

    override val TAG: String = "MAIN_FRAGMENT"

    private var isAuthorized = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginLinearLayout.setOnDebouncedClickListener {
            viewModel.onLoginClick()
        }

        binding.profileLinearLayout.setOnDebouncedClickListener {
            viewModel.onProfileClick()
        }

        binding.noPropertyLinearLayout.setOnDebouncedClickListener {
            viewModel.onNoPropertyClick()
        }

        binding.propertyAvailableLinearLayout.setOnDebouncedClickListener {
            viewModel.onPropertyAvailableClick()
        }

        binding.searchTagsLayout.tagsFlowLayout.children.forEach { view->
            (view as TextView).let {
                it.setOnDebouncedClickListener {
                    viewModel.onTagClick(it.text.toString())
                }
            }
        }

        binding.searchTagsLayout.searchCatalogCardView.setOnDebouncedClickListener {
            viewModel.onSearchClick()
        }

        subscribe(viewModel.registrationObserver) {
            isAuthorized = it
            binding.profileLinearLayout.visibleIf(it)
            binding.loginLinearLayout.goneIf(it)
            if (!it) {
                binding.propertyAvailableLinearLayout.gone()
                binding.noPropertyLinearLayout.gone()
            }
        }

        subscribe(viewModel.propertyAvailabilityObserver) {
            binding.propertyAvailableLinearLayout.visibleIf(it && isAuthorized)
            binding.noPropertyLinearLayout.goneIf(it  || !isAuthorized)
        }

    }

    override fun onClose() {
        hideSoftwareKeyboard()
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

    override fun getNavigationScope(): NavigationScope? {
        return NavigationScope.NAV_MAIN
    }
}