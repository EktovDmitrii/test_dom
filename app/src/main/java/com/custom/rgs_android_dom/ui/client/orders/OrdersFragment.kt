package com.custom.rgs_android_dom.ui.client.orders

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentOrdersBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*

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

            showCatalogTextView.setOnDebouncedClickListener { viewModel.onShowCatalogClick() }
            subscribe(viewModel.ordersObserver) { ordersAdapter.setItems(it) }
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

    override fun onEmpty() {
        super.onEmpty()
        initState(empty = true)
    }

    private fun initState(
        loading: Boolean = false,
        content: Boolean = false,
        error: Boolean = false,
        empty: Boolean = false
    ) {
        with(binding) {
            noOrdersLayout.visibility = if (error) VISIBLE else GONE
            recyclerView.visibility = if (content) VISIBLE else GONE
            loadingProgressBar.visibility = if (loading) VISIBLE else GONE
            noOrdersLayout.visibility = if (empty) VISIBLE else GONE
        }
    }

}
