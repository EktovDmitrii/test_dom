package com.custom.rgs_android_dom.data.repositories.promo_codes

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.PromoCodesMapper
import com.custom.rgs_android_dom.data.network.requests.ActivatePromoCodeRequest
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeItemModel
import com.custom.rgs_android_dom.domain.repositories.PromoCodesRepository
import io.reactivex.Single

class PromoCodesRepositoryImpl(private val api: MSDApi) : PromoCodesRepository {

    companion object {
        const val PROMO_CODE_STATUSES = "active"
    }

    override fun getPromoCodes(): Single<List<PromoCodeItemModel>> {
        return api.getPromoCodes(statuses = PROMO_CODE_STATUSES).map {
            PromoCodesMapper.responseToPromoCodesItemModels(it.items)
        }
    }

    override fun activatePromoCode(promoCodeId: String): Single<PromoCodeItemModel> {
        val bodyRequest = ActivatePromoCodeRequest(promoCode = promoCodeId)
        return api.activatePromoCode(body = bodyRequest).map {
            PromoCodesMapper.responseToPromoCodesItemModel(it)
        }
    }
}
