package com.custom.rgs_android_dom.ui.catalog.subcategories

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentCatalogPrimaryProductsBinding
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.views.NavigationScope
import com.yandex.metrica.YandexMetrica
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class CatalogPrimaryProductsFragment :
    BaseBottomSheetFragment<CatalogPrimaryProductsViewModel, FragmentCatalogPrimaryProductsBinding>() {

    companion object {

        private const val ARG_PRIMARY_CATALOG_CATEGORY = "ARG_PRIMARY_CATALOG_CATEGORY"

        fun newInstance(category: CatalogCategoryModel): CatalogPrimaryProductsFragment {
            return CatalogPrimaryProductsFragment().args {
                putSerializable(ARG_PRIMARY_CATALOG_CATEGORY, category)
            }
        }
    }

    private val primaryProductsAdapter: GridVerticalPrimaryProductsAdapter
        get() = binding.productsRecyclerView.adapter as GridVerticalPrimaryProductsAdapter

    override val TAG: String
        get() = "CATALOG_ALL_PRIMARY_SUBCATEGORIES_FRAGMENT"

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_PRIMARY_CATALOG_CATEGORY) as CatalogCategoryModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.productsRecyclerView.adapter = GridVerticalPrimaryProductsAdapter {
            viewModel.onProductClick(it)

            YandexMetrica.reportEvent("prodlist_product_click", "{\"product_item\":\"${it.name}\"}")
        }
        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }
        binding.searchImageView.setOnDebouncedClickListener {
            viewModel.onSearchClick()
        }

        subscribe(viewModel.productsObserver) {
            primaryProductsAdapter.setItems(it)
        }
        subscribe(viewModel.titleObserver) {
            binding.titleTextView.text = it
        }
    }

    override fun getNavigationScope(): NavigationScope? {
        return NavigationScope.NAV_CATALOG
    }


}