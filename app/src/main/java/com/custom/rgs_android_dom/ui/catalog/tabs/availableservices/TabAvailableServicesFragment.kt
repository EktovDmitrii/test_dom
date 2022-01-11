package com.custom.rgs_android_dom.ui.catalog.tabs.availableservices

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentTabAvailableServicesBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.recycler_view.VerticalItemDecoration
import com.custom.rgs_android_dom.utils.setStatusBarColor
import com.custom.rgs_android_dom.utils.subscribe

class TabAvailableServicesFragment() : BaseFragment<TabAvailableServicesViewModel, FragmentTabAvailableServicesBinding>(R.layout.fragment_tab_available_services) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AvailableServicesAdapter {
            viewModel.onServiceClick(it)
        }
        binding.servicesRecyclerView.adapter = adapter
        binding.servicesRecyclerView.addItemDecoration(
            VerticalItemDecoration(gap = 8.dp(binding.root.context))
        )
        subscribe(viewModel.servicesObserver) {
            adapter.setItems(it)
        }
    }


    override fun setStatusBarColor(){
        setStatusBarColor(R.color.primary400)
    }
}