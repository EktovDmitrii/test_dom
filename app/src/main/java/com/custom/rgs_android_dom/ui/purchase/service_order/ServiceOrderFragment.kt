package com.custom.rgs_android_dom.ui.purchase.service_order

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentServiceOrderBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseDateTimeModel
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.purchase.add.comment.PurchaseCommentListener
import com.custom.rgs_android_dom.ui.purchase.select.address.SelectPurchaseAddressListener
import com.custom.rgs_android_dom.ui.purchase.select.date_time.PurchaseDateTimeFragment
import com.custom.rgs_android_dom.utils.*
import com.yandex.metrica.YandexMetrica
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class ServiceOrderFragment :
    BaseFragment<ServiceOrderViewModel, FragmentServiceOrderBinding>(R.layout.fragment_service_order),
    SelectPurchaseAddressListener,
    PurchaseCommentListener,
    PurchaseDateTimeFragment.PurchaseDateTimeListener {

    companion object {
        private const val ARG_SERVICE_ORDER_LAUNCHER = "ARG_SERVICE_ORDER_LAUNCHER"

        fun newInstance(serviceOrderLauncher: ServiceOrderLauncher): ServiceOrderFragment {
            return ServiceOrderFragment().args {
                putSerializable(ARG_SERVICE_ORDER_LAUNCHER, serviceOrderLauncher)
            }
        }
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary500)
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getSerializable(ARG_SERVICE_ORDER_LAUNCHER)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutPurchaseServiceHeader.orderTitleTextView.text = "Ваш заказ"
        YandexMetrica.reportEvent("service_order_paid_in_pack")

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.layoutProperty.root.setOnDebouncedClickListener {
            viewModel.onSelectPropertyClick(childFragmentManager)
        }

        binding.layoutNoProperty.root.setOnDebouncedClickListener {
            viewModel.onSelectPropertyClick(childFragmentManager)
        }

        binding.layoutProperty.addCommentTextView.setOnDebouncedClickListener {
            viewModel.onAddCommentClick(childFragmentManager)
        }

        binding.layoutDateTime.root.setOnDebouncedClickListener {
            viewModel.onSelectOrderDateClick(childFragmentManager)
        }

        binding.orderTextView.setOnDebouncedClickListener {
            viewModel.onOrderClick()
        }

        subscribe(viewModel.serviceObserver){service->
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(service.logoSmall))
                .transform(RoundedCorners(20.dp(requireContext())))
                .into(binding.layoutPurchaseServiceHeader.logoImageView)

            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(service.iconLink))
                .transform(RoundedCorners(4.dp(requireContext())))
                .error(R.drawable.rectangle_filled_secondary_100_radius_8dp)
                .into(binding.layoutIncludedProduct.includedProductImageView)

            binding.layoutPurchaseServiceHeader.nameTextView.text = service.name
            binding.layoutPurchaseServiceHeader.durationTextView.text = "Работа займёт ~${service.deliveryTime}"
            binding.layoutPurchaseServiceHeader.priceTextView.text = "0 ₽"
        }

        subscribe(viewModel.serviceOrderViewStateObserver){ viewState->
            binding.orderTextView.isEnabled = viewState.isServiceOrderTextViewEnabled

            binding.layoutProperty.addCommentTextView.text = if (viewState.comment.isNullOrEmpty()){
                "Добавить комментарий"
            } else {
                "Редактировать комментарий"
            }

            binding.layoutProperty.root.visibleIf(viewState.property != null)
            binding.layoutNoProperty.root.visibleIf(viewState.property == null)

            viewState.property?.let {property->
                binding.layoutProperty.propertyTypeTextView.text = property.name
                binding.layoutProperty.propertyAddressTextView.text = property.address?.address

                if (property.photoLink != null) {
                    GlideApp.with(requireContext())
                        .load(GlideUrlProvider.makeHeadersGlideUrl(property.photoLink))
                        .transform(RoundedCorners(4.dp(requireContext())))
                        .into(binding.layoutProperty.propertyTypeImageView)
                } else {
                    when (property.type) {
                        PropertyType.HOUSE -> {
                            binding.layoutProperty.propertyTypeImageView.setImageResource(
                                R.drawable.ic_type_home
                            )
                        }
                        PropertyType.APARTMENT -> {
                            binding.layoutProperty.propertyTypeImageView.setImageResource(
                                R.drawable.ic_type_apartment_334px
                            )
                        }
                        else -> throw IllegalArgumentException("Wrong argument for ${property.type}")
                    }
                }
            }

            viewState.orderDate?.let { orderDate->
                binding.layoutDateTime.filledDateTimeGroup.visible()
                binding.layoutDateTime.chooseDateTimeTextView.gone()
                binding.layoutDateTime.timesOfDayTextView.text = "${orderDate.selectedPeriodModel?.timeFrom} – ${orderDate.selectedPeriodModel?.timeTo}"
                binding.layoutDateTime.timeIntervalTextView.text = orderDate.selectedDate.formatTo(DATE_PATTERN_DATE_FULL_MONTH)
            }
        }

        subscribe(viewModel.isOrderPerformingObserver){
            binding.orderTextView.setLoading(it)
        }

    }

    override fun onPropertySelected(propertyItemModel: PropertyItemModel) {
        viewModel.onPropertySelected(propertyItemModel)

        YandexMetrica.reportEvent("service_order_progress_address")
    }

    override fun onCommentSelected(comment: String) {
        viewModel.onCommentSelected(comment)
    }

    override fun onSelectDateTimeClick(purchaseDateTimeModel: PurchaseDateTimeModel) {
        viewModel.onOrderDateSelected(purchaseDateTimeModel)

        YandexMetrica.reportEvent("service_order_progress_date")
    }
}
