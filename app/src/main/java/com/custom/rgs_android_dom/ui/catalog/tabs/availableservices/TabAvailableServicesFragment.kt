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

    private val availableServicesAdapter: AvailableServicesAdapter
        get() = binding.servicesRecyclerView.adapter as AvailableServicesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.servicesRecyclerView.adapter = AvailableServicesAdapter {
            viewModel.onServiceClick(it)
        }

        /*binding.servicesRecyclerView.addItemDecoration(
            VerticalItemDecoration(gap = 8.dp(binding.root.context))
        )*/
        binding.openCatalogTextView.setOnDebouncedClickListener {
            (this.parentFragment as? MainCatalogFragment?)?.navigateCatalog()
        }

        subscribe(viewModel.availableServicesObserver) {
            binding.servicesRecyclerView.visibleIf(it.isNotEmpty())
            binding.noServicesLayout.visibleIf(it.isEmpty())

            availableServicesAdapter.setItems(it)
        }

        subscribe(viewModel.isNoServicesLayoutVisibleObserver){
            binding.servicesRecyclerView.gone()
            binding.noServicesLayout.visible()
        }
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary400)
    }
}