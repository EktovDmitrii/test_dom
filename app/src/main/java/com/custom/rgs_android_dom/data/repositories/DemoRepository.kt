package com.custom.rgs_android_dom.data.repositories

import com.custom.rgs_android_dom.data.network.mappers.DemoMapper
import com.custom.rgs_android_dom.data.network.responses.DemoResponse
import com.custom.rgs_android_dom.domain.model.DemoModel
import io.reactivex.Single

class DemoRepository {

    private val mockData = DemoResponse("Текст полученный из апи", 1)

    fun getDemo(): Single<DemoModel>{
        return Single.just(mockData)
            .map { DemoMapper.toModel(it) }
    }

}