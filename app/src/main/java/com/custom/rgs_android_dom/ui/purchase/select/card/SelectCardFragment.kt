package com.custom.rgs_android_dom.ui.purchase.select.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentSelectCardBinding
import com.custom.rgs_android_dom.domain.purchase.models.CardModel
import com.custom.rgs_android_dom.domain.purchase.models.NewCardModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.smoothScrollTo
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import java.io.Serializable

class SelectCardFragment : BaseBottomSheetModalFragment<SelectCardViewModel, FragmentSelectCardBinding>() {

    companion object {
        private const val ARG_SELECTED_CARD = "ARG_SELECTED_CARD"

        fun newInstance(selectedCard: CardModel?): SelectCardFragment {
            return SelectCardFragment().args {
                putSerializable(ARG_SELECTED_CARD, selectedCard)
            }
        }
    }

    override val TAG: String = "SELECT_CARD_FRAGMENT"

    private var purchaseCardListener: PurchaseCardListener? = null

    private val cardsAdapter: CardsAdapter
        get() = binding.cardsRecyclerView.adapter as CardsAdapter

    private var cardModel: CardModel? = null

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getSerializable(ARG_SELECTED_CARD))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (parentFragment is PurchaseCardListener) {
            purchaseCardListener = parentFragment as PurchaseCardListener
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardsRecyclerView.itemAnimator = null
        binding.cardsRecyclerView.adapter = CardsAdapter(
            onCardSelected = {
                cardModel = it
                binding.selectCardTextView.isEnabled = true

                if (it is NewCardModel) {
                    binding.scrollView.smoothScrollTo(binding.selectCardTextView)
                }
            }
        )

        binding.selectCardTextView.setOnDebouncedClickListener {
            cardModel?.let {
                purchaseCardListener?.onSaveCardClick(it)
                dismissAllowingStateLoss()
            }
        }

        subscribe(viewModel.savedCardsObserver){
            cardsAdapter.setItems(it)
            it.find { it.isSelected }?.let {
                binding.selectCardTextView.isEnabled = true
            }
        }
    }

    interface PurchaseCardListener : Serializable {
        fun onSaveCardClick(card: CardModel)
    }
}