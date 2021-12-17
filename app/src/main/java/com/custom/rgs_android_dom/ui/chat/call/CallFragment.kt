package com.custom.rgs_android_dom.ui.chat.call

import android.Manifest
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentCallBinding
import com.custom.rgs_android_dom.domain.chat.models.CallType
import com.custom.rgs_android_dom.domain.chat.models.ChannelMemberModel
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.rationale.RequestRationaleFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.activity.clearLightStatusBar
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import org.webrtc.RendererCommon

class CallFragment : BaseFragment<CallViewModel, FragmentCallBinding>(R.layout.fragment_call), RequestRationaleFragment.OnRequestRationaleDismissListener {

    companion object {
        private const val ARG_CALL_TYPE = "ARG_CALL_TYPE"
        private const val ARG_CONSULTANT = "ARG_CHANNEL_MEMBER"

        private const val REQUEST_CODE_MIC = 1
        private const val REQUEST_CODE_MIC_AND_CAMERA = 2

        private const val SMALL_SCREEN_WIDTH = 136
        private const val SMALL_SCREEN_HEIGHT = 180
        private const val SMALL_SCREEN_MARGIN = 16

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
                binding.cameraOffImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_call_camera_selector))
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
            if (requireArguments().containsKey(ARG_CONSULTANT)) requireArguments().getSerializable(ARG_CONSULTANT) as ChannelMemberModel else null
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSoftwareKeyboard()

        binding.endCallImageView.setOnDebouncedClickListener {
            viewModel.onRejectClick()
        }

        binding.micOffImageView.setOnDebouncedClickListener {
            if (hasPermissions(Manifest.permission.RECORD_AUDIO)){
                viewModel.onEnableMicClick(binding.micOffImageView.isActivated)
            } else {
                showRequestRecordAudioRationaleDialog()
            }
        }

        binding.cameraOffImageView.setOnDebouncedClickListener {
            if (hasPermissions(Manifest.permission.CAMERA)){
                viewModel.onEnableCameraClick(binding.cameraOffImageView.isActivated)
            } else{
                showRequestRecordVideoRationaleDialog()
            }
        }

        binding.switchCameraImageView.setOnDebouncedClickListener {
            if (hasPermissions(Manifest.permission.CAMERA)){
                viewModel.onSwitchCameraClick()
            } else {
                showRequestRecordVideoRationaleDialog()
            }
        }

        binding.switchSurfacesConsultantImageView.setOnDebouncedClickListener {
            viewModel.onVideoTrackSwitchClick(false)
        }

        binding.switchSurfacesMyImageView.setOnDebouncedClickListener {
            viewModel.onVideoTrackSwitchClick(true)
        }

        binding.minimizeImageView.setOnDebouncedClickListener {
            viewModel.onMinimizeClick()
        }

        subscribe(viewModel.callTypeObserver) {
            when (it) {
                CallType.AUDIO_CALL -> {
                    if (binding.switchCameraImageView.isEnabled) binding.switchCameraImageView.isEnabled = false
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
                    room.initVideoRenderer(binding.consultantSurfaceRenderer)
                    room.initVideoRenderer(binding.mySurfaceRenderer)

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
            binding.switchCameraImageView.isEnabled = roomInfo.cameraEnabled
            binding.micOffImageView.isActivated = roomInfo.micEnabled == false
            binding.cameraOffImageView.isActivated = roomInfo.cameraEnabled == false

            if (roomInfo.consultantVideoTrack != null){
                binding.consultantSurfaceRenderer.visible()
                roomInfo.consultantVideoTrack?.addRenderer(binding.consultantSurfaceRenderer)
                binding.consultantSurfaceRenderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL, RendererCommon.ScalingType.SCALE_ASPECT_FILL)

                if (roomInfo.callType == CallType.VIDEO_CALL){
                    binding.waitingConsultantVideoFrameLayout.gone()
                }
            } else {
                binding.consultantSurfaceRenderer.gone()
            }

            if (roomInfo.videoTracksSwitched){
                binding.consultantSurfaceContainer.z = 1F
                binding.mySurfaceContainer.z = 0f
                setMyVideoFullScreen()
            } else {
                binding.consultantSurfaceContainer.z = 0F
                binding.mySurfaceContainer.z = 1f
                setConsultantVideoFullScreen()
            }

            if (roomInfo.myVideoTrack != null && roomInfo.cameraEnabled){
                binding.mySurfaceContainer.visible()
                roomInfo.myVideoTrack?.addRenderer(binding.mySurfaceRenderer)
            } else {
                binding.mySurfaceContainer.gone()
            }

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

    private fun setConsultantVideoFullScreen() {

        binding.consultantSurfaceContainer.visible()
        binding.switchSurfacesConsultantImageView.gone()

        with(binding.consultantSurfaceContainer.layoutParams as FrameLayout.LayoutParams){
            width = MATCH_PARENT
            height = MATCH_PARENT
            setMargins(0,0,0,0)
            gravity = Gravity.CENTER
            binding.consultantSurfaceContainer.layoutParams = this
        }

        binding.mySurfaceContainer.visible()
        binding.switchSurfacesMyImageView.visible()

        with(binding.mySurfaceContainer.layoutParams as FrameLayout.LayoutParams){
            width = SMALL_SCREEN_WIDTH.dp(requireContext())
            height = SMALL_SCREEN_HEIGHT.dp(requireContext())
            setMargins(0,0, SMALL_SCREEN_MARGIN.dp(requireContext()), SMALL_SCREEN_MARGIN.dp(requireContext()))
            gravity = Gravity.BOTTOM or Gravity.END
            binding.mySurfaceContainer.layoutParams = this
        }

    }

    private fun setMyVideoFullScreen() {

        binding.mySurfaceContainer.visible()
        binding.switchSurfacesMyImageView.gone()

        with(binding.mySurfaceContainer.layoutParams as FrameLayout.LayoutParams){
            width = MATCH_PARENT
            height = MATCH_PARENT
            setMargins(0,0,0,0)
            gravity = Gravity.CENTER
            binding.mySurfaceContainer.layoutParams = this
        }

        binding.consultantSurfaceContainer.visible()
        binding.switchSurfacesConsultantImageView.visible()

        with(binding.consultantSurfaceContainer.layoutParams as FrameLayout.LayoutParams){
            width = SMALL_SCREEN_WIDTH.dp(requireContext())
            height = SMALL_SCREEN_HEIGHT.dp(requireContext())
            setMargins(0,0, SMALL_SCREEN_MARGIN.dp(requireContext()), SMALL_SCREEN_MARGIN.dp(requireContext()))
            gravity = Gravity.BOTTOM or Gravity.END
            binding.consultantSurfaceContainer.layoutParams = this
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.consultantSurfaceRenderer.release()
        binding.mySurfaceRenderer.release()
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
        val requestRationaleFragment = RequestRationaleFragment.newInstance(
            requestCode = REQUEST_CODE_MIC,
            description = "Разрешите доступ, чтобы консультант или мастер могли слышать вас",
            icon = R.drawable.device_microphone
        )
        requestRationaleFragment.show(childFragmentManager, requestRationaleFragment.TAG)
    }

    private fun showRequestRecordVideoRationaleDialog(){
        val requestRationaleFragment = RequestRationaleFragment.newInstance(
            requestCode = REQUEST_CODE_MIC_AND_CAMERA,
            description = "Разрешите доступ, чтобы консультант или мастер могли видеть вас",
            icon = R.drawable.device_microphone
        )
        requestRationaleFragment.show(childFragmentManager, requestRationaleFragment.TAG)
    }

    override fun onRequestRationaleDismiss(requestCode: Int?) {
        when (requestCode){
            REQUEST_CODE_MIC -> {
                if (hasPermissions(Manifest.permission.RECORD_AUDIO)){
                    viewModel.onEnableMicClick(true)
                    binding.micOffImageView.isActivated = false
                }
            }
            REQUEST_CODE_MIC_AND_CAMERA -> {
                if (hasPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)){
                    viewModel.onEnableMicClick(true)
                    viewModel.onEnableCameraClick(true)

                    binding.cameraOffImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.button_call_camera_selector))
                    binding.cameraOffImageView.isActivated = false
                    binding.micOffImageView.isActivated = false
                }
            }
        }
    }

}