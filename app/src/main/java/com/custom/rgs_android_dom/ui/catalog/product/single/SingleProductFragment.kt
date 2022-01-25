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
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class SingleProductFragment : BaseBottomSheetFragment<SingleProductViewModel, FragmentSingleProductBinding>() {

    companion object {
        private const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"

        fun newInstance(productId: String): SingleProductFragment {
            return SingleProductFragment().args {
                putString(ARG_PRODUCT_ID, productId)
            }
        }
    }

    override val TAG: String = "SINGLE_PRODUCT_FRAGMENT"

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_PRODUCT_ID))
    }

    private val advantagesAdapter: ProductAdvantagesAdapter
        get() = binding.advantagesLayout.advantagesRecycler.adapter as ProductAdvantagesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe(viewModel.productObserver){product->

            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(product.iconLink))
                .transform(RoundedCorners(24.dp(requireContext())))
                .into(binding.header.headerImg)

            binding.header.headerTitle.text = product.name
            binding.header.headerDescription.text = product.title
            binding.about.aboutValue.text = product.description

            product.price?.amount?.let {
                binding.price.priceValue.text = DigitsFormatter.priceFormat(it)
                binding.detailButton.btnPrice.text = DigitsFormatter.priceFormat(it)
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