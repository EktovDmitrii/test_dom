package com.custom.rgs_android_dom.ui.confirm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentConfirmBinding
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ConfirmBottomSheetFragment() : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ConfirmBottomSheetFragment"

        const val ARG_ICON = "ARG_ICON"
        const val ARG_TITLE = "ARG_TITLE"
        const val ARG_DESCRIPTION = "ARG_DESCRIPTION"
        const val ARG_CONFIRM_TEXT = "ARG_CONFIRM_TEXT"
        const val ARG_CANCEL_TEXT = "ARG_CANCEL_TEXT"

        fun newInstance(icon: Int,
                        title: String,
                        description: String,
                        confirmText: String,
                        cancelText: String): ConfirmBottomSheetFragment {
            return ConfirmBottomSheetFragment().args {
                putInt(ARG_ICON, icon)
                putString(ARG_TITLE, title)
                putString(ARG_DESCRIPTION, description)
                putString(ARG_CONFIRM_TEXT, confirmText)
                putString(ARG_CANCEL_TEXT, cancelText)
            }

        }
    }

    private val binding: FragmentConfirmBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private var confirmListener: ConfirmListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (parentFragment is ConfirmListener){
            confirmListener = parentFragment as ConfirmListener
        }
        return binding.root
    }


    override fun getTheme(): Int {
        return R.style.BottomSheet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.iconImageView.setImageResource(requireArguments().getInt(ARG_ICON))
        binding.titleTextView.text = requireArguments().getString(ARG_TITLE)
        binding.descriptionTextView.text = requireArguments().getString(ARG_DESCRIPTION)
        binding.confirmTextView.setText(requireArguments().getString(ARG_CONFIRM_TEXT) ?: "")
        binding.cancelTextView.text = requireArguments().getString(ARG_CANCEL_TEXT) ?: ""

        binding.confirmTextView.setOnDebouncedClickListener {
            dismissAllowingStateLoss()
            confirmListener?.onConfirmClick()

        }

        binding.cancelTextView.setOnDebouncedClickListener {
            dismissAllowingStateLoss()
            confirmListener?.onCancelClick()
        }
    }

    interface ConfirmListener {
        fun onConfirmClick() {}
        fun onCancelClick() {}
    }
}