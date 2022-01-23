package com.custom.rgs_android_dom.domain.purchase_service

data class SavedCardModel(
    val id: Int,
    val number: String,
    val type: CardType,
    var isSelected: Boolean = false
) {

    companion object {
        fun getEmptyInstance() = SavedCardModel(
            id = -1,
            number = "",
            type = CardType.VISA
        )
    }

    enum class CardType {
        VISA, MASTERCARD
    }
}