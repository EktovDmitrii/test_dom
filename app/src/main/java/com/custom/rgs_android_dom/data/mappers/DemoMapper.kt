package com.custom.rgs_android_dom.data.mappers

import com.custom.rgs_android_dom.data.response.DemoResponse
import com.custom.rgs_android_dom.domain.model.DemoModel

object DemoMapper {

    fun toModel(response: DemoResponse): DemoModel{
        return DemoModel(response.name ?: "Пусто")
    }
}