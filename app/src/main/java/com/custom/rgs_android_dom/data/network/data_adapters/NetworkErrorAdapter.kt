package com.custom.rgs_android_dom.data.network.data_adapters


import android.util.Log
import com.custom.rgs_android_dom.data.network.ErrorType
import com.custom.rgs_android_dom.data.network.error.MSDNetworkError
import io.reactivex.*
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type
import kotlin.reflect.KClass

class NetworkException(
    val error: Any?,
    override val message: String = "",
    val throwable: Throwable
) : RuntimeException()

internal class RxCallAdapterWrapperFactory(private val rxJava2CallAdapterFactory: RxJava2CallAdapterFactory) :
    CallAdapter.Factory() {

    companion object {
        fun createAsync(): RxCallAdapterWrapperFactory {
            return RxCallAdapterWrapperFactory(RxJava2CallAdapterFactory.createAsync())
        }
    }

    private fun handleError(annotations: Array<Annotation>, retrofit: Retrofit, throwable: Throwable): Throwable {

        val errorType: ErrorType? = annotations.find { it is ErrorType } as? ErrorType

        return if (errorType != null && throwable is HttpException) {
            val error = parseError(retrofit, throwable, errorType.type)
            NetworkException(error, throwable.message(), throwable)
        } else throwable
    }

    private fun parseError(retrofit: Retrofit, httpException: HttpException, kClass: KClass<*>): Any? {
        if (httpException.response()?.isSuccessful == true) {
            return null
        }
        Log.d("MyLog", "ERROR CODE " + httpException.response()?.code())
        if (httpException.response()?.code() == 401){
            Log.d("MyLog", "TOKEN ERROR")
            return MSDNetworkError("AUTH-016", "token expired")
        } else {
            val errorBody = httpException.response()?.errorBody() ?: return null
            val converter: Converter<ResponseBody, Any> = retrofit.responseBodyConverter(kClass.java, arrayOf())
            return converter.convert(errorBody)
        }

    }

    private inner class RxCallAdapterWrapper constructor(
        private val annotations: Array<Annotation>,
        private val retrofit: Retrofit,
        private val callAdapter: CallAdapter<Any, Any>
    ) : CallAdapter<Any, Any> {

        override fun adapt(call: Call<Any>): Any {
            val any = callAdapter.adapt(call)

            if (any is Observable<*>) {
                return any.onErrorResumeNext(Function { Observable.error(handleError(annotations, retrofit, it)) })
            }

            if (any is Maybe<*>) {
                return any.onErrorResumeNext(Function { Maybe.error(handleError(annotations, retrofit, it)) })
            }

            if (any is Single<*>) {
                return any.onErrorResumeNext(Function { Single.error(handleError(annotations, retrofit, it)) })
            }

            if (any is Flowable<*>) {
                return any.onErrorResumeNext(Function { Flowable.error(handleError(annotations, retrofit, it)) })
            }

            if (any is Completable) {
                return any.onErrorResumeNext(Function { Completable.error(handleError(annotations, retrofit, it)) })
            }

            return any
        }

        override fun responseType(): Type {
            return callAdapter.responseType()
        }
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        @Suppress("UNCHECKED_CAST")
        val rxJava2CallAdapter: CallAdapter<Any, Any> =
            rxJava2CallAdapterFactory.get(returnType, annotations, retrofit) as CallAdapter<Any, Any>
        return RxCallAdapterWrapper(annotations, retrofit, rxJava2CallAdapter)
    }
}