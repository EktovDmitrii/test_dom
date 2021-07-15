package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.data.repositories.DemoRepository
import org.koin.dsl.module

val dataModule = module {

    single { DemoRepository() }

}