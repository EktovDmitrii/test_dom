package com.custom.rgs_android_dom.domain.property

import android.content.Context
import android.net.Uri
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.mappers.PropertyMapper
import com.custom.rgs_android_dom.data.repositories.files.FilesRepositoryImpl
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.domain.property.details.exceptions.PropertyDocumentValidationException
import com.custom.rgs_android_dom.domain.property.details.exceptions.PropertyDocumentValidationException.*
import com.custom.rgs_android_dom.domain.property.details.exceptions.PropertyField
import com.custom.rgs_android_dom.domain.property.details.exceptions.ValidatePropertyException
import com.custom.rgs_android_dom.domain.property.details.view_states.PropertyDetailsViewState
import com.custom.rgs_android_dom.domain.property.models.ModificationTask
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.property.models.PropertyType
import com.custom.rgs_android_dom.domain.property.view_states.SelectPropertyTypeViewState
import com.custom.rgs_android_dom.domain.property.view_states.SelectAddressViewState
import com.custom.rgs_android_dom.domain.repositories.FilesRepository
import com.custom.rgs_android_dom.domain.repositories.PropertyRepository
import com.custom.rgs_android_dom.utils.convertToFile
import com.custom.rgs_android_dom.utils.sizeInMb
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.io.File

class PropertyInteractor(
    private val propertyRepository: PropertyRepository,
    private val filesRepository: FilesRepository,
    private val context: Context
) {

    companion object {
        private const val TOTAL_MAX_SIZE = 250.0   // Mb
        private const val ONE_FILE_MAX_SIZE = 10.0 // Mb

        const val STORE_PATH = "${BuildConfig.BASE_URL}/api/store/"

        private val supportedFileExtensions =
            listOf("jpeg", "jpg", "png", "bmp", "pdf", "txt", "doc", "docx", "rtf")
        private val mediaFilesExtensions = listOf("jpeg", "jpg", "png", "bmp")
    }

    val selectAddressViewStateSubject = PublishSubject.create<SelectAddressViewState>()
    val selectPropertyTypeViewStateSubject = PublishSubject.create<SelectPropertyTypeViewState>()
    val propertyDetailsViewStateSubject = PublishSubject.create<PropertyDetailsViewState>()
    val propertyInfoStateSubject = PublishSubject.create<PropertyItemModel>()
    val propertyAvatarSubject = PublishSubject.create<String>()
    val closePropertyPageSubject = propertyRepository.getClosePropertyPageSubject()
    val propertyAvatarUrlChangedSubject = propertyRepository.getPropertyAvatarChangedSubject()
    val propertyAvatarUrlRemovedSubject = propertyRepository.getPropertyAvatarRemovedSubject()
    val propertyDocumentUploadedSubject = propertyRepository.getPropertyDocumentUploadedSubject()
    val propertyDocumentDeletedSubject = propertyRepository.getPropertyDocumentDeletedSubject()
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
        photoLink = null,
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

    fun initPropertyDetailsForUpdate(propertyItemModel: PropertyItemModel): PropertyDetailsViewState {
        propertyDetailsViewState = propertyDetailsViewState.copy(
            name = propertyItemModel.name,
            type = propertyItemModel.type.type,
            address = AddressItemModel.createEmpty().copy(
                addressString = propertyItemModel.address?.address ?: "",
                cityFiasId = propertyItemModel.address?.cityFiasId ?: "",
                cityName = propertyItemModel.address?.cityName ?: "",
                fiasId = propertyItemModel.address?.fiasId ?: "",
                regionFiasId = propertyItemModel.address?.regionFiasId ?: "",
                regionName = propertyItemModel.address?.regionName ?: ""
            ),
            photoLink = propertyItemModel.photoLink,
            entrance = propertyItemModel.address?.entrance?.let { it.toString() } ?: "",
            floor = propertyItemModel.address?.floor?.let { it.toString() } ?: "",
            isOwn = if (propertyItemModel.isOwn == true) "yes" else if (propertyItemModel.isOwn == false) "no" else null,
            isRent = if (propertyItemModel.isRent == true) "yes" else if (propertyItemModel.isRent == false) "no" else null,
            isTemporary = if (propertyItemModel.isTemporary == true) "yes" else if (propertyItemModel.isTemporary == false) "no" else null,
            totalArea = propertyItemModel.totalArea?.let { it.toString() } ?: "",
            comment = propertyItemModel.comment,
            documentsFormatted = propertyItemModel.documents
        )
        return propertyDetailsViewState
    }

    fun updatePropertyName(name: String) {
        propertyDetailsViewState = propertyDetailsViewState.copy(
            name = name
        )
    }

    fun updatePropertyType(type: String) {
        propertyDetailsViewState = propertyDetailsViewState.copy(
            type = type
        )
    }

    fun updatePropertyAddress(addressItemModel: AddressItemModel) {
        propertyDetailsViewState = propertyDetailsViewState.copy(
            address = addressItemModel.copy()
        )
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

    fun updateAvatar(id: String?) {
        propertyDetailsViewState =  if (id != null) {
            propertyAvatarSubject.onNext(STORE_PATH + id)
            propertyDetailsViewState.copy(photoLink = STORE_PATH + id)
        } else {
            propertyAvatarSubject.onNext("")
            propertyDetailsViewState.copy(photoLink = null)
        }
    }

    fun updatePropertyAvatar(avatar: File): Completable {
        return filesRepository.putFileToTheStore(avatar, FilesRepositoryImpl.STORE_AVATARS)
            .map { id ->
                propertyAvatarUrlChangedSubject.onNext(id)
            }.doFinally {
                avatar.delete()
            }.ignoreElement()
    }

    fun removePropertyAvatar() {
        propertyAvatarUrlRemovedSubject.onNext(Unit)
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
            propertyDetailsViewState.flat.takeIf { it.isNotEmpty() }?.let {
                addressString = "$addressString, квартира $it"
            }

            propertyDetailsViewState.address.addressString = addressString

            val floor = if (propertyDetailsViewState.floor.isEmpty()) null else propertyDetailsViewState.floor.toInt()
            val entrance = if (propertyDetailsViewState.entrance.isEmpty()) null else propertyDetailsViewState.entrance.toInt()
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
                        documents = propertyDocuments,
                        floor = floor,
                        entrance = entrance,
                    )
                }
        } else {
            return Completable.error(documentValidationException)
        }
    }

    fun updateProperty(objectId: String): Completable {
        val addressString = propertyDetailsViewState.address.addressString.trim()
        if (addressString.isEmpty()) {
            return Completable.error(
                ValidatePropertyException(
                    field = PropertyField.ADDRESS,
                    errorMessage = ""
                )
            )
        }

        val floor = if (propertyDetailsViewState.floor.isEmpty()) null else propertyDetailsViewState.floor.toInt()
        val entrance = if (propertyDetailsViewState.entrance.isEmpty()) null else propertyDetailsViewState.entrance.toInt()
        val totalArea =
            if (propertyDetailsViewState.totalArea.isNotEmpty()) propertyDetailsViewState.totalArea.toFloat() else null
        val comment = propertyDetailsViewState.comment.ifEmpty { null }

        return propertyRepository.updatePropertyInfo(
            objectId = objectId,
            name = propertyDetailsViewState.name,
            type = propertyDetailsViewState.type,
            address = propertyDetailsViewState.address.copy(),
            isOwn = propertyDetailsViewState.isOwn ?: "unspecified",
            isRent = propertyDetailsViewState.isRent ?: "unspecified",
            isTemporary = propertyDetailsViewState.isTemporary ?: "unspecified",
            photoLink = propertyDetailsViewState.photoLink?.removePrefix(STORE_PATH),
            totalArea = totalArea,
            comment = comment,
            floor = floor,
            entrance = entrance,
            documents = propertyDetailsViewState.documentsFormatted
        )
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
        if (files.isNotEmpty()){
            propertyRepository.onFilesToUploadSelected(files)
        }
    }

    fun onFilesToDeleteSelected(propertyItemModel: PropertyItemModel){
        propertyRepository.onFileToDeleteSelected(propertyItemModel)
    }

    private fun validateFiles(files: List<File>): Boolean {

        var totalSizeMediaFiles = 0.0
        var totalSizeTextFiles = 0.0

        files.forEach { file ->
            if (!isExtensionValid(file)) {
                documentValidationException = UnsupportedFileType(file.extension)
                return false
            }

            if (!isSizeValid(file)) {
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

    private fun isExtensionValid(file: File): Boolean {
        if (!supportedFileExtensions.contains(file.extension)) {
            return false
        }
        return true
    }

    private fun isSizeValid(file: File): Boolean {
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

    fun updatePropertyDocuments(
        objectId: String,
        propertyItemModel: PropertyItemModel,
        filesUri: List<Uri>
    ): Single<PropertyItemModel> {
        val documentsToPost = urisToFiles(filesUri)
        return postDocumentsSingle(documentsToPost)
            .flatMap { propertyDocuments ->
                val newDocumentsList = propertyItemModel.documents + propertyDocuments
                propertyRepository.updateProperty(
                    objectId,
                    propertyItemModel.copy(documents = newDocumentsList.toMutableList())
                )
            }
    }

    fun updateDocument(
        objectId: String,
        propertyItemModel: PropertyItemModel
    ) = propertyRepository.updateProperty(
        objectId,
        propertyItemModel
    )

    fun getEditPropertyRequestedSubject(): BehaviorSubject<Boolean> {
        return propertyRepository.getEditPropertyRequestedSubject()
    }

    fun requestModification(objectId: String): Completable {
        return propertyRepository.requestEditProperty(objectId)
    }

    fun getModifications(objectId: String): Single<List<ModificationTask>> {
        return propertyRepository.getModifications(objectId)
    }

    fun getPropertyDeletedSubject(): PublishSubject<String>{
        return propertyRepository.getPropertyDeletedSubject()
    }

    fun deleteProperty(objectId: String): Completable {
        return propertyRepository.deleteProperty(objectId)
    }

    fun closePropertyPage() {
        closePropertyPageSubject.onNext(Unit)
    }
}
