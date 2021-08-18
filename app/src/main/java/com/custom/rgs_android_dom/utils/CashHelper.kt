package com.custom.rgs_android_dom.utils

import android.util.Log
import com.custom.rgs_android_dom.data.repositories.client.ClientRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.java.KoinJavaComponent.inject

object CashHelper {

    lateinit var compositeDisposable: CompositeDisposable
    val clientRepository: ClientRepository by inject(ClientRepository::class.java)

    fun init() {
        compositeDisposable = CompositeDisposable()
    }

    fun destroy() {
        compositeDisposable.clear()
    }

    fun loadAndSaveClient() {
        clientRepository.loadAndSaveClient()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = {
                    Log.d("MyLog", "error get client")
                }
            )
            .addTo(compositeDisposable)
    }
}