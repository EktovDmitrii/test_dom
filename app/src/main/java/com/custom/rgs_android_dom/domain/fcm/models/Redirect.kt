package com.custom.rgs_android_dom.domain.fcm.models

enum class Redirect(screen: String){
    CHAT("##chat"),
    PROFILE("##profile"),
    ORDER("##order"),
    CALL("##call"),
    NEW_OBJECT("##new_object"),
    FEEDBACK("##feedback"),
    UNKNOWN("unknown")
}