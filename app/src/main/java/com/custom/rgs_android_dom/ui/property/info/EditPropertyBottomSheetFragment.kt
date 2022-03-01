package com.custom.rgs_android_dom.ui.property.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentEditPropertyInfoBottomSheetBinding
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable

class EditPropertyBottomSheetFragment : BottomSheetDialogFragment() {

    private var editPropertyInfoListener: EditPropertyInfoListener? = null

    private val binding: FragmentEditPropertyInfoBottomSheetBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    companion object {
        const val TAG: String = "EDIT_PROPERTY_INFO_FRAGMENT"

        fun newInstance(): EditPropertyBottomSheetFragment =
            EditPropertyBottomSheetFragment()
    }

    override fun getTheme(): Int {
        return R.style.BottomSheet
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is EditPropertyInfoListener) {
            editPropertyInfoListener = parentFragment as EditPropertyInfoListener
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editPropertyInfoTextView.setOnDebouncedClickListener {
            editPropertyInfoListener?.onEditPropertyClicked()
            dismissAllowingStateLoss()
        }

        binding.deletePropertyInfoTextView.setOnDebouncedClickListener {
            editPropertyInfoListener?.onDeletePropertyClicked()
            dismissAllowingStateLoss()
        }
    }

    interface EditPropertyInfoListener : Serializable {
        fun onEditPropertyClicked()
        fun onDeletePropertyClicked()
    }
}