package com.custom.rgs_android_dom.ui.countries

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
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

    private val scrollChangedListener = ViewTreeObserver.OnScrollChangedListener {
        val scrollBounds = Rect()
        binding.contentNestedScrollView.getHitRect(scrollBounds)
        CrossfadeAnimator.crossfade(binding.titlePrimaryTextView, binding.titleSecondaryTextView, scrollBounds)
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_SELECTED_COUNTRY_LETTER_CODE))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSoftwareKeyboard()

        binding.countriesRecyclerView.adapter = CountriesAdapter(
            onCountryClick = {
                disableSearchMode()
                viewModel.onCountryClick(it)
            }
        )

        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseClick()
        }

        binding.primarySearchInput.setOnFocusChangedListener {
            viewModel.onPrimarySearchInputFocusChanged(it)
        }

        binding.primarySearchInput.addTextChangedListener {
            viewModel.onSearchQueryChanged(it)
        }

        binding.primarySearchInput.setOnClearClickListener {
            viewModel.onClearClick()
        }

        binding.secondarySearchInput.setOnClickListener {
            viewModel.onSecondarySearchInputClick()
        }

        subscribe(viewModel.countriesObserver){
            countriesAdapter.setItems(it)
        }

        subscribe(viewModel.isSearchInputFocusedObserver){
            binding.primarySearchInput.setFocus(it)
        }

        subscribe(viewModel.isEmptyResultsVisibleObserver){
            binding.emptyResultsTextView.visibleIf(it)
            binding.countriesRecyclerView.visibleIf(!it)
        }

        subscribe(viewModel.isSearchModeActivatedObserver){
            if (it){
                activateSearchMode()
            } else{
                disableSearchMode()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.contentNestedScrollView.viewTreeObserver.addOnScrollChangedListener(scrollChangedListener)
    }

    override fun onStop() {
        super.onStop()
        binding.contentNestedScrollView.viewTreeObserver.removeOnScrollChangedListener(scrollChangedListener)
    }

    override fun onClose() {
        if (binding.primarySearchInput.isFocused){
            disableSearchMode()
        } else {
            hideSoftwareKeyboard()
            ScreenManager.back(getNavigateId())
        }
    }

    private fun activateSearchMode(){
        binding.titleFrameLayout.gone()
        binding.secondarySearchInput.gone()
        binding.titlePrimaryTextView.gone()

        binding.searchFrameLayout.visible()

        binding.primarySearchInput.focus()
    }

    private fun disableSearchMode(){
        binding.titleFrameLayout.visible()
        binding.secondarySearchInput.visible()
        binding.titlePrimaryTextView.visible()

        binding.searchFrameLayout.gone()

        binding.primarySearchInput.unfocus()
    }

}