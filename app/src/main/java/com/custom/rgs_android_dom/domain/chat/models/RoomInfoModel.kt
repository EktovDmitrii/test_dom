package com.custom.rgs_android_dom.domain.chat.models

import io.livekit.android.room.Room
import io.livekit.android.room.track.LocalAudioTrack
import io.livekit.android.room.track.VideoTrack

data class RoomInfoModel(
    var callType: CallType? = null,
    var room: Room? = null,
    var consultantVideoTrack: VideoTrack? = null,
    var myVideoTrack: VideoTrack? = null,
    var myAudioTrack: LocalAudioTrack? = null,
    var cameraEnabled: Boolean,
    var frontCameraEnabled: Boolean = true,
    var micEnabled: Boolean,
    var videoTracksSwitched: Boolean = false,
)