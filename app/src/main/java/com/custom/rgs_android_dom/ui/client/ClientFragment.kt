package com.custom.rgs_android_dom.ui.client

import android.os.Bundle
import android.util.Log
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentClientBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.recycler_view.HorizontalItemDecoration

class ClientFragment() : BaseBottomSheetFragment<ClientViewModel, FragmentClientBinding>() {

    override val TAG: String = "CLIENT_FRAGMENT"

    private val propertyItemsAdapter: PropertyItemsAdapter
        get() = binding.propertyItemsRecycler.adapter as PropertyItemsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.setColorSchemeResources(R.color.secondary600)

        binding.propertyItemsRecycler.adapter = PropertyItemsAdapter(
            onAddPropertyClick = {
                viewModel.onAddPropertyClick()
            },
            onPropertyItemClick = {
                viewModel.onPropertyItemClick(it)
            }
        )

        binding.propertyItemsRecycler.addItemDecoration(
            HorizontalItemDecoration(
                gap = 16.dp(requireContext())
            )
        )

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.onRefresh()
        }

        binding.logoutRelativeLayout.setOnDebouncedClickListener {
            viewModel.onLogoutClick()
        }

        binding.personalDataRelativeLayout.setOnDebouncedClickListener {
            viewModel.onPersonalDataClick()
        }


        binding.agentInfoLinearLayout.setOnDebouncedClickListener {
            viewModel.onAgentInfoClick()
        }

        binding.aboutAppLinearLayout.setOnDebouncedClickListener {
            viewModel.onAboutAppClick()
        }

        binding.ordersHistoryLinearLayout.setOnDebouncedClickListener {
            viewModel.onNotCreatedScreenClick()
        }

        binding.myCardsLinearLayout.setOnDebouncedClickListener {
            viewModel.onNotCreatedScreenClick()
        }

        binding.notificationSettingsLinearLayout.setOnDebouncedClickListener {
            viewModel.onNotCreatedScreenClick()
        }

        binding.feedbackLinearLayout.setOnDebouncedClickListener {
            viewModel.onNotCreatedScreenClick()
        }

        subscribe(viewModel.propertyItemsObserver) { propertyItems ->
            propertyItemsAdapter.setItems(propertyItems)
        }

        subscribe(viewModel.clientShortViewStateObserver) { state ->
            binding.phoneTextView.text = state.phone

            if (state.firstName.isEmpty() && state.lastName.isEmpty()) {
                binding.nameTextView.text = "Добавьте ваше имя"
                binding.nameTextView.setTextColor(requireContext().getColor(R.color.primary500))
            } else {
                binding.nameTextView.text = "${state.lastName} ${state.firstName}"
                binding.nameTextView.setTextColor(requireContext().getColor(R.color.secondary900))
            }

            if (state.hasAgentInfo){
                binding.agentInfoTextView.text = "Данные агента"
            } else {
                binding.agentInfoTextView.text = "Я знаю код агента"
            }
        }

        subscribe(viewModel.networkErrorObserver) {
            toast(it)
        }

        subscribe(viewModel.swipeRefreshingObserver){
            binding.swipeRefreshLayout.isRefreshing = it
        }
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }
}