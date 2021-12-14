package com.custom.rgs_android_dom.ui.chat.call

import android.Manifest
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentCallBinding
import com.custom.rgs_android_dom.domain.chat.models.CallType
import com.custom.rgs_android_dom.domain.chat.models.ChannelMemberModel
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.chat.call.rationale.RequestMicCameraRationaleFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.activity.clearLightStatusBar
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import org.webrtc.RendererCommon

class CallFragment : BaseFragment<CallViewModel, FragmentCallBinding>(R.layout.fragment_call), RequestMicCameraRationaleFragment.OnDialogDismissListener {

    companion object {
        private const val ARG_CALL_TYPE = "ARG_CALL_TYPE"
        private const val ARG_CONSULTANT = "ARG_CHANNEL_MEMBER"

        private const val REQUEST_CODE_MIC = 1
        private const val REQUEST_CODE_MIC_AND_CAMERA = 2

        fun newInstance(callType: CallType, consultant: ChannelMemberModel?): CallFragment {
            return CallFragment().args {
                putSerializable(ARG_CALL_TYPE, callType)
                if (consultant != null){
                    putSerializable(ARG_CONSULTANT, consultant)
                }

            }
        }
    }

    private val requestMicAndCameraPermissionsAction =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
            if (permissionsResult[Manifest.permission.CAMERA] == true
                && permissionsResult[Manifest.permission.RECORD_AUDIO] == true
                && permissionsResult[Manifest.permission.MODIFY_AUDIO_SETTINGS] == true){
                viewModel.onVideoCallPermissionsGranted(true)

                binding.waitingCameraPermissionProgressBar.gone()
                binding.cameraOffImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_camera_off_24px))
                binding.cameraOffImageView.isActivated = false
                binding.micOffImageView.isActivated = false

            } else {
                viewModel.onVideoCallPermissionsGranted(false)
                showRequestRecordVideoRationaleDialog()
            }
        }

    private val requestMicPermissionsAction =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
            if (permissionsResult[Manifest.permission.RECORD_AUDIO] == true
                && permissionsResult[Manifest.permission.MODIFY_AUDIO_SETTINGS] == true){
                viewModel.onAudioCallPermissionsGranted(true)
                binding.micOffImageView.isActivated = false
            } else {
                viewModel.onAudioCallPermissionsGranted(false)
                showRequestRecordAudioRationaleDialog()
            }
        }

    private var previousSpeakerphoneOn = true
    private var previousMicrophoneMute = false
    private var renderersInited = false

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getSerializable(ARG_CALL_TYPE) as CallType,
            if (requireArguments().containsKey(ARG_CONSULTANT))
                requireArguments().getSerializable(ARG_CONSULTANT) as ChannelMemberModel else null

        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.endCallImageView.setOnDebouncedClickListener {
            viewModel.onRejectClick()
        }

        binding.micOffImageView.setOnDebouncedClickListener {
            viewModel.onEnableMicCall(binding.micOffImageView.isActivated)
        }

        binding.cameraOffImageView.setOnDebouncedClickListener {
            viewModel.onEnableCameraClick(binding.cameraOffImageView.isActivated)
        }

        binding.switchCameraImageView.setOnDebouncedClickListener {

        }

        binding.switchSurfacesImageView.setOnDebouncedClickListener {

        }

        binding.minimizeImageView.setOnDebouncedClickListener {
            viewModel.onMinimizeClick()
        }

        subscribe(viewModel.callTypeObserver) {
            when (it) {
                CallType.AUDIO_CALL -> {

                    requestMicPermissionsAction.launch(
                        arrayOf(
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS
                        )
                    )
                }
                CallType.VIDEO_CALL -> {
                    binding.waitingConsultantVideoFrameLayout.visible()
                    binding.waitingCameraPermissionProgressBar.visible()
                    binding.cameraOffImageView.setImageDrawable(null)
                    binding.cameraOffImageView.isActivated = true

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
            if (!renderersInited){
                roomInfo.room?.let { room ->
                    room.initVideoRenderer(binding.primarySurfaceRenderer)
                    room.initVideoRenderer(binding.secondarySurfaceRenderer)

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

                    renderersInited = true

                }
            }

            binding.micOffImageView.isActivated = roomInfo.micEnabled == false
            binding.cameraOffImageView.isActivated = roomInfo.cameraEnabled == false

            if (roomInfo.myVideoTrack != null && roomInfo.cameraEnabled){
                binding.secondarySurfaceContainer.visible()
                roomInfo.myVideoTrack?.addRenderer(binding.secondarySurfaceRenderer)
            } else {
                binding.secondarySurfaceContainer.gone()
            }

            if (roomInfo.consultantVideoTrack != null){
                binding.primarySurfaceRenderer.visible()
                roomInfo.consultantVideoTrack?.addRenderer(binding.primarySurfaceRenderer)
                binding.primarySurfaceRenderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL, RendererCommon.ScalingType.SCALE_ASPECT_FILL)

                if (roomInfo.callType == CallType.VIDEO_CALL){
                    binding.waitingConsultantVideoFrameLayout.gone()
                }
            } else {
                binding.primarySurfaceRenderer.gone()
            }

            // TODO change z-order of surface views according to variable roomInfo?.videoTracksSwitched


        }

        subscribe(viewModel.consultantObserver){consultant->
            binding.consultantNameTextView.text = "${consultant.lastName} ${consultant.firstName}, консультант"

            if (consultant.avatar.isNotEmpty()) {
                GlideApp.with(binding.avatarImageView)
                    .load(GlideUrlProvider.makeHeadersGlideUrl(consultant.avatar))
                    .transform(RoundedCorners(32.dp(requireContext())))
                    .error(R.drawable.ic_call_consultant)
                    .into(binding.avatarImageView)
            } else {
                binding.avatarImageView.setImageResource(R.drawable.ic_call_consultant)
            }
        }

        subscribe(viewModel.callTimeObserver){
            binding.titleTextView.text = "Мастер онлайн"
            binding.subtitleTextView.text = it
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.primarySurfaceRenderer.release()
        binding.secondarySurfaceRenderer.release()
        val audioManager = requireContext().getSystemService(AUDIO_SERVICE) as AudioManager
        with(audioManager) {
            isSpeakerphoneOn = previousSpeakerphoneOn
            isMicrophoneMute = previousMicrophoneMute
            mode = AudioManager.MODE_NORMAL
        }

    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.secondary900)
        requireActivity().clearLightStatusBar()
    }

    private fun showRequestRecordAudioRationaleDialog(){
        val requestMicCameraRationaleFragment = RequestMicCameraRationaleFragment.newInstance(REQUEST_CODE_MIC)
        requestMicCameraRationaleFragment.show(childFragmentManager, requestMicCameraRationaleFragment.TAG)
    }

    private fun showRequestRecordVideoRationaleDialog(){
        val requestMicCameraRationaleFragment = RequestMicCameraRationaleFragment.newInstance(REQUEST_CODE_MIC_AND_CAMERA)
        requestMicCameraRationaleFragment.show(childFragmentManager, requestMicCameraRationaleFragment.TAG)
    }

    override fun onDialogDismiss(requestCode: Int?) {

    }

}