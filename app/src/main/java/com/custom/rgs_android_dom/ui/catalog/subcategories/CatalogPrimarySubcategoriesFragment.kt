package com.custom.rgs_android_dom.ui.catalog.subcategories

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentCatalogPrimarySubcategoriesBinding
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class CatalogPrimarySubcategoriesFragment : BaseBottomSheetFragment<CatalogPrimarySubcategoryViewModel, FragmentCatalogPrimarySubcategoriesBinding>() {

    companion object {

        private const val ARG_PRIMARY_CATALOG_CATEGORY = "ARG_PRIMARY_CATALOG_CATEGORY"

        fun newInstance(category: CatalogCategoryModel): CatalogPrimarySubcategoriesFragment {
            return CatalogPrimarySubcategoriesFragment().args {
                putSerializable(ARG_PRIMARY_CATALOG_CATEGORY, category)
            }
        }
    }

    private val primarySubcategoriesAdapter: CatalogPrimarySubcategoriesAdapter
        get() = binding.subcategoriesRecyclerView.adapter as CatalogPrimarySubcategoriesAdapter

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
        binding.subcategoriesRecyclerView.adapter = CatalogPrimarySubcategoriesAdapter {
            viewModel.onSubCategoryClick(it)
        }
        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        subscribe(viewModel.subcategoriesObserver) {
            primarySubcategoriesAdapter.setItems(it)
        }
    }


}