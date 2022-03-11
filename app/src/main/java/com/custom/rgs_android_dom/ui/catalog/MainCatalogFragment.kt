package com.custom.rgs_android_dom.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentMainCatalogBinding
import com.custom.rgs_android_dom.databinding.TabCatalogBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.client.orders.OrdersFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.views.NavigationScope
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yandex.metrica.YandexMetrica
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf


class MainCatalogFragment :
    BaseBottomSheetFragment<MainCatalogViewModel, FragmentMainCatalogBinding>() {

    companion object {

        const val TAB_ALL = 0
        const val TAB_MY_PRODUCTS = 1

        private const val KEY_TAB = "tab"

        fun newInstance(tab: Int = TAB_ALL): MainCatalogFragment {
            return MainCatalogFragment().args {
                putInt(KEY_TAB, tab)
            }
        }

    }

    private val tabs = listOf(
        Pair(R.drawable.ic_tab_catalog, "Все"),
        Pair(R.drawable.ic_tab_products, "Мои продукты"),
        Pair(R.drawable.ic_tab_available_services, "Доступные услуги")
    )

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getInt(KEY_TAB))
    }

    override val TAG: String = "MAIN_CATALOG_FRAGMENT"

    private val pagerAdapter: MainCatalogPagerAdapter
        get() = binding.mainCatalogViewPager.adapter as MainCatalogPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainCatalogViewPager.adapter = MainCatalogPagerAdapter(this)
        binding.mainCatalogViewPager.isUserInputEnabled = false

        binding.searchCatalogCardView.root.setOnDebouncedClickListener {
            viewModel.onSearchClick()
        }
        val mediator =
            TabLayoutMediator(binding.tabLayout, binding.mainCatalogViewPager) { tab, position ->
                val tabCatalogBinding =
                    TabCatalogBinding.inflate(LayoutInflater.from(requireContext()), null, false)

                tabCatalogBinding.tabIconImageView.setImageResource(tabs[position].first)
                tabCatalogBinding.tabTitleTextView.text = tabs[position].second

                tab.customView = tabCatalogBinding.root
            }
        mediator.attach()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                YandexMetrica.reportEvent("catalog_section", "{\"catalog_section_item\":\"${tabs[tab.position]}\"}")
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        subscribe(viewModel.tabObserver) {
            when(it) {
                TAB_ALL -> { binding.mainCatalogViewPager.currentItem = TAB_ALL }
                TAB_MY_PRODUCTS -> {
                        binding.mainCatalogViewPager.setCurrentItem(TAB_MY_PRODUCTS,false)
                    }
                }
        }

    }

    fun navigateCatalog() {
        binding.mainCatalogViewPager.currentItem = TAB_ALL
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

    override fun getNavigationScope(): NavigationScope? {
        return NavigationScope.NAV_CATALOG
    }

}
