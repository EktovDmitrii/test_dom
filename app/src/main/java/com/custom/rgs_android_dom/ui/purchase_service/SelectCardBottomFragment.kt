package com.custom.rgs_android_dom.ui.purchase_service

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.custom.rgs_android_dom.databinding.FragmentBottomSelectCardBinding
import com.custom.rgs_android_dom.domain.purchase_service.model.CardModel
import com.custom.rgs_android_dom.domain.purchase_service.model.NewCardModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.smoothScrollTo
import com.custom.rgs_android_dom.utils.subscribe

class SelectCardBottomFragment : BaseBottomSheetModalFragment<SelectCardViewModel, FragmentBottomSelectCardBinding>() {

    override val TAG: String = "SELECT_CARD_BOTTOM_FRAGMENT"

    companion object {
        private const val ARG_PURCHASE_SELECTED_CARD = "ARG_PURCHASE_SELECTED_CARD"
        fun newInstance(): SelectCardBottomFragment {
            return SelectCardBottomFragment().args {}
        }
    }

    private val cardsAdapter: SelectCardAdapter
        get() = binding.cardsRecyclerView.adapter as SelectCardAdapter

    private var cardModel: CardModel? = null

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
            setFragmentResult(
                "card_request",
                bundleOf("card_key" to cardModel)
            )
            dismiss()
        }

        subscribe(viewModel.savedCardsObserver){
            cardsAdapter.setItems(it)
        }
    }

}