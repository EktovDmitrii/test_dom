package com.custom.rgs_android_dom.ui.purchase.add.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class AddCommentViewModel(private val comment: String?) : BaseViewModel() {

    private val commentController = MutableLiveData<String>()
    val commentObserver: LiveData<String> = commentController

    init {
        commentController.value = comment ?: ""
    }

}