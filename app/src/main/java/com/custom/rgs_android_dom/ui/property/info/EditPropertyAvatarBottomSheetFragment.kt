package com.custom.rgs_android_dom.ui.property.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentEditPropertyAvatarBottomSheetBinding
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.goneIf
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable

class EditPropertyAvatarBottomSheetFragment : BottomSheetDialogFragment() {

    private var editPropertyAvatarInfoListener: EditPropertyAvatarInfoListener? = null

    private val binding: FragmentEditPropertyAvatarBottomSheetBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    companion object {
        const val TAG: String = "EDIT_PROPERTY_AVATAR_INFO_FRAGMENT"
        private const val ARG_AVATAR_IS_EXIST: String = "ARG_AVATAR_IS_EXIST"

        fun newInstance(isExistAvatar: Boolean): EditPropertyAvatarBottomSheetFragment =
            EditPropertyAvatarBottomSheetFragment().args {
                putBoolean(ARG_AVATAR_IS_EXIST, isExistAvatar)
            }
    }

    override fun getTheme(): Int {
        return R.style.BottomSheet
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is EditPropertyAvatarInfoListener) {
            editPropertyAvatarInfoListener = parentFragment as EditPropertyAvatarInfoListener
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isExistAvatar = requireArguments().getBoolean(ARG_AVATAR_IS_EXIST, false)
        binding.deletePropertyAvatarTextView.goneIf(!isExistAvatar)
        binding.editPropertyAvatarTextView.setOnDebouncedClickListener {
            editPropertyAvatarInfoListener?.onLoadAvatarClicked()
            dismissAllowingStateLoss()
        }
        binding.deletePropertyAvatarTextView.setOnDebouncedClickListener {
            editPropertyAvatarInfoListener?.onDeleteAvatarClicked()
            dismissAllowingStateLoss()
        }
        binding.cancelPropertyAvatarTextView.setOnDebouncedClickListener {
            dismissAllowingStateLoss()
        }
    }

    interface EditPropertyAvatarInfoListener : Serializable {
        fun onLoadAvatarClicked()
        fun onDeleteAvatarClicked()
    }
}