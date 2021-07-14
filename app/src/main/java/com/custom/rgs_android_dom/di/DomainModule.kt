package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.domain.DemoInteractor
import org.koin.dsl.module

val domainModule = module {

    factory { DemoInteractor(demoRepository = get()) }

}