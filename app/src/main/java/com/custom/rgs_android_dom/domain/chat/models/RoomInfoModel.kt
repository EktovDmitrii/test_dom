package com.custom.rgs_android_dom.domain.chat.models

import io.livekit.android.room.Room
import io.livekit.android.room.track.AudioTrack
import io.livekit.android.room.track.LocalAudioTrack
import io.livekit.android.room.track.VideoTrack

data class RoomInfoModel(
    var callType: CallType? = null,
    var room: Room? = null,
    var primaryVideoTrack: VideoTrack? = null,
    var secondaryVideoTrack: VideoTrack? = null,
    var cameraEnabled: Boolean,
    var micEnabled: Boolean,
    var videoTracksInversed: Boolean = false,
    var localAudioTrack: LocalAudioTrack? = null,
    var localVideoTrack: VideoTrack? = null
)