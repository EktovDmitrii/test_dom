package com.custom.rgs_android_dom.ui.chats.chat.call

import android.Manifest
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentCallBinding
import com.custom.rgs_android_dom.domain.chat.models.CallType
import com.custom.rgs_android_dom.domain.chat.models.ChannelMemberModel
import com.custom.rgs_android_dom.domain.chat.models.MediaOutputType
import com.custom.rgs_android_dom.domain.chat.models.RoomInfoModel
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.chats.chat.call.media_output_chooser.MediaOutputChooserFragment
import com.custom.rgs_android_dom.ui.rationale.RequestRationaleFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.activity.clearLightStatusBar
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import org.webrtc.RendererCommon

class CallFragment : BaseFragment<CallViewModel, FragmentCallBinding>(R.layout.fragment_call), RequestRationaleFragment.OnRequestRationaleDismissListener {

    companion object {
        private const val ARC_CHANNEL_ID = "ARG_CHANNEL_ID"
        private const val ARG_CALL_TYPE = "ARG_CALL_TYPE"
        private const val ARG_CONSULTANT = "ARG_CHANNEL_MEMBER"

        private const val REQUEST_CODE_MIC = 1
        private const val REQUEST_CODE_MIC_AND_CAMERA = 2

        fun newInstance(channelId: String, callType: CallType, consultant: ChannelMemberModel?): CallFragment {
            return CallFragment().args {
                putString(ARC_CHANNEL_ID, channelId)
                putSerializable(ARG_CALL_TYPE, callType)
                if (consultant != null){
                    putSerializable(ARG_CONSULTANT, consultant)
                }
            }
        }
    }

    private val smallVideoWidth by lazy {
        resources.getDimensionPixelSize(R.dimen.chat_small_video_width)
    }
    private val smallVideoHeight by lazy {
        resources.getDimensionPixelSize(R.dimen.chat_small_video_height)
    }
    private val smallVideoMargin by lazy {
        resources.getDimensionPixelSize(R.dimen.material_margin_normal)
    }

    private val requestMicAndCameraPermissionsAction =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->

            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                viewModel.onVideoCallPermissionsGranted(false)
                showRequestRecordVideoRationaleDialog()
            } else if (permissionsResult[Manifest.permission.CAMERA] == true){
                viewModel.onVideoCallPermissionsGranted(true)
            }
            binding.waitingCameraPermissionFrameLayout.gone()
            binding.cameraOnOffImageView.visible()

            if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)){
                viewModel.onAudioCallPermissionsGranted(false)
                showRequestRecordAudioRationaleDialog()
            } else if (permissionsResult[Manifest.permission.RECORD_AUDIO] == true
                && permissionsResult[Manifest.permission.MODIFY_AUDIO_SETTINGS] == true){
                viewModel.onAudioCallPermissionsGranted(true)
            }
        }

    private val requestCameraPermissionAction =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionResult ->

            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                viewModel.onVideoCallPermissionsGranted(false)
                showRequestRecordVideoRationaleDialog()
            } else if (permissionResult == true){
                viewModel.onVideoCallPermissionsGranted(true)
            }
            binding.waitingCameraPermissionFrameLayout.gone()
            binding.cameraOnOffImageView.visible()
        }

    private val requestMicPermissionsAction =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
            if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)){
                viewModel.onAudioCallPermissionsGranted(false)
                showRequestRecordAudioRationaleDialog()
            } else if (permissionsResult[Manifest.permission.RECORD_AUDIO] == true
                && permissionsResult[Manifest.permission.MODIFY_AUDIO_SETTINGS] == true){
                viewModel.onAudioCallPermissionsGranted(true)
            }
        }

    private var renderersInited = false

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(ARC_CHANNEL_ID),
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

        binding.micOnOffImageView.setOnDebouncedClickListener {
            if (hasPermissions(Manifest.permission.RECORD_AUDIO)){
                viewModel.onEnableMicClick(!binding.micOnOffImageView.isActivated)
            } else {
                showRequestRecordAudioRationaleDialog()
            }
        }

        binding.cameraOnOffImageView.setOnDebouncedClickListener {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                showRequestRecordVideoRationaleDialog()
            } else {
                if (hasPermissions(Manifest.permission.CAMERA)){
                    viewModel.onEnableCameraClick(!binding.cameraOnOffImageView.isActivated)
                } else {
                    requestCameraPermissionAction.launch(Manifest.permission.CAMERA)
                }
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
            viewModel.onVideoTrackSwitchClick()
        }

        binding.switchSurfacesMyImageView.setOnDebouncedClickListener {
            viewModel.onVideoTrackSwitchClick()
        }

        binding.minimizeImageView.setOnDebouncedClickListener {
            viewModel.onMinimizeClick()
        }

        binding.mediaOutputImageView.setOnDebouncedClickListener {
            val mediaOutputChooserFragment = MediaOutputChooserFragment()
            mediaOutputChooserFragment.show(childFragmentManager, mediaOutputChooserFragment.TAG)
        }

        subscribe(viewModel.callTypeObserver) {
            when (it) {
                CallType.AUDIO_CALL -> {
                    binding.switchCameraImageView.isEnabled = false
                    requestMicPermissionsAction.launch(
                        arrayOf(
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS
                        )
                    )
                }
                CallType.VIDEO_CALL -> {
                    binding.waitingConsultantVideoFrameLayout.visible()
                    binding.waitingCameraPermissionFrameLayout.visible()
                    binding.cameraOnOffImageView.gone()
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
                    renderersInited = true
                }
            }
            binding.switchCameraImageView.isEnabled = roomInfo.cameraEnabled
            if (roomInfo.cameraEnabled) binding.switchCameraImageView.isActivated = roomInfo.frontCameraEnabled

            binding.cameraOnOffImageView.isActivated = roomInfo.cameraEnabled
            binding.micOnOffImageView.isActivated = roomInfo.micEnabled

            binding.consultantSurfaceContainer.visibleIf(roomInfo.consultantVideoTrack != null)

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
                setMyVideoFullScreen(roomInfo)
            } else {
                binding.consultantSurfaceContainer.z = 0F
                binding.mySurfaceContainer.z = 1f
                setConsultantVideoFullScreen(roomInfo)
            }

            if (roomInfo.myVideoTrack != null && roomInfo.cameraEnabled){
                binding.mySurfaceContainer.visible()
                roomInfo.myVideoTrack?.addRenderer(binding.mySurfaceRenderer)
            } else {
                expandConsultantVideoScreen()
            }

        }

        subscribe(viewModel.consultantObserver){consultant->
            binding.consultantNameTextView.text = "${consultant.lastName} ${consultant.firstName}, Онлайн мастер"

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
            binding.titleTextView.text = "Онлайн Мастер"
            binding.subtitleTextView.text = it
        }

        subscribe(viewModel.mediaOutputObserver){
            when (it){
                MediaOutputType.PHONE,
                MediaOutputType.WIRED_HEADPHONE -> {
                    binding.mediaOutputImageView.setImageResource(R.drawable.ic_phone_call_24px)
                }
                MediaOutputType.SPEAKERPHONE -> {
                    binding.mediaOutputImageView.setImageResource(R.drawable.ic_speaker_24px)
                }
                MediaOutputType.BLUETOOTH -> {
                    binding.mediaOutputImageView.setImageResource(R.drawable.ic_bluetooth_24px)
                }
            }
        }

    }

    private fun setConsultantVideoFullScreen(roomInfoModel: RoomInfoModel) {

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
            width = smallVideoWidth
            height = smallVideoHeight
            setMargins(0,0, smallVideoMargin, smallVideoMargin)
            gravity = Gravity.BOTTOM or Gravity.END
            binding.mySurfaceContainer.layoutParams = this
        }

        binding.mySurfaceContainer.setOnDebouncedClickListener { }
    }

    private fun expandConsultantVideoScreen(){

        binding.mySurfaceContainer.gone()
        binding.switchSurfacesMyImageView.gone()
        binding.consultantSurfaceContainer.visible()
        binding.switchSurfacesConsultantImageView.gone()

        with(binding.consultantSurfaceContainer.layoutParams as FrameLayout.LayoutParams){
            width = MATCH_PARENT
            height = MATCH_PARENT
            setMargins(0,0,0,0)
            gravity = Gravity.CENTER
            binding.consultantSurfaceContainer.layoutParams = this
        }
    }

    private fun setMyVideoFullScreen(roomInfoModel: RoomInfoModel) {

        binding.mySurfaceContainer.visible()
        binding.switchSurfacesMyImageView.gone()

        with(binding.mySurfaceContainer.layoutParams as FrameLayout.LayoutParams){
            width = MATCH_PARENT
            height = MATCH_PARENT
            setMargins(0,0,0,0)
            gravity = Gravity.CENTER
            binding.mySurfaceContainer.layoutParams = this
        }

        if (roomInfoModel.consultantVideoTrack != null){
            binding.consultantSurfaceContainer.visible()
            binding.switchSurfacesConsultantImageView.visible()

            with(binding.consultantSurfaceContainer.layoutParams as FrameLayout.LayoutParams){
                width = smallVideoWidth
                height = smallVideoHeight
                setMargins(0,0, smallVideoMargin, smallVideoMargin)
                gravity = Gravity.BOTTOM or Gravity.END
                binding.consultantSurfaceContainer.layoutParams = this
            }
        } else {
            binding.consultantSurfaceContainer.gone()
            binding.mySurfaceContainer.setOnDebouncedClickListener {
                setConsultantVideoFullScreen(roomInfoModel)
            }
        }

    }

    override fun onDestroyView() {
        binding.consultantSurfaceRenderer.release()
        binding.mySurfaceRenderer.release()
        super.onDestroyView()
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
                    binding.micOnOffImageView.isActivated = false
                }
            }
            REQUEST_CODE_MIC_AND_CAMERA -> {
                if (hasPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)){
                    viewModel.onEnableMicClick(true)
                    viewModel.onEnableCameraClick(true)
                }
            }
        }
    }

}