package com.custom.rgs_android_dom.ui.catalog.search

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentCatalogSearchBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class CatalogSearchFragment : BaseFragment<CatalogSearchViewModel, FragmentCatalogSearchBinding>(
    R.layout.fragment_catalog_search
) {

    companion object {
        private const val ARG_TAG = "ARG_TAG"

        fun newInstance(tag: String? = null): CatalogSearchFragment {
            return CatalogSearchFragment().args { 
                putString(ARG_TAG, tag)
            }
        }
    }

    private val popularProductsAdapter: CatalogSearchResultsAdapter
        get() = binding.popularProductsRecyclerView.adapter as CatalogSearchResultsAdapter

    private val searchResultsAdapter: CatalogSearchResultsAdapter
        get() = binding.searchResultsRecyclerView.adapter as CatalogSearchResultsAdapter

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_TAG, null))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchInput.focus()

        binding.searchResultsRecyclerView.adapter = CatalogSearchResultsAdapter {
            hideSoftwareKeyboard()
            viewModel.onProductClick(it)
        }

        binding.popularProductsRecyclerView.adapter = CatalogSearchResultsAdapter {
            hideSoftwareKeyboard()
            viewModel.onProductClick(it)
        }

        binding.searchInput.addTextChangedListener {
            viewModel.onSearchQueryChanged(it)
        }

        binding.searchInput.setOnClearClickListener {
            binding.searchInput.clear()
            viewModel.onClearClick()
        }

        binding.cancelTextView.setOnDebouncedClickListener {
            viewModel.onCancelClick()
        }

        binding.openChatTextView.setOnDebouncedClickListener {
            hideSoftwareKeyboard()
            viewModel.onOpenChatClick()
        }

        subscribe(viewModel.popularProductsObserver){
            popularProductsAdapter.setItems(it)
        }

        subscribe(viewModel.searchResultsObserver){
            searchResultsAdapter.setItems(it)
        }

        subscribe(viewModel.tagObserver){
            binding.searchInput.setText(it)
        }

        subscribe(viewModel.popularProductsVisibleObserver){
            binding.popularProductsLinearLayout.visibleIf(it)
        }

        subscribe(viewModel.searchResultsVisibleObserver){
            binding.searchResultsRecyclerView.visibleIf(it)
        }

        subscribe(viewModel.noSearchResultsVisibleObserver){
            binding.noSearchResultsLinearLayout.visibleIf(it)
        }
    }


    override fun onClose() {
        hideSoftwareKeyboard()
        ScreenManager.back(getNavigateId())
    }

}
