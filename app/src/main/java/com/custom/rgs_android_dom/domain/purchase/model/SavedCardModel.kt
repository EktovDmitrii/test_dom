package com.custom.rgs_android_dom.domain.purchase.model

import java.io.Serializable

abstract class CardModel(
    open var isSelected: Boolean
) : Serializable

data class NewCardModel(
    val doSave: Boolean = false,
    val title: String = "Новой картой",
    override var isSelected: Boolean = false
): CardModel(isSelected)

data class SavedCardModel(
    val id: String,
    val number: String,
    val type: CardType,
    val expireDate: String,
    val isDefault: Boolean,
    override var isSelected: Boolean = false
): CardModel(isSelected)

enum class CardType {
    VISA, MASTERCARD
}