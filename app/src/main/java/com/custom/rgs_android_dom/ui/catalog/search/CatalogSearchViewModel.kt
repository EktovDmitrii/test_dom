package com.custom.rgs_android_dom.ui.catalog.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.ProductShortModel
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.ProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.ProductLauncher
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.single.SingleProductLauncher
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.navigation.TargetScreen
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.logException
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CatalogSearchViewModel(
    tag: String?,
    private val chatInteractor: ChatInteractor,
    private val catalogInteractor: CatalogInteractor,
    private val registrationInteractor: RegistrationInteractor,
    private val clientInteractor: ClientInteractor
) : BaseViewModel() {

    companion object {
        private const val SEARCH_DELAY = 700L
    }

    private val tagController = MutableLiveData<String>()
    val tagObserver: LiveData<String> = tagController

    private val popularProductVisibleController = MutableLiveData<Boolean>()
    val popularProductsVisibleObserver: LiveData<Boolean> = popularProductVisibleController

    private val searchResultsVisibleController = MutableLiveData<Boolean>()
    val searchResultsVisibleObserver: LiveData<Boolean> = searchResultsVisibleController

    private val noSearchResultsVisibleController = MutableLiveData<Boolean>()
    val noSearchResultsVisibleObserver: LiveData<Boolean> = noSearchResultsVisibleController

    private val popularProductsController = MutableLiveData<List<ProductShortModel>>()
    val popularProductsObserver: LiveData<List<ProductShortModel>> = popularProductsController

    private val searchResultsController = MutableLiveData<List<ProductShortModel>>()
    val searchResultsObserver: LiveData<List<ProductShortModel>> = searchResultsController

    private val searchSubject = PublishRelay.create<String>()
    private var popularProducts = listOf<ProductShortModel>()
    private var screenToOpenOnLogin = TargetScreen.UNSPECIFIED

    init {
        tag?.let { tag->
            tagController.value = tag
        }

        catalogInteractor.getPopularSearchProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    popularProducts = it
                    popularProductsController.value = popularProducts

                    if (tag == null){
                        popularProductVisibleController.value = popularProducts.isNotEmpty()
                    }

                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        registrationInteractor.getAuthFlowEndedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onNext = {
                    when (screenToOpenOnLogin) {
                        TargetScreen.CHAT -> { ScreenManager.showBottomScreen(ChatFragment.newInstance(chatInteractor.getMasterOnlineCase())) }
                    }
                    screenToOpenOnLogin = TargetScreen.UNSPECIFIED
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        subscribeSearchSubject()
    }


    fun onSearchQueryChanged(query: String){
        noSearchResultsVisibleController.value = false
        searchSubject.accept(query)
    }

    fun onClearClick(){
        noSearchResultsVisibleController.value = false

        popularProductVisibleController.value = popularProducts.isNotEmpty()
        searchResultsVisibleController.value = false
    }

    fun onProductClick(product: ProductShortModel){
        if (product.defaultProduct){
            ScreenManager.showBottomScreen(SingleProductFragment.newInstance(SingleProductLauncher( product.id)))
        } else {
            ScreenManager.showBottomScreen(ProductFragment.newInstance(ProductLauncher(product.id)))
        }
    }

    fun onCancelClick(){
        closeController.value = Unit
    }

    fun onOpenChatClick(){
        if (registrationInteractor.isAuthorized()){
            ScreenManager.showBottomScreen(ChatFragment.newInstance(chatInteractor.getMasterOnlineCase()))
        } else {
            screenToOpenOnLogin = TargetScreen.CHAT
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }
    }

    private fun subscribeSearchSubject() {
        searchSubject.hide()
            .debounce(SEARCH_DELAY, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {query->
                    if (query.isNotEmpty()){
                        popularProductVisibleController.value = false
                        findProducts(query)
                    } else {
                        popularProductVisibleController.value = popularProducts.isNotEmpty()
                        searchResultsVisibleController.value = false
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    private fun findProducts(query: String){
        catalogInteractor.findProducts(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onSuccess = {
                    if (it.isNotEmpty()){
                        searchResultsController.value = it
                        searchResultsVisibleController.value = true
                        noSearchResultsVisibleController.value = false
                    } else {
                        searchResultsVisibleController.value = false
                        noSearchResultsVisibleController.value = true
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

}