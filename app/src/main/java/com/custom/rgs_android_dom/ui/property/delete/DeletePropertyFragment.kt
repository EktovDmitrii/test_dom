package com.custom.rgs_android_dom.ui.property.delete

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentCancelOrderBinding
import com.custom.rgs_android_dom.databinding.FragmentDeletePropertyBinding
import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class DeletePropertyFragment : BaseBottomSheetFragment<DeletePropertyViewModel, FragmentDeletePropertyBinding>() {

    companion object {
        private const val ARG_PROPERTY = "ARG_PROPERTY"

        fun newInstance(property: PropertyItemModel): DeletePropertyFragment {
            return DeletePropertyFragment().args {
                putSerializable(ARG_PROPERTY, property)
            }
        }
    }

    override val TAG = "DELETE_PROPERTY_FRAGMENT"

    override fun getThemeResource(): Int {
        return R.style.BottomSheet
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_PROPERTY) as PropertyItemModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe(viewModel.propertyObserver){
            binding.nameTextView.text = it.name
            binding.addressTextView.text = it.address?.address

            /*GlideApp.with(binding.root.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(it))
                .apply(
                    RequestOptions().transform(
                        CenterCrop(),
                        RoundedCorners(16.dp(binding.root.context))
                    )
                )
                .error(R.drawable.rectangle_filled_secondary_100_radius_16dp)
                .into(binding.logoImageView)*/
        }
    }

    override fun onLoading() {
        super.onLoading()
    }

    override fun onError() {
        super.onError()
    }

    override fun onContent() {
        super.onContent()
    }

}
