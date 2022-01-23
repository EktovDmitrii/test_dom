package com.custom.rgs_android_dom.ui.purchase_service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.purchase_service.SavedCardModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class SelectCardViewModel : BaseViewModel() {

    private val savedCardsController = MutableLiveData<List<SavedCardModel>>()
    val savedCardsObserver: LiveData<List<SavedCardModel>> = savedCardsController

    init {
        savedCardsController.value = listOf(
            SavedCardModel(
                id = 0,
                number = "9914",
                type = SavedCardModel.CardType.MASTERCARD
            ),
            SavedCardModel(
                id = 1,
                number = "3084",
                type = SavedCardModel.CardType.VISA
            ),
            SavedCardModel(
                id = 0,
                number = "9914",
                type = SavedCardModel.CardType.MASTERCARD
            ),
            SavedCardModel(
                id = 1,
                number = "3084",
                type = SavedCardModel.CardType.VISA
            ),
            SavedCardModel(
                id = 0,
                number = "9914",
                type = SavedCardModel.CardType.MASTERCARD
            ),
            SavedCardModel(
                id = 1,
                number = "3084",
                type = SavedCardModel.CardType.VISA
            ),
            SavedCardModel(
                id = 0,
                number = "9914",
                type = SavedCardModel.CardType.MASTERCARD
            ),
            SavedCardModel(
                id = 1,
                number = "3084",
                type = SavedCardModel.CardType.VISA
            ),
            SavedCardModel(
                id = 0,
                number = "9914",
                type = SavedCardModel.CardType.MASTERCARD
            ),
            SavedCardModel(
                id = 1,
                number = "3084",
                type = SavedCardModel.CardType.VISA
            ),
            SavedCardModel(
                id = 0,
                number = "9914",
                type = SavedCardModel.CardType.MASTERCARD
            ),
            SavedCardModel(
                id = 1,
                number = "3084",
                type = SavedCardModel.CardType.VISA
            ),
            SavedCardModel(
                id = 0,
                number = "9914",
                type = SavedCardModel.CardType.MASTERCARD
            ),
            SavedCardModel(
                id = 1,
                number = "3084",
                type = SavedCardModel.CardType.VISA
            )
        )
    }

}