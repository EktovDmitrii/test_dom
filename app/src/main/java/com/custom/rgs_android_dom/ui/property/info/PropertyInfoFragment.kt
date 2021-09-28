package com.custom.rgs_android_dom.ui.property.info

import android.os.Bundle
import android.util.Log
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPropertyInfoBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PropertyInfoFragment: BaseBottomSheetFragment<PropertyInfoViewModel, FragmentPropertyInfoBinding>() {
    override val TAG: String = "PROPERTY_INFO_FRAGMENT"

    companion object {
        private const val ARG_OBJECT_ID = "ARG_OBJECT_ID"

        fun newInstance(objectId: String): PropertyInfoFragment {
            return PropertyInfoFragment().args {
                putString(ARG_OBJECT_ID, objectId)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_OBJECT_ID))
    }

    override fun getSwipeAnchor(): View? {
        return binding.swipeAnchorLayout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.scroller.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.d("SCROLL", "SCROLLED")
        }

        binding.backImageView.setOnDebouncedClickListener {
            ScreenManager.closeCurrentBottomFragment()
        }

        subscribe(viewModel.propertyItemObserver) {propertyItem->
            when (propertyItem.type){
                PropertyType.HOUSE -> {
                    binding.propertyImageView.setImageResource(R.drawable.ic_type_home)
                    binding.typeTextView.setValue("Дом")
                }
                PropertyType.APARTMENT -> {
                    binding.propertyImageView.setImageResource(R.drawable.ic_type_apartment)
                    binding.typeTextView.setValue("Квартира")
                }
            }
            binding.titleTextView.text = propertyItem.name
            binding.subtitleTextView.text = propertyItem.address
            binding.addressTextView.setValue(propertyItem.address)
            binding.isOwnTextView.setValue(if (propertyItem.isOwn) "Да" else "Нет")
            binding.isRentTextView.setValue(if (propertyItem.isRent) "Да" else "Нет")
            binding.isTemporaryTextView.setValue(if (propertyItem.isTemporary) "Да" else "Нет")
            propertyItem.totalArea?.let { totalArea->
                binding.totalAreaTextView.setValue("$totalArea м²")
            }
            if (propertyItem.comment.isNotEmpty()){
                binding.commentTextView.setValue(propertyItem.comment)
            }
        }
    }
}