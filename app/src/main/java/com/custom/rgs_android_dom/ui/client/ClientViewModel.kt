package com.custom.rgs_android_dom.ui.client

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.view_states.ClientShortViewState
import com.custom.rgs_android_dom.domain.property.models.AddPropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.about_app.AboutAppFragment
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.client.agent.AgentFragment
import com.custom.rgs_android_dom.ui.client.personal_data.PersonalDataFragment
import com.custom.rgs_android_dom.ui.navigation.ADD_PROPERTY
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.property.add.select_address.SelectAddressFragment
import com.custom.rgs_android_dom.ui.property.add.select_type.SelectPropertyTypeFragment
import com.custom.rgs_android_dom.ui.property.info.PropertyInfoFragment
import com.custom.rgs_android_dom.ui.screen_stub.ScreenStubFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ClientViewModel(
    private val clientInteractor: ClientInteractor,
    private val registrationInteractor: RegistrationInteractor,
    private val propertyInteractor: PropertyInteractor
) : BaseViewModel() {

    private val clientShortViewStateController = MutableLiveData<ClientShortViewState>()

    private val propertyItemsController = MutableLiveData<List<Any>>()
    val propertyItemsObserver: LiveData<List<Any>> = propertyItemsController

    val clientShortViewStateObserver: LiveData<ClientShortViewState> =
        clientShortViewStateController

    private val swipeRefreshingController = MutableLiveData<Boolean>()
    val swipeRefreshingObserver: LiveData<Boolean> = swipeRefreshingController

    init {
        clientInteractor.subscribeClientUpdateSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    clientShortViewStateController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        clientInteractor.getClient()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    clientShortViewStateController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        propertyInteractor.getPropertyAddedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    loadProperty()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        loadProperty()
    }

    fun onLogoutClick() {
        registrationInteractor.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    closeController.value = Unit
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onPersonalDataClick(){
        ScreenManager.showScreen(PersonalDataFragment())
    }

    fun onAgentInfoClick(){
        ScreenManager.showScreen(AgentFragment())
    }

    fun onAboutAppClick() {
        ScreenManager.showScreen(AboutAppFragment())
    }

    fun onPropertyItemClick(model: PropertyItemModel) {
        ScreenManager.showBottomScreen(PropertyInfoFragment.newInstance(model.id))
    }

    fun onNotCreatedScreenClick(){
        ScreenManager.showScreen(ScreenStubFragment())
    }

    fun onAddPropertyClick(){
        propertyInteractor.getAllProperty()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    ScreenManager.showScreenScope(SelectAddressFragment.newInstance(it.size), ADD_PROPERTY)
                    //closeController.value = Unit
                    //ScreenManager.showScreenScope(SelectPropertyTypeFragment.newInstance(it.size), ADD_PROPERTY)
                },
                onError = {
                    logException(this, it)
                    networkErrorController.value = "Не удалось загрузить список собственности"
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onRefresh(){
        swipeRefreshingController.value = true
        loadProperty()
    }

    private fun loadProperty(){
        propertyInteractor.getAllProperty()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    // TODO Maybe we need to re-factor this stuff
                    val propertyItems = arrayListOf<Any>()
                    propertyItems.addAll(it)
                    propertyItems.add(AddPropertyItemModel())
                    propertyItemsController.value = propertyItems

                    swipeRefreshingController.value = false
                },
                onError = {
                    val propertyItems = arrayListOf<Any>()
                    propertyItems.add(AddPropertyItemModel())
                    propertyItemsController.value = propertyItems

                    logException(this, it)
                    networkErrorController.value = "Не удалось загрузить список собственности"

                    swipeRefreshingController.value = false
                }
            ).addTo(dataCompositeDisposable)
    }

}