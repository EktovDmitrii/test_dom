package com.custom.rgs_android_dom.ui.catalog.product.single

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentSingleProductBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.catalog.ProductAdvantagesAdapter
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.MSDProductPriceView
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class SingleProductFragment : BaseBottomSheetFragment<SingleProductViewModel, FragmentSingleProductBinding>() {

    companion object {
        private const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"
        private const val ARG_IS_PRODUCT_INCLUDED = "ARG_IS_PRODUCT_INCLUDED"

        fun newInstance(productId: String, isIncluded: Boolean = false): SingleProductFragment {
            return SingleProductFragment().args {
                putString(ARG_PRODUCT_ID, productId)
                putBoolean(ARG_IS_PRODUCT_INCLUDED, isIncluded)
            }
        }
    }

    override val TAG: String = "SINGLE_PRODUCT_FRAGMENT"

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(ARG_PRODUCT_ID),
            requireArguments().getBoolean(ARG_IS_PRODUCT_INCLUDED)
        )
    }

    private val advantagesAdapter: ProductAdvantagesAdapter
        get() = binding.advantagesLayout.advantagesRecycler.adapter as ProductAdvantagesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.detailButton.btnTitle.text = "Оформить"
        if (requireArguments().getBoolean(ARG_IS_PRODUCT_INCLUDED, false))
            binding.priceView.type = MSDProductPriceView.PriceType.Included
        subscribe(viewModel.productObserver){product->
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(product.iconLink))
                .transform(RoundedCorners(6.dp(requireContext())))
                .into(binding.header.iconImageView)
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(product.logoMiddle))
                .transform(RoundedCorners(16.dp(requireContext())))
                .into(binding.header.logoImageView)

            binding.header.headerTitle.text = product.name
            binding.header.headerDescription.text = product.title
            binding.about.aboutValue.text = product.description
            binding.priceView.type =
                if (product.price?.fix == true) MSDProductPriceView.PriceType.Fixed
                else MSDProductPriceView.PriceType.Unfixed
            product.price?.amount?.let { price ->
                binding.priceView.setPrice(price)
                binding.detailButton.btnPrice.text = price.formatPrice(isFixed = product.price.fix)
            }

            binding.validity.validityValue.text = "${product.duration?.units} ${product.duration?.unitType?.description}"
        }
        binding.advantagesLayout.advantagesRecycler.adapter = ProductAdvantagesAdapter()

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

    override fun isNavigationViewVisible(): Boolean {
        return false
    }
}