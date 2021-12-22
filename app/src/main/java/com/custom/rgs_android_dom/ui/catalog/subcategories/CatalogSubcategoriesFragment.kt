package com.custom.rgs_android_dom.ui.catalog.subcategories

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentCatalogSubcategoriesBinding
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.args
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

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_CATALOG_CATEGORY) as CatalogCategoryModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

}