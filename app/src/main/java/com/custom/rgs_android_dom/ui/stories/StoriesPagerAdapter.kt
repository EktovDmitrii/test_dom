package com.custom.rgs_android_dom.ui.stories

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.custom.rgs_android_dom.ui.stories.tabs.TabGuaranteeFragment
import com.custom.rgs_android_dom.ui.stories.tabs.TabNewServiceFragment
import com.custom.rgs_android_dom.ui.stories.tabs.TabSupportFragment

class StoriesPagerAdapter(
    val fragment: Fragment
) : FragmentStateAdapter(fragment) {

    companion object {
        private const val TABS_COUNT = 3
        private const val TAB_NEW_SERVICE = 0
        private const val TAB_GUARANTEE = 1
        private const val TAB_SUPPORT = 2
    }

    override fun getItemCount(): Int {
        return TABS_COUNT
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            TAB_NEW_SERVICE -> TabNewServiceFragment()
            TAB_GUARANTEE -> TabGuaranteeFragment()
            TAB_SUPPORT -> TabSupportFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
}
