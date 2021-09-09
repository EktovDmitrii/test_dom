package com.custom.rgs_android_dom.data

import com.jakewharton.rxrelay2.PublishRelay

object GlobalSubjects {

    val authFailedSubject = PublishRelay.create<Unit>()
}