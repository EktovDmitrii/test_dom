package com.custom.rgs_android_dom.ui.catalog.product

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentProductBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.recycler_view.GridSpaceItemDecoration
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class ProductFragment :BaseBottomSheetFragment<ProductViewModel, FragmentProductBinding>() {

    companion object {

        private const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"

        fun newInstance(productId: String): ProductFragment {
            return ProductFragment().args {
                putString(ARG_PRODUCT_ID, productId)
            }
        }
    }

    override val TAG: String = "PRODUCT_DETAIL_FRAGMENT"

    override fun getThemeResource(): Int = R.style.BottomSheetNoDim

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_PRODUCT_ID))
    }

    private val inclusionAdapter: ProductInclusionAdapter
        get() = binding.includes.includesRecycler.adapter as ProductInclusionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.includes.includesRecycler.apply {
            adapter = ProductInclusionAdapter()
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.material_margin_normal)
            addItemDecoration(GridSpaceItemDecoration(spacingInPixels))
        }

        subscribe(viewModel.productObserver) { product ->
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(product.iconLink))
                .error(R.mipmap.temp)
                .transform(RoundedCorners(20.dp(requireContext())))
                .into(binding.header.headerImg)

            binding.header.headerTitle.text = product.title
            binding.header.headerDescription.text = product.description
            binding.price.priceValue.text = "${product.price?.amount}₽"
            binding.detailButton.btnPrice.text = "${product.price?.amount}₽"
        }
    }
}