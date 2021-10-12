package com.custom.rgs_android_dom.ui.location.suggestions

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentAddressSuggestionsBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.hideSoftwareKeyboard
import com.custom.rgs_android_dom.utils.subscribe

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
            addressItemsAdapter.setItems(it)
        }
    }

    override fun isFullScreen(): Boolean {
        return true
    }

    override fun onClose() {
        super.onClose()
        hideSoftwareKeyboard()
    }

}