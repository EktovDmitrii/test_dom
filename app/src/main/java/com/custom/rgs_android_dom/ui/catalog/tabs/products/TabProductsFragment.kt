package com.custom.rgs_android_dom.ui.catalog.tabs.products

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentTabProductsBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.setStatusBarColor
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf

class TabProductsFragment :
    BaseFragment<TabProductsViewModel, FragmentTabProductsBinding>(R.layout.fragment_tab_products) {

    private val myProductsAdapter: CatalogMyProductsAdapter
        get() = binding.myProductsRecyclerView.adapter as CatalogMyProductsAdapter

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary400)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myProductsRecyclerView.adapter = CatalogMyProductsAdapter {
            viewModel.onProductClick(it)
        }

        subscribe(viewModel.myProductsObserver) {
            binding.myProductsRecyclerView.visibleIf(it.isNotEmpty())
            binding.myProductsEmptyState.visibleIf(it.isEmpty())

            myProductsAdapter.setItems(it)
        }
    }
}