package com.custom.rgs_android_dom.utils

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProgressTransformer<R>(
    private val onLoading: () -> Unit,
    private val onError: (Throwable) -> Unit,
    private val onLoaded: () -> Unit
) : SingleTransformer<R, R>, MaybeTransformer<R, R>, CompletableTransformer {

    override fun apply(upstream: Single<R>): SingleSource<R> {
        return upstream
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onLoading() }
            .doOnError { onError(it) }
            .doOnSuccess { onLoaded() }
    }

    override fun apply(upstream: Maybe<R>): MaybeSource<R> {
        return upstream
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onLoading() }
            .doOnError { onError(it) }
            .doOnSuccess { onLoaded() }
    }

    override fun apply(upstream: Completable): CompletableSource {
        return upstream
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onLoading() }
            .doOnError { onError(it) }
            .doOnComplete { onLoaded() }
    }
}