package com.custom.rgs_android_dom.ui.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentMainBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.NavigationScope
import com.custom.rgs_android_dom.utils.recycler_view.GridThreeSpanItemDecoration
import com.skydoves.androidveil.VeilLayout
import com.yandex.metrica.YandexMetrica

class MainFragment : BaseBottomSheetFragment<MainViewModel, FragmentMainBinding>() {

    override val TAG: String = "MAIN_FRAGMENT"

    private var isAuthorized = false

    private val ratingCommentsAdapter: RatingCommentsAdapter
        get() = binding.ratingLayout.ratingRecyclerView.adapter as RatingCommentsAdapter

    private val popularServicesAdapter: GridPopularServicesAdapter
        get() = binding.popularServicesLayout.popularServicesRecyclerView.adapter as GridPopularServicesAdapter

    private val popularCategoriesAdapter: PopularCategoriesAdapter
        get() = binding.popularCategoriesLayout.popularCategoriesRecyclerView.adapter as PopularCategoriesAdapter

    private val popularProductsAdapter: PopularProductsAdapter
        get() = binding.popularProductsLayout.popularProductsRecyclerView.adapter as PopularProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.popularProductsLayout.popularProductsRecyclerView.adapter =
            PopularProductsAdapter { productId -> viewModel.onPopularProductClick(productId) }

        binding.popularCategoriesLayout.popularCategoriesRecyclerView.adapter =
            PopularCategoriesAdapter { viewModel.onCategoryClick(it) }

        binding.loginLinearLayout.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("mp_action", "{\"action\":\"Войти\"}")

            viewModel.onLoginClick()
        }

        binding.noPropertyLinearLayout.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("mp_action", "{\"action\":\"Добавить\"}")

            viewModel.onNoPropertyClick()
        }

        binding.propertyAvailableLinearLayout.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("mp_action", "{\"action\":\"Мой дом\"}")

            viewModel.onPropertyAvailableClick()
        }

        binding.sosLinearLayout.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("mp_action", "{\"action\":\"SOS\"}")

            viewModel.onSOSClick()
        }

        binding.policiesLinearLayout.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("mp_action", "{\"action\":\"Полисы\"}")

            viewModel.onPoliciesClick()
        }

        binding.productsLinearLayout.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("mp_action", "{\"action\":\"Продукты\"}")

            viewModel.onProductsClick()
        }

        binding.ordersLinearLayout.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("mp_action", "{\"action\":\"Заказы\"}")

            viewModel.onOrdersClick()
        }

        binding.searchTagsLayout.tagsFlowLayout.children.forEach { view ->
            (view as TextView).let {
                it.setOnDebouncedClickListener {
                    YandexMetrica.reportEvent("mp_tag_service", "{\"service\":\"${it.text}\"}")

                    viewModel.onTagClick(it.text.toString())
                }
            }
        }

        binding.aboutApplicationLayout.aboutServiceTextView.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("mp_about")

            viewModel.onAboutServiceClick()
        }

        binding.searchTagsLayout.searchCatalogCardView.root.setOnDebouncedClickListener {
            viewModel.onSearchClick()
        }

        binding.popularProductsLayout.showAllTextView.setOnDebouncedClickListener {
            viewModel.onShowAllPopularProductsClick()
        }

        GlideApp.with(requireContext())
            .load(R.mipmap.master_male_2)
            .transform(GranularRoundedCorners(0f,0f, 16f.dp(requireContext()), 0f))
            .into(binding.ratingLayout.ratingMasterMale)
        binding.ratingLayout.ratingRecyclerView.adapter = RatingCommentsAdapter(
            onCommentClick = { YandexMetrica.reportEvent("mp_rating") }
        )

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
        binding.mainShimmerLayout.horizontalScrollView.setOnTouchListener { _, _ -> true }
        binding.mainErrorLayout.reloadTextView.setOnDebouncedClickListener {
            viewModel.loadContent()
        }

        binding.popularCategoriesLayout.showAllTextView.setOnDebouncedClickListener {
            viewModel.onShowAllPopularCategoriesClick()
        }

        binding.storiesLayout.newServiceLinearLayout.setOnDebouncedClickListener {
            viewModel.onStoriesNewServiceClick()
        }

        binding.storiesLayout.guaranteeLinearLayout.setOnDebouncedClickListener {
            viewModel.onStoriesGuaranteeClick()
        }

        binding.storiesLayout.supportLinearLayout.setOnDebouncedClickListener {
            viewModel.onStoriesSupportClick()
        }

        subscribe(viewModel.registrationObserver) {
            isAuthorized = it
            binding.loginLinearLayout.goneIf(it)
            if (!it) {
                binding.propertyAvailableLinearLayout.gone()
                binding.noPropertyLinearLayout.gone()
            }
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

        subscribe(viewModel.popularProductsObserver) {
            popularProductsAdapter.setItems(it)
        }

        subscribe(viewModel.popularCategoriesObserver){
            popularCategoriesAdapter.setItems(it)
        }

        subscribe(viewModel.loadingStateObserver) {
            binding.mainShimmerLayout.root.visibleIf(it == BaseViewModel.LoadingState.LOADING)
            binding.mainErrorLayout.root.visibleIf(it == BaseViewModel.LoadingState.ERROR)
            binding.mainContentLayout.visibleIf(it == BaseViewModel.LoadingState.CONTENT)

            requireActivity().findViewById<VeilLayout>(R.id.rootShimmerLayout)?.visibleIf(it == BaseViewModel.LoadingState.LOADING)
            requireActivity().findViewById<View>(R.id.toolbarContentLinearLayout)?.visibleIf(it != BaseViewModel.LoadingState.LOADING)
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