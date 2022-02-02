package com.custom.rgs_android_dom.ui.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentBottomSelectCardBinding
import com.custom.rgs_android_dom.domain.purchase_service.model.CardModel
import com.custom.rgs_android_dom.domain.purchase_service.model.NewCardModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.smoothScrollTo
import com.custom.rgs_android_dom.utils.subscribe
import java.io.Serializable

class SelectCardBottomFragment : BaseBottomSheetModalFragment<SelectCardViewModel, FragmentBottomSelectCardBinding>() {

    override val TAG: String = "SELECT_CARD_BOTTOM_FRAGMENT"
    private var purchaseCardListener: PurchaseCardListener? = null

    companion object {
        private const val ARG_PURCHASE_SELECTED_CARD = "ARG_PURCHASE_SELECTED_CARD"
        fun newInstance(): SelectCardBottomFragment {
            return SelectCardBottomFragment().args {}
        }
    }

    private val cardsAdapter: SelectCardAdapter
        get() = binding.cardsRecyclerView.adapter as SelectCardAdapter

    private var cardModel: CardModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is PurchaseCardListener) {
            purchaseCardListener = parentFragment as PurchaseCardListener
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardsRecyclerView.adapter = SelectCardAdapter(
            onCardSelected = {
                cardModel = it
                binding.selectButton.isEnabled = true

                if (it is NewCardModel) {
                    binding.scrollView.smoothScrollTo(binding.selectButton)
                }
            }
        )
        binding.selectButton.setOnDebouncedClickListener {
            cardModel?.let {
                purchaseCardListener?.onSaveCardClick(it)
                dismissAllowingStateLoss()
            }
        }

        subscribe(viewModel.savedCardsObserver){
            cardsAdapter.setItems(it)
        }
    }

    interface PurchaseCardListener : Serializable {
        fun onSaveCardClick(card: CardModel)
    }
}