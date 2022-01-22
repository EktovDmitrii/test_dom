package com.custom.rgs_android_dom.ui.chat.call.media_output_chooser

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentMediaOutputChooserBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.subscribe

class MediaOutputChooserFragment : BaseBottomSheetModalFragment<MediaOutputChooserViewModel, FragmentMediaOutputChooserBinding>() {

    override val TAG: String = "MEDIA_OUTPUT_CHOOSER_FRAGMENT"

    private val adapter: MediaOutputsAdapter
        get() = binding.mediaOutputsRecyclerView.adapter as MediaOutputsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mediaOutputsRecyclerView.adapter = MediaOutputsAdapter(){
            viewModel.onMediaOutputSelected(it)
        }

        subscribe(viewModel.mediaOutputsObserver){
            adapter.setItems(it)
        }

    }

}