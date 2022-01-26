package com.custom.rgs_android_dom.data.repositories.policies

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.domain.policies.models.PolicyModel
import com.custom.rgs_android_dom.domain.repositories.PoliciesRepository
import io.reactivex.Single

class PoliciesRepositoryImpl(private val api: MSDApi) : PoliciesRepository {

    override fun getPolicies(): Single<List<PolicyModel>> {
        return /*api.getPolicies()*/ Single.create {
            val list = listOf<PolicyModel>(
                /*PolicyModel(
                    id = "1",
                    name = "Страховой продукт 1",
                    logo = "",
                    startsAt = "12.09.2022",
                    expiresAt = "23.09.2030"
                ),
                PolicyModel(
                    id = "2",
                    name = "Страховой продукт 2",
                    logo = "",
                    startsAt = "12.09.2022",
                    expiresAt = "21.01.2031"
                ),
                PolicyModel(
                    id = "3",
                    name = "Страховой продукт 3",
                    logo = "",
                    startsAt = "12.09.2022",
                    expiresAt = "22.09.2033"
                ),
                PolicyModel(
                    id = "4",
                    name = "Страховой продукт 4",
                    logo = "",
                    startsAt = "12.09.2022",
                    expiresAt = "23.03.2032"
                ),
                PolicyModel(
                    id = "5",
                    name = "Страховой продукт 5",
                    logo = "",
                    startsAt = "12.09.2022",
                    expiresAt = "25.03.2024"
                )*/
            )
            it.onSuccess(list)
        }
    }

}