package com.custom.rgs_android_dom.ui.chat.files.viewers.image

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentImageViewerBinding
import com.custom.rgs_android_dom.domain.chat.models.ChatFileModel
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.chat.files.manage.ManageFileFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.activity.clearLightStatusBar
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf


class ImageViewerFragment : BaseFragment<ImageViewerViewModel, FragmentImageViewerBinding>(R.layout.fragment_image_viewer) {

    companion object {
        private const val ARG_FILE = "ARG_FILE"

        fun newInstance(chatFile: ChatFileModel): ImageViewerFragment {
            return ImageViewerFragment().args {
                putSerializable(ARG_FILE, chatFile)
            }
        }

    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_FILE) as ChatFileModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.moreImageView.setOnDebouncedClickListener {
            viewModel.onMoreClick()
        }

        subscribe(viewModel.urlObserver){
            GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(it))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.pictureZoomageView)
        }

        subscribe(viewModel.dateObserver){
            binding.dateTextView.text = it
        }

        subscribe(viewModel.nameObserver){
            binding.titleTextView.text = it
        }

        subscribe(viewModel.manageFileObserver){
            val manageFilesFragment = ManageFileFragment.newInstance(it)
            manageFilesFragment.show(childFragmentManager, manageFilesFragment.TAG)
        }

    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.black)
        requireActivity().clearLightStatusBar()
    }

}