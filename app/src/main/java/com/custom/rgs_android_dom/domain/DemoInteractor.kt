package com.custom.rgs_android_dom.domain

import com.custom.rgs_android_dom.data.repositories.DemoRepository
import com.custom.rgs_android_dom.domain.model.DemoModel
import io.reactivex.Single

class DemoInteractor(private val demoRepository: DemoRepository) {

    fun getDemoModel(): Single<DemoModel>{
        return demoRepository.getDemo()
    }

}