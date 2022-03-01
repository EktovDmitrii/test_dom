package com.custom.rgs_android_dom.ui.property.info.edit.avatar

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.custom.rgs_android_dom.databinding.FragmentEditPropertyAvatarBottomSheetBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.*

class EditPropertyAvatarBottomSheetFragment : BaseBottomSheetModalFragment<EditPropertyAvatarBottomSheetViewModel, FragmentEditPropertyAvatarBottomSheetBinding>() {

    companion object {
        private const val ARG_AVATAR_IS_EXIST: String = "ARG_AVATAR_IS_EXIST"

        fun newInstance(isExistAvatar: Boolean): EditPropertyAvatarBottomSheetFragment =
            EditPropertyAvatarBottomSheetFragment().args {
                putBoolean(ARG_AVATAR_IS_EXIST, isExistAvatar)
            }
    }

    override val TAG: String = "ADD_PHOTO_FRAGMENT"

    private val getPhotoFromGalleryAction =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.convertToFile(requireActivity())?.let {
                viewModel.onAvatarSelected(it)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isExistAvatar = requireArguments().getBoolean(ARG_AVATAR_IS_EXIST, false)
        binding.deletePropertyAvatarTextView.goneIf(!isExistAvatar)
        binding.dividerView1.goneIf(!isExistAvatar)

        binding.editPropertyAvatarTextView.setOnDebouncedClickListener {
            getPhotoFromGalleryAction.launch("image/*")
        }

        binding.deletePropertyAvatarTextView.setOnDebouncedClickListener {
            viewModel.onDeleteAvatarClick()
        }
    }

}