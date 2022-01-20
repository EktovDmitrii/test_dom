package com.custom.rgs_android_dom.ui.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentMainBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.NavigationScope
import com.custom.rgs_android_dom.utils.recycler_view.GridThreeSpanItemDecoration

class MainFragment : BaseBottomSheetFragment<MainViewModel, FragmentMainBinding>() {

    override val TAG: String = "MAIN_FRAGMENT"

    private var isAuthorized = false

    private val ratingCommentsAdapter: RatingCommentsAdapter
        get() = binding.ratingLayout.ratingRecyclerView.adapter as RatingCommentsAdapter

    private val popularServicesAdapter: GridPopularServicesAdapter
        get() = binding.popularServicesLayout.popularServicesRecyclerView.adapter as GridPopularServicesAdapter

    private val popularCategoriesAdapter: PopularCategoriesAdapter
        get() = binding.popularCategoriesLayout.popularCategoriesRecyclerView.adapter as PopularCategoriesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.popularCategoriesLayout.popularCategoriesRecyclerView.adapter =
            PopularCategoriesAdapter { viewModel.onCategoryClick(it) }

        binding.loginLinearLayout.setOnDebouncedClickListener {
            viewModel.onLoginClick()
        }

        binding.noPropertyLinearLayout.setOnDebouncedClickListener {
            viewModel.onNoPropertyClick()
        }

        binding.propertyAvailableLinearLayout.setOnDebouncedClickListener {
            viewModel.onPropertyAvailableClick()
        }

        binding.sosLinearLayout.setOnDebouncedClickListener {
            viewModel.onSOSClick()
        }

        binding.policiesLinearLayout.setOnDebouncedClickListener {
            viewModel.onPoliciesClick()
        }

        binding.productsLinearLayout.setOnDebouncedClickListener {
            viewModel.onProductsClick()
        }

        binding.searchTagsLayout.tagsFlowLayout.children.forEach { view ->
            (view as TextView).let {
                it.setOnDebouncedClickListener {
                    viewModel.onTagClick(it.text.toString())
                }
            }
        }

        binding.aboutApplicationLayout.aboutServiceTextView.setOnDebouncedClickListener {
            viewModel.onAboutServiceClick()
        }

        binding.searchTagsLayout.searchCatalogCardView.setOnDebouncedClickListener {
            viewModel.onSearchClick()
        }

        GlideApp.with(requireContext())
            .load(R.mipmap.master_male_2)
            .transform(GranularRoundedCorners(0f,0f, 16f.dp(requireContext()), 0f))
            .into(binding.ratingLayout.ratingMasterMale)
        binding.ratingLayout.ratingRecyclerView.adapter = RatingCommentsAdapter()

        binding.popularServicesLayout.popularServicesRecyclerView.adapter =
            GridPopularServicesAdapter(
                onServiceClick = { viewModel.onServiceClick(it) }
            )

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.dp_12)
        binding.popularServicesLayout.popularServicesRecyclerView.addItemDecoration(
            GridThreeSpanItemDecoration(spacingInPixels)
        )

        binding.popularServicesLayout.allTextView.setOnDebouncedClickListener {
            viewModel.onAllCatalogClick()
        }

        binding.popularCategoriesLayout.showAllTextView.setOnDebouncedClickListener {
            viewModel.onShowAllPopularCategoriesClick()
        }

        subscribe(viewModel.registrationObserver) {
            isAuthorized = it
            binding.loginLinearLayout.goneIf(it)
            if (!it) {
                binding.propertyAvailableLinearLayout.gone()
                binding.noPropertyLinearLayout.gone()
            }

            viewModel.getPopularProducts()
        }

        subscribe(viewModel.propertyAvailabilityObserver) {
            binding.propertyAvailableLinearLayout.visibleIf(it && isAuthorized)
            binding.noPropertyLinearLayout.goneIf(it || !isAuthorized)
        }

        subscribe(viewModel.rateCommentsObserver){
            ratingCommentsAdapter.setItems(it)
        }

        subscribe(viewModel.popularServicesObserver) {
            binding.popularServicesLayout.root.goneIf(it.isEmpty())

            popularServicesAdapter.setItems(it)
        }

        subscribe(viewModel.popularCategoriesObserver){
            popularCategoriesAdapter.setItems(it)
        }

    }

    override fun onClose() {
        hideSoftwareKeyboard()
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }

    override fun getNavigationScope(): NavigationScope? {
        return NavigationScope.NAV_MAIN
    }
}