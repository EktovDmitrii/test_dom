package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentTabCatalogBinding
import com.custom.rgs_android_dom.databinding.LayoutCatalogPrimaryCategoryBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.recycler_view.HorizontalItemDecoration
import com.custom.rgs_android_dom.utils.recycler_view.VerticalItemDecoration
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.toast
import org.koin.android.ext.android.get

class TabCatalogFragment :
    BaseFragment<TabCatalogViewModel, FragmentTabCatalogBinding>(R.layout.fragment_tab_catalog) {

    private val primaryCategoryAdapter: PrimaryCategoryAdapter
        get() = binding.catalogPrimaryCategoryLayout.productsRecyclerView.adapter as PrimaryCategoryAdapter

    private val secondaryCategoryAdapter: SecondaryCategoryAdapter
        get() = binding.secondaryRecyclerView.adapter as SecondaryCategoryAdapter

    private val tertiaryCategoryAdapter: TertiaryCategoryAdapter
        get() = binding.tertiaryRecyclerView.adapter as TertiaryCategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.catalogPrimaryCategoryLayout.productsRecyclerView.addItemDecoration(
            HorizontalItemDecoration(gap = 16.dp(binding.root.context))
        )
        binding.catalogPrimaryCategoryLayout.productsRecyclerView.adapter =
            PrimaryCategoryAdapter(
                onProductClick = { viewModel.onProductClick() },
                onMoreProductsClick = { viewModel.onMoreProductsClick() }
            )

        binding.secondaryRecyclerView.addItemDecoration(
            VerticalItemDecoration(gap = 24.dp(binding.root.context))
        )
        binding.secondaryRecyclerView.adapter =
            SecondaryCategoryAdapter(
                onServiceClick = { viewModel.onServiceClick() }
            )

        binding.tertiaryRecyclerView.addItemDecoration(VerticalItemDecoration(gap = 24.dp(binding.root.context)))
        binding.tertiaryRecyclerView.adapter = TertiaryCategoryAdapter()

        binding.catalogPrimaryCategoryLayout.showAllTextView.setOnDebouncedClickListener {
            viewModel.onShowAllPrimaryCategoryClick()
        }

        subscribe(viewModel.viewStateObserver) {
            primaryCategoryAdapter.setItems(it.primaryCategories)
            secondaryCategoryAdapter.setItems(it.secondaryCategories)
            tertiaryCategoryAdapter.setItems(it.tertiaryCategories)
        }

        subscribe(viewModel.notificationObserver) {
            toast(it)
        }

    }
}