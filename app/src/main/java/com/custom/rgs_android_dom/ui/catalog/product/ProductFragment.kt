package com.custom.rgs_android_dom.ui.catalog.product

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentProductBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.catalog.ProductAdvantagesAdapter
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.recycler_view.GridTwoSpanItemDecoration
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class ProductFragment :BaseBottomSheetFragment<ProductViewModel, FragmentProductBinding>() {

    companion object {

        private const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"

        private const val GRID_HORIZONTAL_GAP = 16
        private const val GRID_VERTICAL_GAP = 12

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

    private val advantagesAdapter: ProductAdvantagesAdapter
        get() = binding.advantagesLayout.advantagesRecycler.adapter as ProductAdvantagesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }
        binding.detailButton.root.setOnDebouncedClickListener {
            viewModel.onCheckoutClick()
        }

        binding.includes.includesRecycler.apply {
            adapter = ProductInclusionAdapter {
                viewModel.onServiceClick(it)
            }
            val horizontalGapInPixels = GRID_HORIZONTAL_GAP.dp(requireActivity())
            val verticalGapInPixels = GRID_VERTICAL_GAP.dp(requireActivity())
            addItemDecoration(GridTwoSpanItemDecoration(horizontalGapInPixels, verticalGapInPixels))
        }
        binding.advantagesLayout.advantagesRecycler.adapter = ProductAdvantagesAdapter()

        subscribe(viewModel.productObserver) { product ->
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(product.iconLink))
                .transform(RoundedCorners(6.dp(requireContext())))
                .into(binding.header.iconImageView)

            binding.validity.validityValue.text = "${product.duration?.units} ${product.duration?.unitType?.description}"
            binding.header.headerTitle.text = product.name
            binding.header.headerDescription.text = product.title
            binding.about.aboutValue.text = product.description
            product.price?.amount?.let { price ->
                binding.priceView.setPrice(price)
                binding.detailButton.btnPrice.text = price.formatPrice()
            }
            product.advantages?.let {
                advantagesAdapter.setItems(it)
                binding.advantagesLayout.root.visibleIf(it.isNotEmpty())
            }
        }

        subscribe(viewModel.productServicesObserver) {
            inclusionAdapter.setItems(it)

            binding.includes.root.visibleIf(it.isNotEmpty())
        }
    }

    override fun isNavigationViewVisible(): Boolean {
        return false
    }
}
