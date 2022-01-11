package com.custom.rgs_android_dom.ui.catalog.subcategory

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentCatalogSubcategoryBinding
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.catalog.subcategories.CatalogSubcategoryProductsAdapter
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class CatalogSubcategoryFragment : BaseBottomSheetFragment<CatalogSubcategoryViewModel, FragmentCatalogSubcategoryBinding>() {

    companion object {

        private const val ARG_CATALOG_SUBCATEGORY = "ARG_CATALOG_SUBCATEGORY"

        fun newInstance(subCategory: CatalogSubCategoryModel): CatalogSubcategoryFragment {
            return CatalogSubcategoryFragment().args {
                putSerializable(ARG_CATALOG_SUBCATEGORY, subCategory)
            }
        }
    }

    override val TAG: String = "CATALOG_SUBCATEGORY_FRAGMENT"

    private val adapter: CatalogSubcategoryProductsAdapter
        get() = binding.productsRecyclerView.adapter as CatalogSubcategoryProductsAdapter

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_CATALOG_SUBCATEGORY) as CatalogSubCategoryModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CatalogSubcategoryProductsAdapter(){
            viewModel.onProductClick(it)
        }

        binding.productsRecyclerView.adapter = adapter

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        subscribe(viewModel.titleObserver){
            binding.titleTextView.text = it
        }

        subscribe(viewModel.productsObserver){
            adapter.setItems(it)
        }

        subscribe(viewModel.iconObserver){
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(it))
                .transform(RoundedCorners(8.dp(requireContext())))
                .error(R.drawable.rectangle_filled_secondary_100_radius_8dp)
                .into(binding.logoImageView)
        }

    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

}