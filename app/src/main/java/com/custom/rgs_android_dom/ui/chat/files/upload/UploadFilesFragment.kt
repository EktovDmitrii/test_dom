package com.custom.rgs_android_dom.ui.chat.files

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.custom.rgs_android_dom.databinding.FragmentAddPhotoBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.convertToPhotoFile
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf

class AddFilesFragment() : BaseBottomSheetModalFragment<AddFilesViewModel, FragmentAddPhotoBinding>() {

    override val TAG: String = "ADD_FILES_FRAGMENT"

    private val getPhotoFromGalleryAction =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.convertToPhotoFile(requireActivity())?.let {
                //presenter.onPhotoSelected(it)
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