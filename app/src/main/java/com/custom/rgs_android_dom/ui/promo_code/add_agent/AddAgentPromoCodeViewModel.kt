package com.custom.rgs_android_dom.ui.promo_code.add_agent

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.data.network.toMSDErrorModel
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.exceptions.SpecificValidateClientExceptions
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.promo_code.dialogs.PromoCodeDialogsFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class AddAgentPromoCodeViewModel(
    private val promoCodeId: String,
    private val clientInteractor: ClientInteractor
) : BaseViewModel() {

    val promoCodeIdString = promoCodeId

    companion object {
        private const val ERR_AGENT_NOT_FOUND = "INS-093"
    }

    private val isSaveTextViewEnabledController = MutableLiveData<Boolean>()
    val isSaveTextViewEnabledObserver: LiveData<Boolean> = isSaveTextViewEnabledController

    private val validateExceptionController = MutableLiveData<SpecificValidateClientExceptions>()
    val validateExceptionObserver: LiveData<SpecificValidateClientExceptions> = validateExceptionController

    init {
        clientInteractor.validateSubject.hide()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    isSaveTextViewEnabledController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onAgentCodeChanged(agentCode: String) {
        clientInteractor.onEditAgentCodeChanged(agentCode)
    }

    fun onAgentPhoneChanged(agentPhone: String, isMaskFilled: Boolean) {
        clientInteractor.onEditAgentPhoneChanged(agentPhone, isMaskFilled)
    }

    fun onSaveClick(parentFragmentManager: FragmentManager, isWasCodeAgent: Boolean) {
        clientInteractor.updateAgent()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onComplete = {
                    val dialog =
                        PromoCodeDialogsFragment.newInstance(promoCodeIdString, isWasCodeAgent)
                    dialog.show(parentFragmentManager, dialog.TAG)
                    close()
                },
                onError = {
                    when (it) {
                        is SpecificValidateClientExceptions -> {
                            validateExceptionController.value = it
                            loadingStateController.value = LoadingState.CONTENT
                        }
                        else -> {
                            // TODO Temporary solution
                            val msdErrorModel = it.toMSDErrorModel()
                            if (msdErrorModel != null && msdErrorModel.code == ERR_AGENT_NOT_FOUND) {
                                networkErrorController.value =
                                    TranslationInteractor.getTranslation("app.agent_info_edit.agent_code_error_label")
                            } else {
                                handleNetworkException(it)
                            }
                            loadingStateController.value = LoadingState.ERROR
                        }
                    }
                }
            ).addTo(dataCompositeDisposable)
    }
}
