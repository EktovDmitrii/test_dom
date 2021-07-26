package com.custom.rgs_android_dom.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> Fragment.subscribe(liveData: (LiveData<T>)?, onNext: (t: T) -> Unit) {
    liveData?.observe(viewLifecycleOwner, Observer {
        if (it != null) {
            onNext(it)
        }
    })
}

fun <T> FragmentActivity.subscribe(liveData: (LiveData<T>)?, onNext: (t: T) -> Unit) {
    liveData?.observe(this, Observer {
        if (it != null) {
            onNext(it)
        }
    })
}