package com.custom.rgs_android_dom.ui.catalog.product.service

import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentServiceBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.catalog.ProductAdvantagesAdapter
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.MSDProductPriceView
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class ServiceFragment : BaseBottomSheetFragment<ServiceViewModel, FragmentServiceBinding>() {

    companion object {
        private const val ARG_SERVICE = "ARG_SERVICE"

        fun newInstance(product: ServiceLauncher): ServiceFragment {
            return ServiceFragment().args {
                putSerializable(ARG_SERVICE, product)
            }
        }
    }

    override val TAG: String = "SERVICE_FRAGMENT"

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getSerializable(ARG_SERVICE) as ServiceLauncher,
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

        binding.detailButton.root.setOnDebouncedClickListener {
            viewModel.onServiceOrderClick()
        }

        binding.features.featuresValue1.text = "Поддержка 24/7"

        subscribe(viewModel.serviceObserver) { service ->
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(service.iconLink))
                .transform(RoundedCorners(6.dp(requireContext())))
                .into(binding.header.iconImageView)
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(service.logoLarge))
                .transform(RoundedCorners(16.dp(requireContext())))
                .into(binding.header.logoImageView)

            if (service.duration != null) {
                binding.validity.validityValue.text = service.duration.toString()
            } else {
                binding.validity.root.gone()
            }
            binding.header.headerTitle.text = service.name
            binding.header.headerDescription.text = service.title
            binding.about.aboutValue.text = service.description

            binding.priceView.setIcon(service.iconLink)
            binding.priceView.type = MSDProductPriceView.PriceType.Included
            service.deliveryTime?.let {
                binding.longness.longnessValue.text = "$it"
            }

            binding.detailButton.btnTitle.text = "Заказать"
            binding.detailButton.btnTitle.gravity = Gravity.CENTER
            binding.detailButton.btnPriceGroup.gone()
        }
        subscribe(viewModel.productAddressObserver) { address ->
            address?.let {
                binding.address.addressValue.text = it
                binding.address.root.visible()
            }
        }

        subscribe(viewModel.priceTextViewVisibleObserver){
            binding.priceView.visibleIf(it)
        }

        subscribe(viewModel.orderTextViewVisibleObserver){
            binding.detailButton.root.visibleIf(it)
        }

        subscribe(viewModel.productValidToObserver) { validTo ->
            validTo?.let {
                binding.validityUntill.validityTillValue.text = it
                binding.validityUntill.root.visible()
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