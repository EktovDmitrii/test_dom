package com.custom.rgs_android_dom.domain.property

import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
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
        propertyAddress = AddressItemModel.createEmpty()
    )

    private var selectPropertyTypeViewState = SelectPropertyTypeViewState(
        isSelectHomeLinearLayoutSelected = false,
        isSelectAppartmentLinearLayoutSelected = false,
        isNextTextViewEnabled = false
    )

    private var propertyDetailsViewState = PropertyDetailsViewState(
        name = "",
        type = "",
        address = AddressItemModel.createEmpty(),
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
        val propertyName = if (propertyCount > 0){
            "Мой Дом ${(propertyCount+1)}"
        } else {
            "Мой Дом"
        }
        selectAddressViewState = selectAddressViewState.copy(
            propertyName = propertyName,
            isNextTextViewEnabled = false,
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

    fun onPropertyAddressChanged(address: AddressItemModel){
        selectAddressViewState = selectAddressViewState.copy(
            propertyAddress = address.copy(),
            isNextTextViewEnabled = address.addressString.isNotEmpty(),
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

    fun initPropertyDetails(propertyName: String, type: PropertyType, address: AddressItemModel): PropertyDetailsViewState {
        propertyDetailsViewState = propertyDetailsViewState.copy(
            name = propertyName,
            type = type.type,
            address = address.copy(),
            updatePropertyAddressEditText = true,
            isAddTextViewEnabled = address.addressString.isNotEmpty()
        )
        return propertyDetailsViewState
    }

    fun updatePropertyAddress(newAddress: String){
        // TODO Do not forget to make a copy of such data, to avoid data loss while navigation between fragments
        val addressCopy = propertyDetailsViewState.address.copy()
        addressCopy.addressString = newAddress
        propertyDetailsViewState = propertyDetailsViewState.copy(address = addressCopy, updatePropertyAddressEditText = false)
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
        propertyDetailsViewState = propertyDetailsViewState.copy(comment = comment)
    }

    fun addProperty(): Completable {
        var addressString = propertyDetailsViewState.address.addressString.trim()

        if (addressString.isEmpty()){
            return Completable.error(
                ValidatePropertyException(
                    field = PropertyField.ADDRESS,
                    errorMessage = ""
                )
            )
        }
        propertyDetailsViewState.corpus.takeIf { it.isNotEmpty() }?.let {
            addressString = "$addressString, корпус $it"
        }
        propertyDetailsViewState.entrance.takeIf { it.isNotEmpty() }?.let {
            addressString = "$addressString, подъезд $it"
        }
        propertyDetailsViewState.floor.takeIf { it.isNotEmpty() }?.let {
            addressString = "$addressString, этаж $it"
        }
        propertyDetailsViewState.flat.takeIf { it.isNotEmpty() }?.let {
            addressString = "$addressString, квартира $it"
        }

        propertyDetailsViewState.address.addressString = addressString

        val totalArea = if (propertyDetailsViewState.totalArea.isNotEmpty()) propertyDetailsViewState.totalArea.toFloat() else null
        val comment = propertyDetailsViewState.comment.ifEmpty { null }

        return propertyRepository.addProperty(
            name = propertyDetailsViewState.name,
            type = propertyDetailsViewState.type,
            address = propertyDetailsViewState.address.copy(),
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
        propertyDetailsViewState = propertyDetailsViewState.copy(isAddTextViewEnabled = propertyDetailsViewState.address.addressString.isNotEmpty())
        propertyDetailsViewStateSubject.onNext(propertyDetailsViewState)
    }
}



