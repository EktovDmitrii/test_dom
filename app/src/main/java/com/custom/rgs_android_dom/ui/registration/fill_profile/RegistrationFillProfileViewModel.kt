package com.custom.rgs_android_dom.ui.registration.fill_profile

import android.text.Html
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.lang.StringBuilder

class RegistrationFillProfileViewModel(private val registrationInteractor: RegistrationInteractor) : BaseViewModel() {

    private val isAgentInfoLinearLayoutVisibleControler = MutableLiveData<Boolean>()
    private val knowAgentCodeTextController = MutableLiveData<String>()

    val isAgentInfoLinearLayoutVisibleObserver: LiveData<Boolean> = isAgentInfoLinearLayoutVisibleControler
    val knowAgentCodeTextObserver: LiveData<String> = knowAgentCodeTextController

    init {

    }

    fun onKnowAgentCodeClick(){
        var isAgentInfoLinearLayoutVisible = isAgentInfoLinearLayoutVisibleControler.value ?: false
        isAgentInfoLinearLayoutVisible = !isAgentInfoLinearLayoutVisible

        val knowAgentCodeText = if (isAgentInfoLinearLayoutVisible) "Свернуть информацию об агенте"
            else "Знаю код агента"

        isAgentInfoLinearLayoutVisibleControler.value = isAgentInfoLinearLayoutVisible
        knowAgentCodeTextController.value = knowAgentCodeText
    }

    fun onSkipClick(){
        closeController.value = Unit
    }

    fun onCloseClick(){
        closeController.value = Unit
    }
}