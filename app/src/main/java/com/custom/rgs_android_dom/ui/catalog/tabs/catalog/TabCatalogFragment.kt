package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentTabCatalogBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.subcategories.CatalogSubcategoriesFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.recycler_view.VerticalItemDecoration
import com.custom.rgs_android_dom.utils.setStatusBarColor
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf

class TabCatalogFragment : BaseFragment<TabCatalogViewModel, FragmentTabCatalogBinding>(R.layout.fragment_tab_catalog) {

    private val catalogCategoriesAdapter: CatalogCategoriesAdapter
        get() = binding.catalogCategoriesRecyclerView.adapter as CatalogCategoriesAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.catalogCategoriesRecyclerView.addItemDecoration(
            VerticalItemDecoration(gap = 24.dp(binding.root.context))
        )

        binding.catalogCategoriesRecyclerView.adapter = CatalogCategoriesAdapter(
            onSubCategoryClick = {
                viewModel.onSubCategoryClick(it)
            },
            onAllProductsClick = {
                viewModel.onAllProductsClick(it)
            }
        )

        subscribe(viewModel.catalogCategoriesObserver){
            catalogCategoriesAdapter.setItems(it)
        }

        subscribe(viewModel.loadingStateObserver){
            binding.loadingProgressBar.visibleIf(it == BaseViewModel.LoadingState.LOADING)
            binding.catalogCategoriesRecyclerView.visibleIf(it == BaseViewModel.LoadingState.CONTENT)
        }

    }

    override fun setStatusBarColor(){
        setStatusBarColor(R.color.primary400)
    }
}