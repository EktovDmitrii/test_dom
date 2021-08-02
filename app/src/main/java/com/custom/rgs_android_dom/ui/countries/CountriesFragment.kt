package com.custom.rgs_android_dom.ui.countries

import android.os.Bundle
import android.util.Log
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentCountriesBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class CountriesFragment : BaseFragment<CountriesViewModel, FragmentCountriesBinding>(
    R.layout.fragment_countries
) {

    companion object {
        private const val ARG_SELECTED_COUNTRY_LETTER_CODE = "ARG_SELECTED_COUNTRY_LETTER_CODE"

        fun newInstance(selectedCountryLetterCode: String): CountriesFragment {
            return CountriesFragment().args {
                putSerializable(ARG_SELECTED_COUNTRY_LETTER_CODE, selectedCountryLetterCode)
            }
        }
    }

    private val countriesAdapter: CountriesAdapter
        get() = binding.countriesRecyclerView.adapter as CountriesAdapter

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_SELECTED_COUNTRY_LETTER_CODE))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.countriesRecyclerView.adapter = CountriesAdapter(
            onCountryClick = {
                viewModel.onCountryClick(it)
            }
        )


        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseClick()
        }

        binding.searchInput.addTextChangedListener {
            viewModel.onSearchQueryChanged(it)
        }

        binding.searchInput.setOnFocusChangedListener {

        }

        binding.searchInput.setOnClearClickListener {
            viewModel.onClearClick()
        }

        subscribe(viewModel.countriesObserver){
            Log.d("MyLog", "SET ITEMS " + it.size)
            countriesAdapter.setItems(it)
        }

        subscribe(viewModel.isSearchInputFocusedObserver){
            binding.searchInput.setFocus(it)
        }
    }

    override fun onClose() {
        hideSoftwareKeyboard()
        ScreenManager.back(getNavigateId())
    }

    override fun onLoading() {
        super.onLoading()

    }

    override fun onError() {
        super.onError()

    }

    override fun onContent() {
        super.onContent()

    }

}