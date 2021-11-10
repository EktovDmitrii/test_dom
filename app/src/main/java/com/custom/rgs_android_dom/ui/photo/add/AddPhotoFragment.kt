package com.custom.rgs_android_dom.ui.photo.add

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentAddPhotoBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.convertToPhotoFile
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf

class AddPhotoFragment() : BaseBottomSheetFragment<AddPhotoViewModel, FragmentAddPhotoBinding>() {

    override val TAG: String = "ADD_PHOTO_FRAGMENT"

    private val getPhotoFromGalleryAction =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.convertToPhotoFile(requireActivity())?.let {
                //presenter.onPhotoSelected(it)
                Log.d("MyLog", "PHOTO " + it.absolutePath)
                viewModel.onAvatarSelected(it)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.uploadPhotoTextView.setOnDebouncedClickListener {
            getPhotoFromGalleryAction.launch("image/*")
        }

        Log.d("MyLog", "On view created")

        subscribe(viewModel.isDeleteTextViewVisibleObserver){
            Log.d("MyLog", "Is not empty avatar " + it)
            binding.dividerView.visibleIf(it)
            binding.deletePhotoTextView.visibleIf(it)
        }
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheet
    }


}