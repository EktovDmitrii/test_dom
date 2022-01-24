package com.custom.rgs_android_dom.ui.chat.files.viewers.video

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.ExoPlayerDataSourceProvider
import com.custom.rgs_android_dom.databinding.FragmentVideoPlayerBinding
import com.custom.rgs_android_dom.domain.chat.models.ChatFileModel
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.chat.files.manage.ManageFileFragment
import com.custom.rgs_android_dom.ui.chat.files.viewers.image.ImageViewerFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.activity.clearLightStatusBar
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf


class VideoPlayerFragment : BaseFragment<VideoPlayerViewModel, FragmentVideoPlayerBinding>(R.layout.fragment_video_player) {

    companion object {
        private const val ARG_FILE = "ARG_FILE"

        fun newInstance(chatFile: ChatFileModel): VideoPlayerFragment {
            return VideoPlayerFragment().args {
                putSerializable(ARG_FILE, chatFile)
            }
        }
    }

    private var player: ExoPlayer? = null

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
            /*GlideApp.with(requireContext())
                .load(GlideUrlProvider.makeHeadersGlideUrl(it))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.pictureZoomageView)*/
            playVideo(it)
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

    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
    }

    private fun playVideo(url: String){
        player = ExoPlayer.Builder(requireContext())
            .setMediaSourceFactory( DefaultMediaSourceFactory(ExoPlayerDataSourceProvider.prepareDataSource()).setLiveTargetOffsetMs(5000))
            .build()

        val mediaItem: MediaItem = MediaItem.Builder()
            .setUri(Uri.parse(url))
            .setLiveConfiguration(
                MediaItem.LiveConfiguration.Builder()
                    .setMaxPlaybackSpeed(1.02f)
                    .build()
            )
            .build()
        player?.setMediaItem(mediaItem)
        player?.playWhenReady = true
        player?.prepare()

        binding.videoPlayerView.player = player
    }

}