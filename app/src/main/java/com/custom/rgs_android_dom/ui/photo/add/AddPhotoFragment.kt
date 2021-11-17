package com.custom.rgs_android_dom.ui.photo.add

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.custom.rgs_android_dom.databinding.FragmentAddPhotoBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.convertToFile
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf

class AddPhotoFragment() : BaseBottomSheetModalFragment<AddPhotoViewModel, FragmentAddPhotoBinding>() {

    override val TAG: String = "ADD_PHOTO_FRAGMENT"

    private val getPhotoFromGalleryAction =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.convertToFile(requireActivity())?.let {
                viewModel.onAvatarSelected(it)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.uploadPhotoTextView.setOnDebouncedClickListener {
            getPhotoFromGalleryAction.launch("image/*")
        }

        binding.deletePhotoTextView.setOnDebouncedClickListener {
            viewModel.onDeleteAvatarClick()
        }

        subscribe(viewModel.isDeleteTextViewVisibleObserver){
            binding.dividerView.visibleIf(it)
            binding.deletePhotoTextView.visibleIf(it)
        }
    }

}