package com.custom.rgs_android_dom.ui.chat.call.media_output_chooser

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
            Log.d("MyLog", "Set items " + it.size)
            adapter.setItems(it)
        }

        val intent = Intent("com.android.settings.panel.action.MEDIA_OUTPUT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        requireContext().startActivity(intent)
    }

}