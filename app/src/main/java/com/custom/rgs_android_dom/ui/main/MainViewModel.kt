package com.custom.rgs_android_dom.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.MainCatalogFragment
import com.custom.rgs_android_dom.ui.catalog.search.CatalogSearchFragment
import com.custom.rgs_android_dom.ui.client.ClientFragment
import com.custom.rgs_android_dom.ui.navigation.ADD_PROPERTY
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.property.add.select_address.SelectAddressFragment
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val registrationInteractor: RegistrationInteractor,
    private val propertyInteractor: PropertyInteractor
) : BaseViewModel() {

    private val registrationController = MutableLiveData(false)
    val registrationObserver: LiveData<Boolean> = registrationController

    private val propertyAvailabilityController = MutableLiveData(false)
    val propertyAvailabilityObserver: LiveData<Boolean> = propertyAvailabilityController

    private val popularServicesController = MutableLiveData<List<ProductShortModel>>()
    val popularServicesObserver: LiveData<List<ProductShortModel>> = popularServicesController

    init {
        registrationController.value = registrationInteractor.isAuthorized().let {
            if (it) getPropertyAvailability()
            return@let it
        }

        registrationInteractor.getLoginSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    registrationController.value = true
                    getPropertyAvailability()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        registrationInteractor.getLogoutSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    registrationController.value = false
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        popularServicesController.value = listOf(
            ProductShortModel(
                id = "",
                type = "",
                title = "Установка раковины",
                code = "",
                versionId = "",
                name = "",
                price = 150,
                tags = emptyList(),
                icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg"
            ),
            ProductShortModel(
                id = "",
                type = "",
                title = "Установка раковины",
                code = "",
                versionId = "",
                name = "",
                price = 150,
                tags = emptyList(),
                icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg"
            ),
            ProductShortModel(
                id = "",
                type = "",
                title = "Установка раковины",
                code = "",
                versionId = "",
                name = "",
                price = 150,
                tags = emptyList(),
                icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg"
            ),
            ProductShortModel(
                id = "",
                type = "",
                title = "Установка раковины",
                code = "",
                versionId = "",
                name = "",
                price = 150,
                tags = emptyList(),
                icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg"
            ),
            ProductShortModel(
                id = "",
                type = "",
                title = "Установка раковины",
                code = "",
                versionId = "",
                name = "",
                price = 150,
                tags = emptyList(),
                icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg"
            ),
            ProductShortModel(
                id = "",
                type = "",
                title = "Установка раковины",
                code = "",
                versionId = "",
                name = "",
                price = 150,
                tags = emptyList(),
                icon = "https://ddom.moi-service.ru/api/store/node:okr8bdicefdg78r1z8euw7dwny.jpeg"
            )
        ).take(6)
    }

    private fun getPropertyAvailability() {
        propertyInteractor.getAllProperty()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    propertyAvailabilityController.value = it.isNotEmpty()
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)
    }

    fun onLoginClick() {
        ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
    }

    fun onProfileClick() {
        ScreenManager.showBottomScreen(ClientFragment())
    }

    fun onNoPropertyClick() {
        ScreenManager.showScreenScope(SelectAddressFragment.newInstance(), ADD_PROPERTY)
    }

    fun onPropertyAvailableClick() {
        ScreenManager.showBottomScreen(ClientFragment())
    }

    fun onTagClick(tag: String) {
        val catalogSearchFragment = CatalogSearchFragment.newInstance(tag)
        ScreenManager.showScreen(catalogSearchFragment)
    }

    fun onSearchClick() {
        val catalogSearchFragment = CatalogSearchFragment.newInstance()
        ScreenManager.showScreen(catalogSearchFragment)
    }

    fun navigateCatalog() {
        ScreenManager.showBottomScreen(MainCatalogFragment())
    }
}