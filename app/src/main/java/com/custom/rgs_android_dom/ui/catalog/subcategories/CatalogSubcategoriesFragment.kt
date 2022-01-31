package com.custom.rgs_android_dom.ui.catalog.subcategories

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentCatalogSubcategoriesBinding
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.CatalogSubcategoriesWithBigImageAdapter
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.recycler_view.GridThreeSpanItemDecoration
import com.custom.rgs_android_dom.utils.recycler_view.GridTwoSpanItemDecoration
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf
import com.custom.rgs_android_dom.views.NavigationScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class CatalogSubcategoriesFragment : BaseBottomSheetFragment<CatalogSubcategoriesViewModel, FragmentCatalogSubcategoriesBinding>() {

    companion object {

        private const val ARG_CATALOG_CATEGORY = "ARG_CATALOG_CATEGORY"

        fun newInstance(category: CatalogCategoryModel): CatalogSubcategoriesFragment {
            return CatalogSubcategoriesFragment().args {
                putSerializable(ARG_CATALOG_CATEGORY, category)
            }
        }
    }

    override val TAG: String = "CATALOG_SUBCATEGORIES_FRAGMENT"

    private val productsAdapter: CatalogSubcategoryProductsAdapter
        get() = binding.productsRecyclerView.adapter as CatalogSubcategoryProductsAdapter

    private val subcategoriesAdapter: CatalogGridSubcategoriesWithBigImageAdapter
        get() = binding.subcategoriesWithBigImageRecyclerView.adapter as CatalogGridSubcategoriesWithBigImageAdapter

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_CATALOG_CATEGORY) as CatalogCategoryModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productsRecyclerView.adapter = CatalogSubcategoryProductsAdapter{
            viewModel.onProductClick(it)
        }
        binding.subcategoriesWithBigImageRecyclerView.adapter = CatalogGridSubcategoriesWithBigImageAdapter{
            viewModel.onSubCategoryClick(it)
        }
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.dp_12)
        binding.subcategoriesWithBigImageRecyclerView.addItemDecoration(GridThreeSpanItemDecoration(spacingInPixels))

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }
        binding.searchImageView.setOnDebouncedClickListener {
            viewModel.onSearchClick()
        }

        subscribe(viewModel.titleObserver){
            binding.titleTextView.text = it
        }

        subscribe(viewModel.subcategoriesObserver){
            subcategoriesAdapter.setItems(it)
        }
        subscribe(viewModel.productsObserver){
            binding.othersTextView.visibleIf(it.isNotEmpty())
            productsAdapter.setItems(it)
        }

    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

    override fun getNavigationScope(): NavigationScope? {
        return NavigationScope.NAV_CATALOG
    }

}