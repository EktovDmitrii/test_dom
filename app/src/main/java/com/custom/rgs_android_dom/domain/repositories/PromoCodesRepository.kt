package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeItemModel
import io.reactivex.Single

interface PromoCodesRepository {

    fun getPromoCodes(): Single<List<PromoCodeItemModel>>

    fun activatePromoCode(promoCodeId: String): Single<PromoCodeItemModel>

}