package com.custom.rgs_android_dom.domain.chat.models

import io.livekit.android.room.Room
import io.livekit.android.room.track.VideoTrack

data class RoomInfoModel(
    val callType: CallType? = null,
    val room: Room? = null,
    val myVideoTrack: VideoTrack? = null,
    val opponentVideoTrack: VideoTrack? = null
)