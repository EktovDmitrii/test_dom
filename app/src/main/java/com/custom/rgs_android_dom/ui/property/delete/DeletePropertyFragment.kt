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
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class DeletePropertyFragment : BaseBottomSheetModalFragment<DeletePropertyViewModel, FragmentDeletePropertyBinding>() {

    companion object {
        private const val ARG_PROPERTY = "ARG_PROPERTY"

        fun newInstance(property: PropertyItemModel): DeletePropertyFragment {
            return DeletePropertyFragment().args {
                putSerializable(ARG_PROPERTY, property)
            }
        }
    }

    override val TAG = "DELETE_PROPERTY_FRAGMENT"

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_PROPERTY) as PropertyItemModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.closeTextView.setOnDebouncedClickListener {
            viewModel.close()
        }

        binding.deletePropertyTextView.setOnDebouncedClickListener {
            viewModel.onDeleteClick()
        }

        binding.contactMasterOnlineTextView.setOnDebouncedClickListener {
            viewModel.onContactMasterOnlineClick()
        }

        subscribe(viewModel.propertyObserver){propertyItem->

            binding.nameTextView.text = propertyItem.name
            binding.addressTextView.text = propertyItem.address?.address

            if (propertyItem.photoLink != null){
                GlideApp.with(binding.root.context)
                .load(GlideUrlProvider.makeHeadersGlideUrl(propertyItem.photoLink))
                .apply(
                    RequestOptions().transform(
                        CenterCrop(),
                        RoundedCorners(16.dp(binding.root.context))
                    )
                )
                .error(R.drawable.rectangle_filled_secondary_100_radius_16dp)
                .into(binding.propertyLogoImageView)
            } else {
                when (propertyItem.type) {
                    PropertyType.HOUSE -> {
                        binding.propertyLogoImageView.setImageResource(R.drawable.ic_type_home)
                    }
                    PropertyType.APARTMENT -> {
                        binding.propertyLogoImageView.setImageResource(R.drawable.ic_type_apartment_334px)
                    }
                }
            }
        }

        subscribe(viewModel.cannotBeDeletedObserver){
            binding.deletePropertyTextView.gone()
            binding.contactMasterOnlineTextView.visible()

            binding.propertyLogoImageView.visible()
            binding.errorImageView.gone()

            binding.titleTextView.text = "Невозможно удалить"

            binding.subtitleTextView.text = "К этой недвижимости привязан ваш\nдействующий страховой полис, поэтому\nеё нельзя удалить самостоятельно."
            binding.subtitleTextView.visible()

            binding.closeTextView.visible()
            binding.closeTextView.text = "Всё понятно"

        }
    }

    override fun onLoading() {
        super.onLoading()

        binding.propertyLogoImageView.visible()
        binding.errorImageView.gone()

        binding.titleTextView.text = "Удалить недвижимость?"

        binding.deletePropertyTextView.setLoading(true)
        binding.closeTextView.invisible()
        binding.subtitleTextView.gone()
    }

    override fun onError() {
        super.onError()

        binding.propertyLogoImageView.gone()
        binding.errorImageView.visible()

        binding.deletePropertyTextView.setText("Попробовать ещё раз")
        binding.deletePropertyTextView.setLoading(false)
        binding.closeTextView.visible()
    }
}
