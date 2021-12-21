package com.custom.rgs_android_dom.ui.catalog

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.custom.rgs_android_dom.ui.catalog.tabs.availableservices.TabAvailableServicesFragment
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.TabCatalogFragment
import com.custom.rgs_android_dom.ui.catalog.tabs.favoriteservices.TabFavoriteServicesFragment
import com.custom.rgs_android_dom.ui.catalog.tabs.products.TabProductsFragment


class MainCatalogPagerAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {

    companion object {
        private const val TABS_COUNT = 4
        private const val TAB_CATALOG = 0
        private const val TAB_PRODUCTS = 1
        private const val TAB_AVAILABLE_SERVICES = 2
        private const val TAB_FAVORITE_SERVICES = 3
    }

    override fun getItemCount(): Int {
        return TABS_COUNT
    }

    override fun createFragment(position: Int): Fragment {

        val fragment = when (position) {
            TAB_CATALOG -> TabCatalogFragment()
            TAB_PRODUCTS -> TabProductsFragment()
            TAB_AVAILABLE_SERVICES -> TabAvailableServicesFragment()
            TAB_FAVORITE_SERVICES -> TabFavoriteServicesFragment()
            else -> TabCatalogFragment()
        }

        return fragment
    }

}