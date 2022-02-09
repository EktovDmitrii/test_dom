package com.custom.rgs_android_dom.domain.purchase.models

import java.io.Serializable

abstract class CardModel(
    open var isSelected: Boolean,
    open var id: String
) : Serializable

data class NewCardModel(
    override var id: String = "new_card",
    var doSave: Boolean = false,
    val title: String = "Новой картой",
    override var isSelected: Boolean = false
): CardModel(isSelected, id)

data class SavedCardModel(
    override var id: String,
    val number: String,
    val type: CardType,
    val expireDate: String,
    val isDefault: Boolean,
    override var isSelected: Boolean = false
): CardModel(isSelected, id)

enum class CardType {
    VISA, MASTERCARD
}