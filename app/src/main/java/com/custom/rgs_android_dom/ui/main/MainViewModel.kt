package com.custom.rgs_android_dom.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.main.CommentModel
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.about_app.AboutAppFragment
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.ProductFragment
import com.custom.rgs_android_dom.ui.catalog.MainCatalogFragment
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductFragment
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
    private val propertyInteractor: PropertyInteractor,
    private val catalogInteractor: CatalogInteractor
) : BaseViewModel() {

    private val registrationController = MutableLiveData(false)
    val registrationObserver: LiveData<Boolean> = registrationController

    private val propertyAvailabilityController = MutableLiveData(false)
    val propertyAvailabilityObserver: LiveData<Boolean> = propertyAvailabilityController

    private val rateCommentsController = MutableLiveData<List<CommentModel>>()
    val rateCommentsObserver: LiveData<List<CommentModel>> = rateCommentsController

    private val popularServicesController = MutableLiveData<List<ProductShortModel>>()
    val popularServicesObserver: LiveData<List<ProductShortModel>> = popularServicesController

    private var openAboutAppScreenAfterLogin = false

    private val popularProductsController = MutableLiveData<List<ProductShortModel>>()
    val popularProductsObserver: LiveData<List<ProductShortModel>> = popularProductsController

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

                    if (openAboutAppScreenAfterLogin){
                        openAboutAppScreenAfterLogin = false
                        ScreenManager.showScreen(AboutAppFragment())
                    }
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

        propertyInteractor.getPropertyAddedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    getPropertyAvailability()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        catalogInteractor.getPopularProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    popularProductsController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        rateCommentsController.value = listOf(
            CommentModel(
                name = "Сергей",
                rate = 5,
                comment = "Все очень грамотно, быстро, все объяснили по заявке."
            ),
            CommentModel(
                name = "Ханума",
                rate = 5,
                comment = "Все быстро организовали, не пришлось долго ждать, мастер вежливый и культурный"
            ),
            CommentModel(
                name = "Ирина",
                rate = 5,
                comment = "Вовремя приехали, быстро все сделали, мастер очень понравился, готова рекомендовать"
            ),
            CommentModel(
                name = "Татьяна",
                rate = 5,
                comment = "Очень довольна, все было своевременно, мастер был всегда на связи"
            ),
            CommentModel(
                name = "Серафима",
                rate = 4,
                comment = "Мастер - хороший, толковый парень, на все руки мастер"
            ),
            CommentModel(
                name = "Анастасия",
                rate = 5,
                comment = "Супер все быстро организовано, качество работ на высоком уровне, все понравилось"
            )
        )
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

    fun getPopularProducts() {
        catalogInteractor.getPopularServices()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    popularServicesController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onLoginClick() {
        ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
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

    fun onSOSClick() {
        if (registrationController.value == false) {
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        } else {
        //todo go to problem solving screen
        }

    }

    fun onPoliciesClick() {
        if (registrationController.value == false) {
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        } else {
            //todo go to PoliciesScreen
        }
    }

    fun onProductsClick() {
        if (registrationController.value == false) {
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        } else {
            ScreenManager.showBottomScreen(MainCatalogFragment.newInstance(MainCatalogFragment.TAB_MY_PRODUCTS))
        }
    }

    fun onServiceClick(serviceModel: ProductShortModel) {
        ScreenManager.showBottomScreen(SingleProductFragment.newInstance(serviceModel.id))
    }

    fun onAllCatalogClick() {
        ScreenManager.showBottomScreen(MainCatalogFragment.newInstance())
    }

    fun onAboutServiceClick(){
        if (registrationInteractor.isAuthorized()){
            ScreenManager.showScreen(AboutAppFragment())
        } else {
            openAboutAppScreenAfterLogin = true
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }
    }
    fun onShowAllPopularProductsClick() {
        ScreenManager.showBottomScreen(MainCatalogFragment.newInstance())
    }

    fun onPopularProductClick(productId: String) {
        ScreenManager.showBottomScreen(ProductFragment.newInstance(productId))
    }

}