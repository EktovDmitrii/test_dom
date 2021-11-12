package com.custom.rgs_android_dom.domain.repositories

import io.reactivex.Completable
import io.reactivex.Single
import java.io.File

interface FilesRepository {

    fun putFileToTheStore(file: File, store: String): Single<String>

    fun deleteFileFromStore(fileId: String): Completable

}