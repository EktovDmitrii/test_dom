package com.custom.rgs_android_dom.domain.property.details

import com.custom.rgs_android_dom.domain.property.details.exceptions.PropertyField
import com.custom.rgs_android_dom.domain.property.details.exceptions.ValidatePropertyException
import com.custom.rgs_android_dom.domain.property.details.view_states.PropertyDetailsViewState
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.domain.repositories.PropertyRepository
import io.reactivex.Completable
import io.reactivex.subjects.PublishSubject

class PropertyDetailsInteractor(private val propertyRepository: PropertyRepository) {

    val propertyDetailsViewStateSubject = PublishSubject.create<PropertyDetailsViewState>()

    private var propertyDetailsViewState = PropertyDetailsViewState(
        name = "",
        type = "",
        address = "",
        entrance = "",
        corpus = "",
        floor = "",
        flat ="",
        isOwn = null,
        isRent = null,
        isTemporary = null,
        totalArea = "",
        comment = "",
        isAddTextViewEnabled = false
    )

    // TODO Will be changed later
    fun updatePropertyName(propertyCount: Int, propertyType: PropertyType){

        var name = when (propertyType){
            PropertyType.HOUSE -> "Дом"
            PropertyType.APARTMENT -> "Квартира"
            else -> "Объект"
        }
        if (propertyCount >0){
            name = "$name ${(propertyCount+1)}"
        }
        else if (propertyCount == 1){
            name = "$name ${(propertyCount)}"
        }
        propertyDetailsViewState = propertyDetailsViewState.copy(name = name)
        checkIfPropertyDetailsFieldsFilled()
    }

    fun updatePropertyType(type: PropertyType) {
        propertyDetailsViewState = propertyDetailsViewState.copy(type = type.type)
        checkIfPropertyDetailsFieldsFilled()
    }

    fun updatePropertyAddress(address: String){
        propertyDetailsViewState = propertyDetailsViewState.copy(address = address)
        checkIfPropertyDetailsFieldsFilled()
    }

    fun updatePropertyEntrance(entrance: String){
        propertyDetailsViewState = propertyDetailsViewState.copy(entrance = entrance)
    }

    fun updatePropertyCorpus(corpus: String){
        propertyDetailsViewState = propertyDetailsViewState.copy(corpus = corpus.trim())
    }

    fun updatePropertyFloor(floor: String) {
        propertyDetailsViewState = propertyDetailsViewState.copy(floor = floor)
    }

    fun updatePropertyFlat(flat: String){
        propertyDetailsViewState = propertyDetailsViewState.copy(flat = flat)
    }

    fun updatePropertyIsOwn(isOwn: String?) {
        propertyDetailsViewState = propertyDetailsViewState.copy(isOwn = isOwn)
    }

    fun updatePropertyIsRent(isRent: String?) {
        propertyDetailsViewState = propertyDetailsViewState.copy(isRent = isRent)
    }

    fun updatePropertyIsTemporary(isTemporary: String?){
        propertyDetailsViewState = propertyDetailsViewState.copy(isTemporary = isTemporary)
    }

    fun updatePropertyTotalArea(totalArea: String){
        propertyDetailsViewState = propertyDetailsViewState.copy(totalArea = totalArea)
    }

    fun updatePropertyComment(comment: String){
        propertyDetailsViewState = propertyDetailsViewState.copy(comment = comment.trim())
    }

    fun addProperty(): Completable {
        var address = propertyDetailsViewState.address.trim()

        if (address.isEmpty()){
            return Completable.error(
                ValidatePropertyException(
                    field = PropertyField.ADDRESS,
                    errorMessage = ""
                )
            )
        }
        propertyDetailsViewState.corpus.takeIf { it.isNotEmpty() }?.let {
            address = "$address, корпус $it"
        }
        propertyDetailsViewState.entrance.takeIf { it.isNotEmpty() }?.let {
            address = "$address, подъезд $it"
        }
        propertyDetailsViewState.floor.takeIf { it.isNotEmpty() }?.let {
            address = "$address, этаж $it"
        }
        propertyDetailsViewState.flat.takeIf { it.isNotEmpty() }?.let {
            address = "$address, квартира $it"
        }

        val totalArea = if (propertyDetailsViewState.totalArea.isNotEmpty()) propertyDetailsViewState.totalArea.toFloat() else null
        val comment = propertyDetailsViewState.comment.ifEmpty { null }

        return propertyRepository.addProperty(
            name = propertyDetailsViewState.name,
            type = propertyDetailsViewState.type,
            address = address,
            isOwn = propertyDetailsViewState.isOwn,
            isRent = propertyDetailsViewState.isRent,
            isTemporary = propertyDetailsViewState.isTemporary,
            totalArea = totalArea,
            comment = comment
        )
    }

    private fun checkIfPropertyDetailsFieldsFilled() {
        propertyDetailsViewState = propertyDetailsViewState.copy(isAddTextViewEnabled = propertyDetailsViewState.address.isNotEmpty())
        propertyDetailsViewStateSubject.onNext(propertyDetailsViewState)
    }
}