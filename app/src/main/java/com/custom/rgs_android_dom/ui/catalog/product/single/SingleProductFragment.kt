package com.custom.rgs_android_dom.ui.catalog.product.single

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentSingleProductBinding
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.catalog.ProductAdvantagesAdapter
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class SingleProductFragment : BaseBottomSheetFragment<SingleProductViewModel, FragmentSingleProductBinding>() {

    companion object {
        private const val ARG_PRODUCT = "ARG_PRODUCT"

        fun newInstance(product: ProductModel): SingleProductFragment {
            return SingleProductFragment().args {
                putSerializable(ARG_PRODUCT, product)
            }
        }
    }

    override val TAG: String = "SINGLE_PRODUCT_FRAGMENT"

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_PRODUCT) as ProductModel)
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

            binding.header.headerTitle.text = product.title
            binding.header.headerDescription.text = product.description
            binding.price.priceValue.text = "${product.price?.amount} ₽"

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
}