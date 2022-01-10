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
        private const val ARG_TAG = "TAG"

        fun newInstance(tag: String? = null): CatalogSearchFragment {
            return CatalogSearchFragment().args { 
                putString(ARG_TAG, tag)
            }
        }
    }

    private val adapter: CatalogSearchResultsAdapter
        get() = binding.allProductsRecyclerView.adapter as CatalogSearchResultsAdapter

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_TAG, null))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSoftwareKeyboard()

        binding.allProductsRecyclerView.adapter = CatalogSearchResultsAdapter(){
            viewModel.onProductClick(it)
        }

        binding.searchInput.addTextChangedListener {
            viewModel.onSearchQueryChanged(it)
        }

        binding.searchInput.setOnClearClickListener {
            binding.searchInput.unfocus()
            viewModel.onClearClick()
        }

        subscribe(viewModel.productsObserver){
            adapter.setItems(it)
        }

        subscribe(viewModel.queryNotEmptyObserver){
            binding.allProductsTextView.goneIf(it)
        }
    }


    override fun onClose() {
        hideSoftwareKeyboard()
        ScreenManager.back(getNavigateId())
    }

}