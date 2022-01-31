package com.custom.rgs_android_dom.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingPagerAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {

    companion object {
        const val TABS_COUNT = 5
    }

    override fun getItemCount(): Int = TABS_COUNT

    override fun createFragment(position: Int): Fragment = TabOnboardingFragment.newInstance(position)

}