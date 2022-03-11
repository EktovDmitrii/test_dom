package com.custom.rgs_android_dom.ui.catalog.tabs.products

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentTabMyProductsBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*
import com.yandex.metrica.YandexMetrica

class TabMyProductsFragment : BaseFragment<TabMyProductsViewModel, FragmentTabMyProductsBinding>(R.layout.fragment_tab_my_products) {

    private val myProductsAdapter: ClientProductsAdapter
        get() = binding.myProductsRecyclerView.adapter as ClientProductsAdapter

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary400)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myProductsRecyclerView.adapter = ClientProductsAdapter {
            viewModel.onProductClick(it)

            YandexMetrica.reportEvent("catalog_my_products", "{\"my_products\":\"${it.productName}\"}")
        }

        binding.addPolicyTextView.setOnDebouncedClickListener {
            viewModel.onAddPolicyClick()
        }

        subscribe(viewModel.myProductsObserver) {
            binding.myProductsRecyclerView.visibleIf(it.isNotEmpty())
            binding.noProductsLayout.visibleIf(it.isEmpty())

            myProductsAdapter.setItems(it)
        }

        subscribe(viewModel.isNoProductsLayoutVisibleObserver){
            binding.myProductsRecyclerView.gone()
            binding.noProductsLayout.visible()
        }

    }
}