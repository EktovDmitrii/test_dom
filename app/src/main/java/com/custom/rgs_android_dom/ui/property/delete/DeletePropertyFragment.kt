package com.custom.rgs_android_dom.ui.property.delete

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentDeletePropertyBinding
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

    private var canBeClosed: Boolean = false

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

        binding.tryAgainTextView.setOnDebouncedClickListener {
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
                val propertyLogo = when (propertyItem.type) {
                    PropertyType.HOUSE -> {
                        R.drawable.ic_type_home
                    }
                    PropertyType.APARTMENT -> {
                        R.drawable.ic_type_apartment_334px
                    }
                    else -> null
                }
                GlideApp.with(binding.root.context)
                    .load(propertyLogo)
                    .apply(
                        RequestOptions().transform(
                            CenterCrop(),
                            RoundedCorners(16.dp(binding.root.context))
                        )
                    )
                    .error(R.drawable.rectangle_filled_secondary_100_radius_16dp)
                    .into(binding.propertyLogoImageView)
            }
        }

        subscribe(viewModel.cannotBeDeletedObserver){
            binding.deletePropertyTextView.gone()
            binding.tryAgainTextView.gone()
            binding.nameTextView.visible()
            binding.addressTextView.visible()
            binding.contactMasterOnlineTextView.visible()

            binding.propertyLogoImageView.visible()
            binding.errorImageView.gone()

            binding.titleTextView.text = "Невозможно удалить"

            binding.subtitleTextView.text = "К этой недвижимости привязан ваш\nдействующий страховой полис, поэтому\nеё нельзя удалить самостоятельно."
            binding.subtitleTextView.visible()

            binding.closeTextView.visible()
            binding.closeTextView.text = "Всё понятно"
            binding.closeTextView.setOnDebouncedClickListener {
                viewModel.close()
            }

            dialog?.setCancelable(true)
            dialog?.setCanceledOnTouchOutside(true)

        }
    }

    override fun onLoading() {
        super.onLoading()
        binding.propertyLogoImageView.visible()
        binding.nameTextView.visible()
        binding.addressTextView.visible()
        binding.errorImageView.gone()

        binding.titleTextView.text = "Удалить недвижимость?"

        binding.deletePropertyTextView.setLoading(true)
        binding.tryAgainTextView.gone()
        binding.closeTextView.invisible()
        binding.subtitleTextView.gone()

        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun onError() {
        super.onError()

        binding.propertyLogoImageView.gone()
        binding.nameTextView.gone()
        binding.addressTextView.gone()
        binding.errorImageView.visible()

        binding.titleTextView.text = "Невозможно обработать"
        binding.subtitleTextView.text = "Мы не смогли обработать\nваш запрос. Пожалуйста, попробуйте\nеще раз"
        binding.subtitleTextView.visible()

        binding.deletePropertyTextView.gone()
        binding.deletePropertyTextView.setLoading(false)

        binding.tryAgainTextView.visible()
        binding.closeTextView.visible()
        binding.closeTextView.text = "Написать онлайн мастеру"
        binding.closeTextView.setOnDebouncedClickListener {
            viewModel.onContactMasterOnlineClick()
        }

        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
    }
}
