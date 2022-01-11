package com.custom.rgs_android_dom.domain.property

import android.content.Context
import android.net.Uri
import com.custom.rgs_android_dom.data.network.mappers.PropertyMapper
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.domain.property.details.exceptions.PropertyDocumentValidationException
import com.custom.rgs_android_dom.domain.property.details.exceptions.PropertyDocumentValidationException.*
import com.custom.rgs_android_dom.domain.property.details.exceptions.PropertyField
import com.custom.rgs_android_dom.domain.property.details.exceptions.ValidatePropertyException
import com.custom.rgs_android_dom.domain.property.details.view_states.PropertyDetailsViewState
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.domain.property.view_states.SelectPropertyTypeViewState
import com.custom.rgs_android_dom.domain.property.view_states.SelectAddressViewState
import com.custom.rgs_android_dom.domain.repositories.PropertyRepository
import com.custom.rgs_android_dom.utils.convertToFile
import com.custom.rgs_android_dom.utils.sizeInMb
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.io.File

class PropertyInteractor(
    private val propertyRepository: PropertyRepository,
    private val context: Context
) {

    companion object {
        private const val TOTAL_MAX_SIZE = 250.0   // Mb
        private const val ONE_FILE_MAX_SIZE = 10.0 // Mb

        private val supportedFileExtensions =
            listOf("jpeg", "jpg", "png", "bmp", "pdf", "txt", "doc", "docx", "rtf")
        private val mediaFilesExtensions = listOf("jpeg", "jpg", "png", "bmp")
    }

    val selectAddressViewStateSubject = PublishSubject.create<SelectAddressViewState>()
    val selectPropertyTypeViewStateSubject = PublishSubject.create<SelectPropertyTypeViewState>()
    val propertyDetailsViewStateSubject = PublishSubject.create<PropertyDetailsViewState>()
    val propertyInfoStateSubject = PublishSubject.create<PropertyItemModel>()
    val propertyDocumentUploadedSubject = propertyRepository.getPropertyDocumentUploadedSubject()
    private lateinit var documentValidationException: PropertyDocumentValidationException

    private var selectAddressViewState = SelectAddressViewState(
        isNextTextViewEnabled = false,
        propertyName = "",
        updatePropertyNameEditText = false,
        propertyAddress = AddressItemModel.createEmpty()
    )

    private var selectPropertyTypeViewState = SelectPropertyTypeViewState(
        isSelectHomeLinearLayoutSelected = false,
        isSelectAppartmentLinearLayoutSelected = false,
        isInfoboxFrameLayoutVisible = false,
        isNextTextViewEnabled = false
    )

    private var propertyDetailsViewState = PropertyDetailsViewState(
        name = "",
        type = "",
        address = AddressItemModel.createEmpty(),
        entrance = "",
        corpus = "",
        floor = "",
        flat = "",
        isOwn = null,
        isRent = null,
        isTemporary = null,
        totalArea = "",
        comment = "",
        isAddTextViewEnabled = false,
        documents = listOf()
    )

    /**
     *
     * Code for SelectAddress screen
     *
     */

    fun initPropertyName(propertyCount: Int) {
        val propertyName = if (propertyCount > 0) {
            "Мой Дом ${(propertyCount + 1)}"
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

    fun onPropertyNameChanged(name: String) {
        selectAddressViewState = selectAddressViewState.copy(
            propertyName = name,
            isNextTextViewEnabled = name.isNotEmpty(),
            updatePropertyNameEditText = false
        )
        selectAddressViewStateSubject.onNext(selectAddressViewState)
    }

    fun onPropertyAddressChanged(address: AddressItemModel) {
        selectAddressViewState = selectAddressViewState.copy(
            propertyAddress = address.copy(),
            isNextTextViewEnabled = address.addressString.isNotEmpty(),
            updatePropertyNameEditText = false
        )
        selectAddressViewStateSubject.onNext(selectAddressViewState)
    }

    fun onFailedToGetLocation() {
        selectAddressViewState = selectAddressViewState.copy(
            updatePropertyNameEditText = false
        )
        selectAddressViewStateSubject.onNext(selectAddressViewState)
    }

    fun onLocationLoaded() {
        selectAddressViewState = selectAddressViewState.copy(
            updatePropertyNameEditText = false
        )
        selectAddressViewStateSubject.onNext(selectAddressViewState)
    }


    /**
     *
     * Code for SelectPropertyType screen
     *
     */

    fun selectHome() {
        selectPropertyTypeViewState = selectPropertyTypeViewState.copy(
            isSelectHomeLinearLayoutSelected = true,
            isSelectAppartmentLinearLayoutSelected = false,
            isInfoboxFrameLayoutVisible = true,
            isNextTextViewEnabled = true
        )
        selectPropertyTypeViewStateSubject.onNext(selectPropertyTypeViewState)
    }

    fun selectApartment() {
        selectPropertyTypeViewState = selectPropertyTypeViewState.copy(
            isSelectHomeLinearLayoutSelected = false,
            isSelectAppartmentLinearLayoutSelected = true,
            isInfoboxFrameLayoutVisible = true,
            isNextTextViewEnabled = true
        )
        selectPropertyTypeViewStateSubject.onNext(selectPropertyTypeViewState)
    }

    /**
     *
     * Code for PropertyDetails Screen
     *
     */

    fun initPropertyDetails(
        propertyName: String,
        type: PropertyType,
        address: AddressItemModel
    ): PropertyDetailsViewState {
        propertyDetailsViewState = propertyDetailsViewState.copy(
            name = propertyName,
            type = type.type,
            address = address.copy(),
            isAddTextViewEnabled = address.addressString.isNotEmpty()
        )
        return propertyDetailsViewState
    }

    fun updatePropertyAddress(newAddress: String) {
        // TODO Do not forget to make a copy of such data, to avoid data loss while navigation between fragments
        val addressCopy = propertyDetailsViewState.address.copy()
        addressCopy.addressString = newAddress
        propertyDetailsViewState = propertyDetailsViewState.copy(address = addressCopy)
        checkIfPropertyDetailsFieldsFilled()
    }

    fun updatePropertyEntrance(entrance: String) {
        propertyDetailsViewState = propertyDetailsViewState.copy(entrance = entrance)
    }

    fun updatePropertyCorpus(corpus: String) {
        propertyDetailsViewState = propertyDetailsViewState.copy(corpus = corpus.trim())
    }

    fun updatePropertyFloor(floor: String) {
        propertyDetailsViewState = propertyDetailsViewState.copy(floor = floor)
    }

    fun updatePropertyFlat(flat: String) {
        propertyDetailsViewState = propertyDetailsViewState.copy(flat = flat)
    }

    fun updatePropertyIsOwn(isOwn: String?) {
        propertyDetailsViewState = propertyDetailsViewState.copy(isOwn = isOwn)
    }

    fun updatePropertyIsRent(isRent: String?) {
        propertyDetailsViewState = propertyDetailsViewState.copy(isRent = isRent)
    }

    fun updatePropertyIsTemporary(isTemporary: String?) {
        propertyDetailsViewState = propertyDetailsViewState.copy(isTemporary = isTemporary)
    }

    fun updatePropertyTotalArea(totalArea: String) {
        propertyDetailsViewState = propertyDetailsViewState.copy(totalArea = totalArea)
    }

    fun updatePropertyComment(comment: String) {
        propertyDetailsViewState = propertyDetailsViewState.copy(comment = comment)
    }

    fun addProperty(): Completable {

        val documentsToPost = urisToFiles(propertyDetailsViewState.documents)
        if (validateFiles(documentsToPost)) {

            var addressString = propertyDetailsViewState.address.addressString.trim()

            if (addressString.isEmpty()) {
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

            val totalArea =
                if (propertyDetailsViewState.totalArea.isNotEmpty()) propertyDetailsViewState.totalArea.toFloat() else null
            val comment = propertyDetailsViewState.comment.ifEmpty { null }

            return postDocumentsSingle(documentsToPost)
                .flatMapCompletable { propertyDocuments ->
                    propertyRepository.addProperty(
                        name = propertyDetailsViewState.name,
                        type = propertyDetailsViewState.type,
                        address = propertyDetailsViewState.address.copy(),
                        isOwn = propertyDetailsViewState.isOwn,
                        isRent = propertyDetailsViewState.isRent,
                        isTemporary = propertyDetailsViewState.isTemporary,
                        totalArea = totalArea,
                        comment = comment,
                        documents = propertyDocuments
                    )
                }
        } else {
            return Completable.error(documentValidationException)
        }
    }

    private fun postDocumentsSingle(files: List<File>): Single<List<PropertyDocument>> {
        return Observable.fromArray(files)
            .flatMapIterable { it }
            .flatMapSingle { file ->
                propertyRepository.postPropertyDocument(file)
                    .map { PropertyMapper.postPropertyDocumentToPropertyDocument(it, file) }
            }
            .toList()
    }

    private fun urisToFiles(uris: List<Uri>): List<File> {
        val files = mutableListOf<File>()

        uris.forEach { uri ->
            val file = uri.convertToFile(context)
            if (file != null) {
                files.add(file)
            }
        }
        return files
    }

    fun getAllProperty(): Single<List<PropertyItemModel>> {
        return propertyRepository.getAllProperty()
    }

    fun getPropertyItem(objectId: String): Single<PropertyItemModel> {
        return propertyRepository.getAllProperty().map { allProperty ->
            allProperty.find { it.id == objectId }
        }
    }

    fun getPropertyAddedSubject(): PublishSubject<Unit> {
        return propertyRepository.getPropertyAddedSubject()
    }

    private fun checkIfPropertyDetailsFieldsFilled() {
        propertyDetailsViewState =
            propertyDetailsViewState.copy(isAddTextViewEnabled = propertyDetailsViewState.address.addressString.isNotEmpty())
        propertyDetailsViewStateSubject.onNext(propertyDetailsViewState)
    }

    fun onFilesToUploadSelected(files: List<Uri>) {
        propertyRepository.onFilesToUploadSelected(files)
    }

    private fun validateFiles(files: List<File>): Boolean {

        var totalSizeMediaFiles = 0.0
        var totalSizeTextFiles = 0.0

        files.forEach { file ->
            if (!validateExtension(file)) {
                documentValidationException = UnsupportedFileType(file.extension)
                return false
            }

            if (!validateSize(file)) {
                documentValidationException = FileSizeExceeded
                return false
            }

            val fileExtension = file.extension

            if (mediaFilesExtensions.contains(fileExtension)) {
                totalSizeMediaFiles += file.sizeInMb
            } else {
                totalSizeTextFiles += file.sizeInMb
            }

            if (totalSizeMediaFiles > TOTAL_MAX_SIZE || totalSizeTextFiles > TOTAL_MAX_SIZE) {
                documentValidationException = TotalFilesSizeExceeded
                return false
            }

        }

        return true
    }

    private fun validateExtension(file: File): Boolean {
        if (!supportedFileExtensions.contains(file.extension)) {
            return false
        }
        return true
    }

    private fun validateSize(file: File): Boolean {
        if (file.sizeInMb > ONE_FILE_MAX_SIZE) {
            return false
        }
        return true
    }

    fun updateDocuments(it: List<Uri>) {
        val current: MutableList<Uri> = mutableListOf()
        current.addAll(propertyDetailsViewState.documents)
        current.addAll(it)
        propertyDetailsViewState = propertyDetailsViewState.copy(documents = current)
        propertyDetailsViewStateSubject.onNext(propertyDetailsViewState)
    }

    fun onRemoveDocument(uri: Uri) {
        val current: MutableList<Uri> = propertyDetailsViewState.documents as MutableList<Uri>
        current.remove(uri)
        propertyDetailsViewState = propertyDetailsViewState.copy(documents = current)
        propertyDetailsViewStateSubject.onNext(propertyDetailsViewState)
    }

    /**
     *
     * Code for PropertyInfo Screen
     *
     */

    fun updatePropertyItem(
        objectId: String,
        propertyItemModel: PropertyItemModel,
        filesUri: List<Uri>
    ): Single<PropertyItemModel> {
        val documentsToPost = urisToFiles(filesUri)
        return postDocumentsSingle(documentsToPost)
            .flatMap { propertyDocuments ->
                val newDocumentsList = propertyItemModel.documents+propertyDocuments
                propertyItemModel.documents = newDocumentsList
                propertyRepository.updateProperty(
                    objectId,
                    propertyItemModel
                )
            }
    }
}
