package com.custom.rgs_android_dom.ui.catalog.product.single

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentClientBinding
import com.custom.rgs_android_dom.databinding.FragmentSingleProductBinding
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.catalog.product.single.more.MoreSingleProductFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.recycler_view.HorizontalItemDecoration
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe(viewModel.productObserver){product->

            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(product.iconLink))
                .transform(RoundedCorners(24.dp(requireContext())))
                .into(binding.headerLayout.logoImageView)

            binding.headerLayout.titleTextView.text = product.title
            binding.headerLayout.descriptionTextView.text = product.description
            binding.headerLayout.priceTextView.text = "${product.price?.amount} ₽/шт"

            binding.purchaseTextView.setSecondaryText("${product.price?.amount} ₽")

            binding.headerLayout.usageTermsTextView.text = "${product.duration?.units} ${product.duration?.unitType?.description}"
        }

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.moreImageView.setOnDebouncedClickListener{
            viewModel.onMoreClick()
        }

        subscribe(viewModel.showMoreDialogObserver){
            val moreSingleProductFragment = MoreSingleProductFragment()
            moreSingleProductFragment.show(childFragmentManager, moreSingleProductFragment.TAG)
        }

    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }
}