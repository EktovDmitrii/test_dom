package com.custom.rgs_android_dom.ui.client.orders

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentOrdersBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf

class OrdersFragment : BaseFragment<OrdersViewModel, FragmentOrdersBinding>(R.layout.fragment_orders) {

    private val ordersAdapter: OrdersAdapter
        get() = binding.recyclerView.adapter as OrdersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            recyclerView.adapter = OrdersAdapter(
                onOrderClick = { viewModel.onItemClick(it) },
                onPayClick = { viewModel.onPayClick(it) }
            )
            backImageView.setOnDebouncedClickListener {
                viewModel.onBackClick()
            }
            swipeRefresh.setOnRefreshListener {
                viewModel.loadOrderHistory()
                swipeRefresh.isRefreshing = false
            }

            showCatalogTextView.setOnDebouncedClickListener {
                viewModel.onShowCatalogClick()
            }
            mainErrorLayout.reloadTextView.setOnDebouncedClickListener {
                viewModel.onReloadClick()
            }
            subscribe(viewModel.ordersObserver) {
                if (it.isEmpty()) {
                    initState(empty = true)
                } else {
                    ordersAdapter.setItems(it)
                }
            }
        }
    }

    override fun onLoading() {
        super.onLoading()
        initState(loading = true)
    }

    override fun onContent() {
        super.onContent()
        initState(content = true)
    }

    override fun onError() {
        super.onError()
        initState(error = true)
    }

    private fun initState(
        loading: Boolean = false,
        content: Boolean = false,
        error: Boolean = false,
        empty: Boolean = false
    ) {
        with(binding) {
            mainErrorLayout.root.visibleIf(error)
            recyclerView.visibleIf(content)
            loadingProgressBar.visibleIf(loading)
            noOrdersLayout.visibleIf(empty)
        }
    }

}
