package com.custom.rgs_android_dom.ui.catalog.product

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentServiceBinding
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.catalog.ProductAdvantagesAdapter
import com.custom.rgs_android_dom.utils.args
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class ServiceFragment : BaseBottomSheetFragment<ServiceViewModel, FragmentServiceBinding>() {

    companion object {
        private const val ARG_SERVICE = "ARG_SERVICE"

        fun newInstance(product: ProductModel): ServiceFragment {
            return ServiceFragment().args {
                putSerializable(ARG_SERVICE, product)
            }
        }
    }

    override val TAG: String = "SERVICE_DETAIL_FRAGMENT"

    override fun getThemeResource(): Int = R.style.BottomSheetNoDim

    private val advantagesAdapter: ProductAdvantagesAdapter
        get() = binding.advantagesLayout.advantagesRecycler.adapter as ProductAdvantagesAdapter

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_SERVICE) as ProductModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.advantagesLayout.advantagesRecycler.adapter = ProductAdvantagesAdapter()
    }

    override fun isNavigationViewVisible(): Boolean {
        return false
    }
}
