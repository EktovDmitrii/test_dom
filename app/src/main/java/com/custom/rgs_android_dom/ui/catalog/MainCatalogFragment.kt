package com.custom.rgs_android_dom.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentMainCatalogBinding
import com.custom.rgs_android_dom.databinding.TabCatalogBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.google.android.material.tabs.TabLayoutMediator


class MainCatalogFragment :
    BaseBottomSheetFragment<MainCatalogViewModel, FragmentMainCatalogBinding>() {

    override val TAG: String = "MAIN_CATALOG_FRAGMENT"

    private val tabs = listOf(
        Pair(R.drawable.ic_tab_catalog, "Каталог"),
        Pair(R.drawable.ic_tab_products, "Мои продукты"),
        Pair(R.drawable.ic_tab_available_services, "Доступные услуги"),
        Pair(R.drawable.ic_tab_favorite_services, "Избранные услуги")
    )

    private val pagerAdapter: MainCatalogPagerAdapter
        get() = binding.mainCatalogViewPager.adapter as MainCatalogPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainCatalogViewPager.adapter = MainCatalogPagerAdapter(this)
        binding.mainCatalogViewPager.isUserInputEnabled = false

        val mediator =
            TabLayoutMediator(binding.tabLayout, binding.mainCatalogViewPager) { tab, position ->
                val tabCatalogBinding =
                    TabCatalogBinding.inflate(LayoutInflater.from(requireContext()), null, false)

                tabCatalogBinding.tabIconImageView.setImageResource(tabs[position].first)
                tabCatalogBinding.tabTitleTextView.text = tabs[position].second

                tab.customView = tabCatalogBinding.root
            }

        mediator.attach()
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }
}