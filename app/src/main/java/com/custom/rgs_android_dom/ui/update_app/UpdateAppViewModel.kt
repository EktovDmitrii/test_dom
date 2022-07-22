package com.custom.rgs_android_dom.ui.update_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.root.RootFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class UpdateAppViewModel(private val clientInteractor: ClientInteractor) : BaseViewModel() {

    private val startUpdateController = MutableLiveData<Unit>()
    val startUpdateObserver: LiveData<Unit> = startUpdateController

    fun onUpdateClick() {
        startUpdateController.value = Unit
    }

    fun onNotNowClick() {
        close()
    }

    fun checkAppUpdates() {
        clientInteractor.getActualAppVersions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {versions ->
                    if (versions.clientCurrentVersion >= versions.appAndroidCurrentVersion && versions.clientCurrentVersion >= versions.appAndroidCriticalVersion) {
                       ScreenManager.resetStackAndShowScreen(RootFragment())
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

}