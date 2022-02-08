package com.custom.rgs_android_dom.ui.purchase.payments.success

import android.text.Spannable
import android.text.SpannableStringBuilder
import androidx.core.text.bold
import androidx.core.text.toSpannable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class PaymentSuccessViewModel(
    private val productId: String,
    email: String
) : BaseViewModel() {

    private val emailController = MutableLiveData<Spannable>()
    val emailObserver: LiveData<Spannable> = emailController

    init {
        emailController.value = SpannableStringBuilder()
            .append("Чек об оплате отправлен на вашу почту ")
            .bold { append(email) }
            .toSpannable()
    }

    fun onCloseScope() {
        closeController.value = Unit
    }

}