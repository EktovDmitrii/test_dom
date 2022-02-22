package com.custom.rgs_android_dom.ui.catalog.product

import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.View
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentProductBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.catalog.ProductAdvantagesAdapter
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.recycler_view.GridTwoSpanItemDecoration
import com.custom.rgs_android_dom.views.MSDProductPriceView
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class ProductFragment :BaseBottomSheetFragment<ProductViewModel, FragmentProductBinding>() {

    companion object {

        private const val ARG_PRODUCT = "ARG_PRODUCT"

        private const val GRID_HORIZONTAL_GAP = 16
        private const val GRID_VERTICAL_GAP = 12

        fun newInstance(productLauncher: ProductLauncher): ProductFragment {
            return ProductFragment().args {
                putSerializable(ARG_PRODUCT, productLauncher)
            }
        }
    }

    override val TAG: String = "PRODUCT_DETAIL_FRAGMENT"

    override fun getThemeResource(): Int = R.style.BottomSheetNoDim

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_PRODUCT) as ProductLauncher)
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
            adapter = ProductInclusionAdapter(
                onServiceClick = { viewModel.onServiceClick(it) },
                onOrderClick = { viewModel.onServiceOrderClick(it) }
            )
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
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(product.logoMiddle))
                .transform(RoundedCorners(16.dp(requireContext())))
                .into(binding.header.logoImageView)

            binding.validity.validityValue.text = product.duration.toString() + " после покупки"
            binding.header.headerTitle.text = product.name
            binding.header.headerDescription.text = product.title
            binding.about.aboutValue.text = product.description?.let { descr ->
                Html.fromHtml(
                    descr,
                    Html.FROM_HTML_MODE_LEGACY
                )
            }

            binding.priceView.goneIf(product.isPurchased)
            binding.priceView.type = if (product.price?.fix == true) {
                MSDProductPriceView.PriceType.Fixed
            } else {
                MSDProductPriceView.PriceType.Unfixed
            }
            product.price?.amount?.let { price ->
                binding.priceView.setPrice(price)
                binding.detailButton.btnPrice.text = price.formatPrice(isFixed = product.price.fix)
            }
            product.advantages?.let {
                advantagesAdapter.setItems(it)
                binding.advantagesLayout.root.visibleIf(it.isNotEmpty())
            }
            binding.detailButton.root.goneIf(product.isPurchased)
        }

        subscribe(viewModel.productServicesObserver) {
            inclusionAdapter.setItems(it)

            binding.includes.root.visibleIf(it.isNotEmpty())
        }
        subscribe(viewModel.productAddressObserver) { address ->
            address?.let {
                binding.address.addressValue.text = it
                binding.address.root.visible()
            }
        }
        subscribe(viewModel.productValidToObserver) { validTo ->
            validTo?.let {
                binding.validityUntill.validityTillValue.text = it
                binding.validityUntill.root.visible()
            }
        }
    }

    override fun isNavigationViewVisible(): Boolean {
        return false
    }
}
