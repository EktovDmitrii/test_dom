package com.custom.rgs_android_dom.domain.property

import android.util.Log
import com.custom.rgs_android_dom.domain.property.details.exceptions.PropertyField
import com.custom.rgs_android_dom.domain.property.details.exceptions.ValidatePropertyException
import com.custom.rgs_android_dom.domain.property.details.view_states.PropertyDetailsViewState
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.domain.property.view_states.SelectPropertyTypeViewState
import com.custom.rgs_android_dom.domain.property.view_states.SelectAddressViewState
import com.custom.rgs_android_dom.domain.repositories.PropertyRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class PropertyInteractor(private val propertyRepository: PropertyRepository){

    val selectAddressViewStateSubject = PublishSubject.create<SelectAddressViewState>()
    val selectPropertyTypeViewStateSubject = PublishSubject.create<SelectPropertyTypeViewState>()
    val propertyDetailsViewStateSubject = PublishSubject.create<PropertyDetailsViewState>()

    private var selectAddressViewState = SelectAddressViewState(
        isNextTextViewEnabled = false,
        propertyName = "",
        isMyLocationImageViewVisible = false,
        updatePropertyNameEditText = false,
        propertyAddress = ""
    )

    private var selectPropertyTypeViewState = SelectPropertyTypeViewState(
        isSelectHomeLinearLayoutSelected = false,
        isSelectAppartmentLinearLayoutSelected = false,
        isNextTextViewEnabled = false
    )

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
        isAddTextViewEnabled = false,
        updatePropertyAddressEditText = false
    )

    /**
     *
     * Code for SelectAddress screen
     *
     */

    fun initPropertyName(propertyCount: Int){
        Log.d("MyLog", "Property count " + propertyCount)
        selectAddressViewState = selectAddressViewState.copy(
            propertyName = "Мой Дом ${(propertyCount+1)}",
            isNextTextViewEnabled = true,
            updatePropertyNameEditText = true
        )
        selectAddressViewStateSubject.onNext(selectAddressViewState)
    }

    fun onPropertyNameChanged(name: String){
        selectAddressViewState = selectAddressViewState.copy(
            propertyName = name,
            isNextTextViewEnabled = name.isNotEmpty(),
            updatePropertyNameEditText = false
        )
        selectAddressViewStateSubject.onNext(selectAddressViewState)
    }

    fun onPropertyAddressChanged(address: String){
        selectAddressViewState = selectAddressViewState.copy(
            propertyAddress = address,
            updatePropertyNameEditText = false
        )
        selectAddressViewStateSubject.onNext(selectAddressViewState)
    }

    fun onFailedToGetLocation(){
        selectAddressViewState = selectAddressViewState.copy(
            isMyLocationImageViewVisible = false,
            updatePropertyNameEditText = false
        )
        selectAddressViewStateSubject.onNext(selectAddressViewState)
    }

    fun onLocationLoaded(){
        selectAddressViewState = selectAddressViewState.copy(
            isMyLocationImageViewVisible = true,
            updatePropertyNameEditText = false
        )
        selectAddressViewStateSubject.onNext(selectAddressViewState)
    }


    /**
     *
     * Code for SelectPropertyType screen
     *
     */

    fun selectHome(){
        selectPropertyTypeViewState = selectPropertyTypeViewState.copy(
            isSelectHomeLinearLayoutSelected = true,
            isSelectAppartmentLinearLayoutSelected = false,
            isNextTextViewEnabled = true
        )
        selectPropertyTypeViewStateSubject.onNext(selectPropertyTypeViewState)
    }

    fun selectAppartment(){
        selectPropertyTypeViewState = selectPropertyTypeViewState.copy(
            isSelectHomeLinearLayoutSelected = false,
            isSelectAppartmentLinearLayoutSelected = true,
            isNextTextViewEnabled = true
        )
        selectPropertyTypeViewStateSubject.onNext(selectPropertyTypeViewState)
    }

    /**
     *
     * Code for PropertyDetails Screen
     *
     */

    // TODO Will be changed later

    fun initPropertyDetails(propertyName: String, type: PropertyType, address: String){
        propertyDetailsViewState = propertyDetailsViewState.copy(
            name = propertyName,
            type = type.type,
            address = address,
            updatePropertyAddressEditText = true,
            isAddTextViewEnabled = address.isNotEmpty()
        )
        Log.d("MyLog", "Init address " + address + " PROPERTY " + propertyName)
        propertyDetailsViewStateSubject.onNext(propertyDetailsViewState)
    }

    fun updatePropertyAddress(address: String){
        propertyDetailsViewState = propertyDetailsViewState.copy(address = address, updatePropertyAddressEditText = false)
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

    fun getAllProperty(): Single<List<PropertyItemModel>> {
        return propertyRepository.getAllProperty()
    }

    fun getPropertyItem(objectId: String): Single<PropertyItemModel>{
        return propertyRepository.getAllProperty().map {allProperty->
            allProperty.find { it.id == objectId }
        }
    }

    fun getPropertyAddedSubject(): PublishSubject<Unit> {
        return propertyRepository.getPropertyAddedSubject()
    }

    private fun checkIfPropertyDetailsFieldsFilled() {
        propertyDetailsViewState = propertyDetailsViewState.copy(isAddTextViewEnabled = propertyDetailsViewState.address.isNotEmpty())
        propertyDetailsViewStateSubject.onNext(propertyDetailsViewState)
    }
}



