package com.custom.rgs_android_dom.ui.property.info

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentPropertyInfoBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.property.add.details.files.PropertyUploadDocumentsFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PropertyInfoFragment :
    BaseBottomSheetFragment<PropertyInfoViewModel, FragmentPropertyInfoBinding>() {

    override val TAG: String = "PROPERTY_INFO_FRAGMENT"

    private val adapter: PropertyDocumentsAdapter
        get() = binding.listDocumentsRecyclerView.adapter as PropertyDocumentsAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listDocumentsRecyclerView.adapter = PropertyDocumentsAdapter()

        binding.backImageView.setOnDebouncedClickListener {
            onClose()
        }

        binding.moreImageView.setOnDebouncedClickListener {

        }

        binding.uploadDocumentFrameLayout.setOnDebouncedClickListener {
            val propertyUploadFilesFragment = PropertyUploadDocumentsFragment()
            propertyUploadFilesFragment.show(childFragmentManager, propertyUploadFilesFragment.TAG)
        }

        subscribe(viewModel.propertyItemObserver) { propertyItem ->
            when (propertyItem.type) {
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
            binding.subtitleTextView.text = propertyItem.address?.address ?: ""
            binding.addressTextView.setValue(propertyItem.address?.address ?: "")

            propertyItem.isOwn?.let { isOwn ->
                binding.isOwnTextView.setValue(if (isOwn) "Да" else "Нет")
            }
            propertyItem.isRent?.let { isRent ->
                binding.isRentTextView.setValue(if (isRent) "Да" else "Нет")
            }
            propertyItem.isTemporary?.let { isTemporary ->
                binding.isTemporaryTextView.setValue(if (isTemporary) "Да" else "Нет")
            }

            propertyItem.totalArea?.let { totalArea ->
                binding.totalAreaTextView.setValue("$totalArea м²")
            }
            if (propertyItem.comment.isNotEmpty()) {
                binding.commentTextView.setValue(propertyItem.comment)
            }

            if (propertyItem.documents.isNotEmpty()) {
                adapter.setItems(propertyItem.documents)
            }

            binding.allDocumentsTextView.setOnDebouncedClickListener {
                viewModel.onShowAllDocumentsClick(propertyItem.id, propertyItem.documents)
            }
        }
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

}
