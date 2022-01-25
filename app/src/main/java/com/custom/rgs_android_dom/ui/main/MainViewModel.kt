package com.custom.rgs_android_dom.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.main.CommentModel
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.about_app.AboutAppFragment
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.ProductFragment
import com.custom.rgs_android_dom.ui.catalog.MainCatalogFragment
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductFragment
import com.custom.rgs_android_dom.ui.catalog.search.CatalogSearchFragment
import com.custom.rgs_android_dom.ui.catalog.subcategories.CatalogSubcategoriesFragment
import com.custom.rgs_android_dom.ui.client.ClientFragment
import com.custom.rgs_android_dom.ui.navigation.ADD_PROPERTY
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.property.add.select_address.SelectAddressFragment
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.ProgressTransformer
import com.custom.rgs_android_dom.ui.sos.SOSFragment
import com.custom.rgs_android_dom.utils.isInternetConnected
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val registrationInteractor: RegistrationInteractor,
    private val propertyInteractor: PropertyInteractor,
    private val catalogInteractor: CatalogInteractor,
    private val context: Context
) : BaseViewModel() {

    private val registrationController = MutableLiveData(false)
    val registrationObserver: LiveData<Boolean> = registrationController

    private val propertyAvailabilityController = MutableLiveData(false)
    val propertyAvailabilityObserver: LiveData<Boolean> = propertyAvailabilityController

    private val rateCommentsController = MutableLiveData<List<CommentModel>>()
    val rateCommentsObserver: LiveData<List<CommentModel>> = rateCommentsController

    private val popularServicesController = MutableLiveData<List<ProductShortModel>>()
    val popularServicesObserver: LiveData<List<ProductShortModel>> = popularServicesController

    private val popularCategoriesController = MutableLiveData<List<CatalogCategoryModel>>()
    val popularCategoriesObserver: LiveData<List<CatalogCategoryModel>> = popularCategoriesController

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

        loadContent()
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

    fun loadContent() {
        if (context.isInternetConnected()) {
            Single.zip(
                catalogInteractor.getPopularServices(),
                catalogInteractor.getPopularProducts(),
                catalogInteractor.getPopularCategories(),
                catalogInteractor.getComments()
            ) { services, products, categories, comments ->
                popularServicesController.postValue(services)
                popularProductsController.postValue(products)
                popularCategoriesController.postValue(categories)
                rateCommentsController.postValue(comments)
            }
                .compose(
                    ProgressTransformer(
                        onLoading = {
                            loadingStateController.postValue(LoadingState.LOADING)
                        },
                        onError = {
                            logException(this, it)
                            loadingStateController.value = LoadingState.ERROR
                        },
                        onLoaded = {
                            loadingStateController.value = LoadingState.CONTENT
                        }
                    )
                )
                .subscribe()
                .addTo(dataCompositeDisposable)
        } else {
            loadingStateController.value = LoadingState.ERROR
        }
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
        ScreenManager.showScreen(SOSFragment())
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

    fun onCategoryClick(category: CatalogCategoryModel) {
        val catalogSubcategoriesFragment = CatalogSubcategoriesFragment.newInstance(category)
        ScreenManager.showBottomScreen(catalogSubcategoriesFragment)
    }

    fun onShowAllPopularCategoriesClick() {
        ScreenManager.showBottomScreen(MainCatalogFragment.newInstance())
    }

    fun onShowAllPopularProductsClick() {
        ScreenManager.showBottomScreen(MainCatalogFragment.newInstance())
    }

    fun onPopularProductClick(productId: String) {
        ScreenManager.showBottomScreen(ProductFragment.newInstance(productId))
    }

}