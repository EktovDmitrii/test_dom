package com.custom.rgs_android_dom.ui.promo_code.add_promo_code

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.promo_code.add_agent.AddAgentPromoCodeFragment
import com.custom.rgs_android_dom.ui.promo_code.dialogs.PromoCodeDialogFragment

class AddPromoCodeViewModel : BaseViewModel() {

    private val promoCodeController = MutableLiveData<String>()
    val promoCodeObserver: LiveData<String> = promoCodeController

    fun onPromoCodeChanged(text: String) {
        promoCodeController.value = text.trim()
    }

    fun onFirstSaveButtonClick(
        parentFragmentManager: FragmentManager,
        shouldShowAgentView: Boolean,
        purchaseModel: PurchaseModel?
    ) {
        promoCodeController.value?.let { promoCodeTest ->
            val dialog = PromoCodeDialogFragment.newInstance(
                promoCodeTest,
                shouldShowAgentView,
                purchaseModel
            )
            dialog.show(parentFragmentManager, dialog.TAG)
            close()
        }
    }

    fun onSecondSaveButtonClick(
        parentFragmentManager: FragmentManager,
        shouldShowAgentView: Boolean,
        purchaseModel: PurchaseModel?
    ) {
        promoCodeObserver.value?.let { promoCode ->
            val dialog = AddAgentPromoCodeFragment.newInstance(
                promoCode,
                shouldShowAgentView,
                purchaseModel
            )
            dialog.show(parentFragmentManager, dialog.TAG)
            close()
        }
    }
}
