package com.custom.rgs_android_dom.ui.catalog

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.TabCatalogFragment
import com.custom.rgs_android_dom.ui.catalog.tabs.products.TabProductsFragment

class MainCatalogPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            1 -> TabCatalogFragment()
            2 -> TabProductsFragment()
            else -> TabCatalogFragment()
        }
        return fragment
    }
}