package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.ui.managers.ConnectionManager
import com.custom.rgs_android_dom.ui.managers.MediaOutputManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val otherModule = module {

    single { MediaOutputManager(context = androidContext()) }
    single { ConnectionManager(context = androidContext()) }

}