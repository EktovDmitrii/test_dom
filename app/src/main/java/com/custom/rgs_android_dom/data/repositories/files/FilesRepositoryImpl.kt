package com.custom.rgs_android_dom.data.repositories.files

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.domain.repositories.FilesRepository
import com.custom.rgs_android_dom.utils.asRequestBody
import com.custom.rgs_android_dom.utils.toMultipartFormData
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File

class FilesRepositoryImpl(private val api: MSDApi): FilesRepository {

    companion object {
        const val STORE_AVATARS = "avatars"
    }

    override fun putFileToTheStore(file: File, store: String): Single<String> {
        return api.putFileToTheStore(file.toMultipartFormData(), store).map {
            it.id
        }
    }

    override fun deleteFileFromStore(fileId: String): Completable {
        return api.deleteFileFromStore(fileId)
    }

}