package com.custom.rgs_android_dom.ui.address.suggestions

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
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
        hideSoftwareKeyboard(true)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        requireActivity().hideKeyboardForced()
    }

}