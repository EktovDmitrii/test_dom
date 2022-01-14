package com.custom.rgs_android_dom.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentMainCatalogBinding
import com.custom.rgs_android_dom.databinding.TabCatalogBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.views.NavigationScope
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf


class MainCatalogFragment :
    BaseBottomSheetFragment<MainCatalogViewModel, FragmentMainCatalogBinding>() {

    enum class CatalogTab {
        ALL,
        MY_PRODUCTS
    }

    companion object {

        private const val KEY_TAB = "tab"

        fun newInstance(tab: String = CatalogTab.ALL.toString()): MainCatalogFragment {
            return MainCatalogFragment().args {
                putString(KEY_TAB, tab)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(KEY_TAB))
    }

    override val TAG: String = "MAIN_CATALOG_FRAGMENT"

    private val tabs = listOf(
        Pair(R.drawable.ic_tab_catalog, "Все"),
        Pair(R.drawable.ic_tab_products, "Мои продукты"),
        Pair(R.drawable.ic_tab_available_services, "Доступные услуги")
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

        subscribe(viewModel.tabObserver) {
            when(it) {
                CatalogTab.ALL.name -> tabs.find { tab -> tab.second == "Все" }?.let { tab -> binding.mainCatalogViewPager.currentItem = tabs.indexOf(tab) }
                CatalogTab.MY_PRODUCTS.name -> tabs.find { tab -> tab.second == "Мои продукты" }?.let { tab -> binding.mainCatalogViewPager.currentItem = tabs.indexOf(tab) }
            }
        }
    }

    fun navigateCatalog() {
        binding.mainCatalogViewPager.currentItem = 0
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

    override fun getNavigationScope(): NavigationScope? {
        return NavigationScope.NAV_CATALOG
    }

}