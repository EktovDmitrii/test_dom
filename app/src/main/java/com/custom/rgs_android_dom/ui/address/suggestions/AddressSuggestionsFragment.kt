package com.custom.rgs_android_dom.ui.address.suggestions

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentAddressSuggestionsBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.activity.hideKeyboardForced

class AddressSuggestionsFragment : BaseBottomSheetModalFragment<AddressSuggestionsViewModel, FragmentAddressSuggestionsBinding>() {

    override val TAG: String = "ADDRESS_SUGGESTIONS_FRAGMENT"

    private val addressItemsAdapter: AddressItemsAdapter
        get() = binding.addressItemsRecycler.adapter as AddressItemsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addressItemsRecycler.adapter = AddressItemsAdapter(){
            viewModel.onAddressItemClick(it)
        }

        binding.searchInput.addTextChangedListener {
            viewModel.onQueryChanged(it)
        }

        subscribe(viewModel.addressItemsObserver){
            binding.addressItemsRecycler.visibleIf(it.isNotEmpty())
            binding.emptyResultsLinearLayout.visibleIf(it.isEmpty())

            addressItemsAdapter.setItems(it)
        }

        subscribe(viewModel.emptyQueryObserver){
            binding.emptyResultsLinearLayout.gone()
            binding.addressItemsRecycler.gone()
        }
    }

    override fun isFullScreen(): Boolean {
        return true
    }

    override fun onClose() {
        super.onClose()
        requireActivity().hideKeyboardForced()
    }

    override fun onDismiss(dialog: DialogInterface) {
        requireActivity().hideKeyboardForced()
        super.onDismiss(dialog)
    }

}