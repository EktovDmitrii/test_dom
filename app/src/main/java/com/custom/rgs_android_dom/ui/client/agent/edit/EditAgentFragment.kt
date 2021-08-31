package com.custom.rgs_android_dom.ui.client.agent.edit

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentEditAgentBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*

class EditAgentFragment : BaseFragment<EditAgentViewModel, FragmentEditAgentBinding>(R.layout.fragment_edit_agent) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

    }

}