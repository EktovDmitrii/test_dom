package com.custom.rgs_android_dom.ui.client.personal_data.delete_client

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentDeleteClientBinding
import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class DeleteClientFragment() : BaseBottomSheetModalFragment<DeleteClientViewModel, FragmentDeleteClientBinding>() {

    companion object {
        private const val ARG_ACTIVE_ORDERS = "ARG_ACTIVE_ORDERS"

        fun newInstance(activeOrders: List<Order>) = DeleteClientFragment().args {
            val gson = Gson()
            putString(ARG_ACTIVE_ORDERS, gson.toJson(activeOrders))
        }
    }

    private val activeOrdersAdapter: ActiveOrdersAdapter
        get() = binding.activeOrdersRecyclerView.adapter as ActiveOrdersAdapter

    override val TAG: String = "DELETE_CLIENT_FRAGMENT"

    override fun getParameters(): ParametersDefinition = {
        val gson = Gson()
        val itemType = object : TypeToken<List<Order>>() {}.type
        parametersOf(gson.fromJson(requireArguments().getString(ARG_ACTIVE_ORDERS), itemType))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expand()

        binding.confirmTextView.setOnDebouncedClickListener {
            viewModel.onConfirmClick()
        }

        binding.cancelTextView.setOnDebouncedClickListener {
            viewModel.onCancelClick()
        }

        subscribe(viewModel.activeOrdersObserver) { activeOrders ->
            if (activeOrders.isNotEmpty()) {
                binding.titleTextView.text = TranslationInteractor.getTranslation("app.profile.delete.active_orders.title")
                binding.descriptionTextView.text = TranslationInteractor.getTranslation("app.profile.delete.active_orders.subtitle")

                binding.activeOrdersRecyclerView.visible()
                binding.activeOrdersRecyclerView.adapter = ActiveOrdersAdapter().apply {
                    setItems(activeOrders)
                }

                if (activeOrders.size == 1) {
                    binding.activeOrdersRecyclerView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                } else {
                    binding.activeOrdersRecyclerView.layoutParams.height = 180.dp(requireContext())
                }

            } else {
                binding.titleTextView.text = TranslationInteractor.getTranslation("app.profile.delete.no_orders.title")
                binding.descriptionTextView.text = TranslationInteractor.getTranslation("app.profile.delete.no_orders.subtitle")
                binding.activeOrdersRecyclerView.gone()
            }
        }

        subscribe(viewModel.networkErrorObserver) {
            notification(it)
        }
    }

    override fun onClose() {
        hideSoftwareKeyboard()
        dismissAllowingStateLoss()
    }

    override fun onLoading() {
        super.onLoading()
        binding.confirmTextView.setLoading(true)
        binding.cancelTextView.isEnabled = false
    }

    override fun onError() {
        super.onError()
        binding.confirmTextView.setLoading(false)
        binding.cancelTextView.isEnabled = true
    }

    override fun onContent() {
        super.onContent()
        binding.confirmTextView.setLoading(false)
        binding.cancelTextView.isEnabled = true
    }
}