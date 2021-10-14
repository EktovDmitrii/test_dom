package com.custom.rgs_android_dom.ui.address.suggestions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.address.AddressInteractor
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class AddressSuggestionsViewModel(private val addressInteractor: AddressInteractor) : BaseViewModel() {

    companion object {
        private const val INPUT_DELAY = 500L
    }

    private val addressItemsController = MutableLiveData<List<AddressItemModel>>()
    val addressItemsObserver: LiveData<List<AddressItemModel>> = addressItemsController

    private val emptyQueryController = MutableLiveData<Unit>()
    val emptyQueryObserver: LiveData<Unit> = emptyQueryController

    private val searchSubject = PublishSubject.create<String>()

    init {
        setupSearch()
    }

    fun onQueryChanged(query: String){
        searchSubject.onNext(query)
    }

    fun onAddressItemClick(addressItem: AddressItemModel){
        addressInteractor.onAddressSelected(addressItem)
        closeController.value = Unit
    }

    private fun setupSearch() {
        searchSubject
            .debounce(INPUT_DELAY, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (it.isNotEmpty()){
                        search(it)
                    } else{
                        emptyQueryController.value = Unit
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    private fun search(query: String){
        addressInteractor.getAddressSuggestions(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    addressItemsController.value = it
                },
                onError = {
                    logException(this, it)
                    addressItemsController.value = listOf()
                }
            ).addTo(dataCompositeDisposable)
    }
}