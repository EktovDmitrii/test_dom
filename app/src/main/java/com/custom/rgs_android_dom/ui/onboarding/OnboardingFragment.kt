package com.custom.rgs_android_dom.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentOnboardingBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingFragment :
    BaseFragment<OnboardingViewModel, FragmentOnboardingBinding>(R.layout.fragment_onboarding) {

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.onboarding_background)
    }

    private val viewPagerAdapter: OnboardingPagerAdapter
        get() = binding.onboardingViewPager.adapter as OnboardingPagerAdapter

    private val viewPagerChangeListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.skipTextView.visibleIf(position == OnboardingPagerAdapter.TABS_COUNT - 1)
            binding.nextTextView.setText(
                if (position == OnboardingPagerAdapter.TABS_COUNT - 1) "Войти"
                else "Далее", false
            )
            super.onPageSelected(position)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.onboardingViewPager.adapter = OnboardingPagerAdapter(this)
        binding.onboardingViewPager.isUserInputEnabled = false
        binding.onboardingViewPager.registerOnPageChangeCallback(viewPagerChangeListener)

        TabLayoutMediator(
            binding.onboardingTabLayout,
            binding.onboardingViewPager
        ) { tab, _ -> tab.view.isClickable = false }.attach()

        binding.nextTextView.setOnDebouncedClickListener {
            val curr = binding.onboardingViewPager.currentItem
            if (curr == OnboardingPagerAdapter.TABS_COUNT - 1) viewModel.onLoginClick()
            else binding.onboardingViewPager.currentItem = curr + 1
        }
        binding.skipTextView.setOnDebouncedClickListener {
            viewModel.onSkipClick()
        }

    }

    override fun onDestroy() {
        binding.onboardingViewPager.unregisterOnPageChangeCallback(viewPagerChangeListener)
        super.onDestroy()
    }

    override fun onClose() {
        if (viewModel.isClosableObserver.value == false)
            activity?.finish()
        else
            super.onClose()
    }

}