package com.custom.rgs_android_dom.ui.property.info

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentEditPropertyInfoBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*

class EditPropertyInfoFragment :
    BaseFragment<EditPropertyInfoViewModel, FragmentEditPropertyInfoBinding>(R.layout.fragment_edit_property_info) {

    companion object {

        fun newInstance(): EditPropertyInfoFragment {
            return EditPropertyInfoFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            hideSoftwareKeyboard(true)
            viewModel.onBackClick()
        }

        binding.addTextView.setOnDebouncedClickListener {
            hideSoftwareKeyboard(true)
        }
    }

}