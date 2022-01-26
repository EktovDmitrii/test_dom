package com.custom.rgs_android_dom.ui.policies.insurant

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentInsurantBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.policies.insurant.dialogs.PolicyDialogsFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class InsurantFragment: BaseFragment<InsurantViewModel, FragmentInsurantBinding>(R.layout.fragment_insurant) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.nextTextView.setOnDebouncedClickListener {
            val dialog = PolicyDialogsFragment()
            dialog.show(childFragmentManager, dialog.TAG)
        }

    }
}