package com.custom.rgs_android_dom.ui.catalog.product.single

import android.os.Bundle
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
        subscribe(viewModel.productObserver) { product ->
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(product.iconLink))
                .transform(RoundedCorners(6.dp(requireContext())))
                .into(binding.header.iconImageView)
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(product.logoMiddle))
                .transform(RoundedCorners(16.dp(requireContext())))
                .into(binding.header.logoImageView)

            binding.validity.validityValue.text =
                "${product.duration?.units} ${product.duration?.unitType?.description}"
            binding.header.headerTitle.text = product.name
            binding.header.headerDescription.text = product.title
            binding.about.aboutValue.text = product.description
            binding.priceView.type = when {
                product.isPurchased -> MSDProductPriceView.PriceType.Purchased
                product.price?.fix == true -> MSDProductPriceView.PriceType.Fixed
                else -> MSDProductPriceView.PriceType.Unfixed
            }
            product.price?.amount?.let { price ->
                binding.priceView.setPrice(price)
                binding.detailButton.btnPrice.text = price.formatPrice(isFixed = product.price.fix)
            }
            product.advantages?.let {
                advantagesAdapter.setItems(it)
                binding.advantagesLayout.root.visibleIf(it.isNotEmpty())
            }
            product.deliveryTime?.let {
                binding.longness.longnessValue.text = "$it"
            }
            binding.features.featuresValue1.text = "Поддержка 24/7"

            if (product.isPurchased) {
                binding.detailButton.btnTitle.text = "Заказать"
                binding.detailButton.btnTitle.gravity = Gravity.CENTER
                binding.detailButton.btnPriceGroup.gone()
            } else {
                binding.detailButton.btnTitle.text = "Оформить"
                binding.detailButton.btnTitle.gravity = Gravity.CENTER_VERTICAL or Gravity.START
                binding.detailButton.btnPriceGroup.visible()
            }
        }
        subscribe(viewModel.serviceObserver) { service ->
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(service.iconLink))
                .transform(RoundedCorners(6.dp(requireContext())))
                .into(binding.header.iconImageView)

            binding.header.headerTitle.text = service.name
            binding.header.headerDescription.text = service.title
            binding.about.aboutValue.text = service.description
            binding.priceView.type = when {
                service.isPurchased -> MSDProductPriceView.PriceType.Purchased
                service.isIncluded -> MSDProductPriceView.PriceType.Included
                service.price?.fix == true -> MSDProductPriceView.PriceType.Fixed
                else -> MSDProductPriceView.PriceType.Unfixed
            }
            service.price?.amount?.let { price ->
                if (!service.isIncluded) {
                    binding.priceView.setPrice(price)
                    binding.detailButton.btnPrice.text =
                        price.formatPrice(isFixed = service.price.fix)
                }
            }
            service.deliveryTime?.let {
                binding.longness.longnessValue.text = "$it"
            }
            binding.features.featuresValue1.text = "Поддержка 24/7"

            if (service.isPurchased || service.isIncluded) {
                binding.detailButton.btnTitle.text = "Заказать"
                binding.detailButton.btnTitle.gravity = Gravity.CENTER
                binding.detailButton.btnPriceGroup.gone()
            } else {
                binding.detailButton.btnTitle.text = "Оформить"
                binding.detailButton.btnTitle.gravity = Gravity.CENTER_VERTICAL or Gravity.START
                binding.detailButton.btnPriceGroup.visible()
            }
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
        subscribe(viewModel.productPaidDateObserver) { paidDate ->
            paidDate?.let {
                binding.priceView.setPurchasedDate(it)
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