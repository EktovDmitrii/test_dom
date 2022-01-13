package com.custom.rgs_android_dom.ui.catalog.tabs.availableservices

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentTabAvailableServicesBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.catalog.MainCatalogFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.recycler_view.VerticalItemDecoration

class TabAvailableServicesFragment :
    BaseFragment<TabAvailableServicesViewModel, FragmentTabAvailableServicesBinding>(R.layout.fragment_tab_available_services) {

    private val servicesAdapter: AvailableServicesAdapter
        get() = binding.servicesRecyclerView.adapter as AvailableServicesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.servicesRecyclerView.adapter = AvailableServicesAdapter {
            viewModel.onServiceClick(it)
        }
        binding.servicesRecyclerView.addItemDecoration(
            VerticalItemDecoration(gap = 8.dp(binding.root.context))
        )
        binding.servicesNavigateCatalog.setOnDebouncedClickListener {
            (this.parentFragment as? MainCatalogFragment?)?.navigateCatalog()
        }

        subscribe(viewModel.servicesObserver) {
            binding.servicesRecyclerView.visibleIf(it.isNotEmpty())
            binding.servicesEmptyState.visibleIf(it.isEmpty())

            servicesAdapter.setItems(it)
        }
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary400)
    }
}