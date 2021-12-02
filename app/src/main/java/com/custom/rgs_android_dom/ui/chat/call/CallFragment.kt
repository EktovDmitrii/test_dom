package com.custom.rgs_android_dom.ui.chat.call

import android.Manifest
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentCallBinding
import com.custom.rgs_android_dom.domain.chat.models.CallType
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.activity.clearLightStatusBar
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import org.webrtc.RendererCommon

class CallFragment : BaseFragment<CallViewModel, FragmentCallBinding>(R.layout.fragment_call) {

    companion object {
        private const val ARG_CALL_TYPE = "ARG_CALL_TYPE"

        fun newInstance(callType: CallType): CallFragment {
            return CallFragment().args {
                putSerializable(ARG_CALL_TYPE, callType)
            }
        }
    }

    private val requestMicAndCameraPermissionsAction =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
            if (permissionsResult[Manifest.permission.CAMERA] == true
                && permissionsResult[Manifest.permission.RECORD_AUDIO] == true
                && permissionsResult[Manifest.permission.MODIFY_AUDIO_SETTINGS] == true){
                viewModel.onVideoCallPermissionsGranted()
            }
            else if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)
                && !ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.RECORD_AUDIO)
                && !ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.MODIFY_AUDIO_SETTINGS)) {
                //viewModel.onShouldRequestLocationPermissionsRationale()
            } else {
                //viewModel.onLocationPermissionsNotGranted()
            }
        }

    private val requestMicPermissionsAction =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
            if (permissionsResult[Manifest.permission.RECORD_AUDIO] == true
                && permissionsResult[Manifest.permission.MODIFY_AUDIO_SETTINGS] == true){
                viewModel.onAudioCallPermissionsGranted()
            }
            else if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.RECORD_AUDIO)
                && !ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.MODIFY_AUDIO_SETTINGS)) {
                //viewModel.onShouldRequestLocationPermissionsRationale()
            } else {
                //viewModel.onLocationPermissionsNotGranted()
            }
        }

    private var previousSpeakerphoneOn = true
    private var previousMicrophoneMute = false

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_CALL_TYPE) as CallType)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rejectImageView.setOnDebouncedClickListener {
            viewModel.onRejectClick()
        }

        subscribe(viewModel.callTypeObserver) {
            when (it) {
                CallType.AUDIO_CALL -> {
                    binding.videoContainer.visibility = View.GONE
                    binding.titleTextView.text = "Звонок"

                    requestMicPermissionsAction.launch(
                        arrayOf(
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS
                        )
                    )
                }
                CallType.VIDEO_CALL -> {
                    binding.videoContainer.visibility = View.VISIBLE
                    binding.titleTextView.text = "Консультант"

                    requestMicAndCameraPermissionsAction.launch(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS
                        )
                    )
                }
            }
        }

        subscribe(viewModel.roomInfoObserver){roomInfo->
            roomInfo.room?.let { room ->
                room.initVideoRenderer(binding.mySurfaceRenderer)
                room.initVideoRenderer(binding.opponentSurfaceRenderer)
                val audioManager = requireContext().getSystemService(AUDIO_SERVICE) as AudioManager
                with(audioManager) {
                    previousSpeakerphoneOn = isSpeakerphoneOn
                    previousMicrophoneMute = isMicrophoneMute
                    isSpeakerphoneOn = true
                    isMicrophoneMute = false
                    mode = AudioManager.MODE_IN_COMMUNICATION
                }
                val result = audioManager.requestAudioFocus(
                    null,
                    AudioManager.STREAM_VOICE_CALL,
                    AudioManager.AUDIOFOCUS_GAIN,
                )
            }

            roomInfo.myVideoTrack?.let {
                it.addRenderer(binding.mySurfaceRenderer)
            }

            roomInfo.opponentVideoTrack?.let {
                it.addRenderer(binding.opponentSurfaceRenderer)
                binding.opponentSurfaceRenderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL, RendererCommon.ScalingType.SCALE_ASPECT_FILL)

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.opponentSurfaceRenderer.release()
        binding.mySurfaceRenderer.release()
        val audioManager = requireContext().getSystemService(AUDIO_SERVICE) as AudioManager
        with(audioManager) {
            isSpeakerphoneOn = previousSpeakerphoneOn
            isMicrophoneMute = previousMicrophoneMute
            mode = AudioManager.MODE_NORMAL
        }

    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.black)
        requireActivity().clearLightStatusBar()
    }

}