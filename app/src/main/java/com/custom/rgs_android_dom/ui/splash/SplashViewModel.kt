package com.custom.rgs_android_dom.ui.splash

import com.custom.rgs_android_dom.ui.base.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class SplashViewModel : BaseViewModel(), KoinComponent {
    init {
        GlobalScope.launch {
            delay(4000)
            loadingStateObserver.postValue(LoadingState.CONTENT)
        }
    }
}
