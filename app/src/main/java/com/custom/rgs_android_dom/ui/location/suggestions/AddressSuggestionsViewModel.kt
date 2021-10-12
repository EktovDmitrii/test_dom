package com.custom.rgs_android_dom.ui.location.suggestions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.location.LocationInteractor
import com.custom.rgs_android_dom.domain.location.models.AddressItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class AddressSuggestionsViewModel(private val locationInteractor: LocationInteractor) : BaseViewModel() {

    companion object {
        private const val INPUT_DELAY = 500L
    }

    private val addressItemsController = MutableLiveData<List<AddressItemModel>>()
    val addressItemsObserver: LiveData<List<AddressItemModel>> = addressItemsController

    private val searchSubject = PublishSubject.create<String>()

    init {
        setupSearch()
    }

    fun onQueryChanged(query: String){
        if (query.isNotEmpty()){
            searchSubject.onNext(query)
        }
    }

    fun onAddressItemClick(addressItem: AddressItemModel){
        locationInteractor.onAddressSelected(addressItem)
        closeController.value = Unit
    }

    private fun setupSearch() {
        searchSubject
            .debounce(INPUT_DELAY, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .flatMapSingle { query->
                locationInteractor.getAddressSuggestions(query)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    addressItemsController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }
}