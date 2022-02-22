package com.custom.rgs_android_dom.ui.catalog.product.single

import android.os.Bundle
import android.text.Html
import android.view.Gravity
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

class SingleProductFragment :
    BaseBottomSheetFragment<SingleProductViewModel, FragmentSingleProductBinding>() {

    companion object {
        private const val ARG_PRODUCT = "ARG_PRODUCT"

        fun newInstance(product: SingleProductLauncher): SingleProductFragment {
            return SingleProductFragment().args {
                putSerializable(ARG_PRODUCT, product)
            }
        }
    }

    override val TAG: String = "SINGLE_PRODUCT_FRAGMENT"

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getSerializable(ARG_PRODUCT) as SingleProductLauncher,
        )
    }

    private val advantagesAdapter: ProductAdvantagesAdapter
        get() = binding.advantagesLayout.advantagesRecycler.adapter as ProductAdvantagesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.advantagesLayout.advantagesRecycler.adapter = ProductAdvantagesAdapter()

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.checkoutButton.root.setOnDebouncedClickListener {
            viewModel.onCheckoutClick()
        }

        subscribe(viewModel.productObserver) { product ->
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
                binding.checkoutButton.btnPrice.text = price.formatPrice(isFixed = product.price.fix)
            }
            product.advantages?.let {
                advantagesAdapter.setItems(it)
                binding.advantagesLayout.root.visibleIf(it.isNotEmpty())
            }
            product.deliveryTime?.let {
                binding.longness.longnessValue.text = "$it"
            }

            if (product.isPurchased) {
                binding.checkoutButton.btnTitle.text = "Заказать"
                binding.checkoutButton.btnTitle.gravity = Gravity.CENTER
                binding.checkoutButton.btnPriceGroup.gone()
            } else {
                binding.checkoutButton.btnTitle.text = "Оформить"
                binding.checkoutButton.btnPriceGroup.visible()
            }
        }
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

    override fun isNavigationViewVisible(): Boolean {
        return false
    }
}