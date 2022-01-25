package com.custom.rgs_android_dom.domain.purchase_service.model

data class SavedCardModel(
    val id: String,
    val number: String,
    val type: CardType,
    val expireDate: String,
    val isDefault: Boolean,
    var isSelected: Boolean = false
) {

    companion object {
        fun getEmptyInstance() = SavedCardModel(
            id = "-1",
            number = "",
            expireDate = "",
            isDefault = false,
            type = CardType.VISA
        )
    }

    enum class CardType {
        VISA, MASTERCARD
    }
}