package com.custom.rgs_android_dom.ui.root

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.*
import android.widget.OverScroller
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRootBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.main.MainFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.NavigationScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*

class RootFragment : BaseFragment<RootViewModel, FragmentRootBinding>(R.layout.fragment_root) {

    private lateinit var transitionBackground: TransitionDrawable

    private val animationSet = AnimationSet(true)
    private var canTransit = true
    private var canTransitReverse = false
    private var bottomSheetInited = false

    private var bottomSheetMainFragment: BaseBottomSheetFragment<*, *>? = null
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var scroller: OverScroller? = null
    private var peekHeight: Int? = null

    private val bottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            try {
                if (!bottomSheetInited) {
                    scroller?.abortAnimation()
                }
            } catch (e: Throwable) {
                logException(this, e)
            }

            if (newState == STATE_EXPANDED || newState == STATE_HALF_EXPANDED) {
                if (!bottomSheetInited && newState == STATE_EXPANDED) {
                    afterBottomSheetInit()
                }
                onSlideStateChanged(SlideState.TOP)
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            if (slideOffset < 0.49f && slideOffset > 0.0f) {
                onSlideStateChanged(SlideState.MOVING_BOTTOM)
            } else if (slideOffset == 0.0f) {
                onSlideStateChanged(SlideState.BOTTOM)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transitionBackground = TransitionDrawable(
            arrayOf(
                ColorDrawable(requireContext().getColor(R.color.primary400)),
                ColorDrawable(requireContext().getColor(R.color.primary700))
            )
        )

        binding.contentConstraintLayout.background = transitionBackground

        ScreenManager.initBottomSheet(R.id.bottomContainer)

        ScreenManager.onBottomSheetChanged = {fragment->
            bottomSheetMainFragment = fragment
            measureAndShowFragment()
            updateNavigationView()
        }

        ScreenManager.showBottomScreen(MainFragment())

        binding.toolbarChatIcon.setOnDebouncedClickListener {
            viewModel.onChatClick()
        }

        binding.bottomNavigationView.selectNavigationScope(NavigationScope.NAV_MAIN)

        binding.bottomNavigationView.setNavigationChangedListener { navigationItem->
            when (navigationItem){
                NavigationScope.NAV_MAIN -> {
                    viewModel.onMainClick()
                }
                NavigationScope.NAV_CATALOG -> {
                    viewModel.onCatalogueClick()
                }
                NavigationScope.NAV_CHAT -> {
                    viewModel.onChatClick()
                }
                NavigationScope.NAV_LOGIN -> {
                    viewModel.onLoginClick()
                }
                NavigationScope.NAV_PROFILE -> {
                    viewModel.onProfileClick()
                }
            }
        }

        subscribe(viewModel.navScopesVisibilityObserver){ scopes->
            scopes.forEach {
                binding.bottomNavigationView.setNavigationScopeVisible(it.first, it.second)
            }
        }

        subscribe(viewModel.navScopeEnabledObserver){
            binding.bottomNavigationView.setNavigationScopeEnabled(it.first, it.second)
        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.subscribeLogout()
    }

    override fun onStop() {
        super.onStop()
        viewModel.unsubscribeLogout()
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary400)
    }

    override fun onVisibleToUser() {
        super.onVisibleToUser()
        measureAndShowFragment()
        updateNavigationView()
    }

    private fun measureAndShowFragment() {

        binding.root.afterMeasured {
            initAnimations()
            bottomSheetBehavior = from<View>(binding.bottomContainer)
            scroller = bottomSheetBehavior?.getViewDragHelper()?.getScroller()

            bottomSheetBehavior?.addBottomSheetCallback(bottomSheetCallback)

            val bottomSheetTopPadding = binding.toolbarLinearLayout.height

            // TODO Improve this later
                /*when (bottomSheetMainFragment) {
                    is ClientFragment -> {
                        binding.toolbarLinearLayout.height
                    }
                    is MainStubFragment -> {
                        binding.toolbarLinearLayout.height
                    }
                    is MainCatalogFragment -> {
                        binding.toolbarLinearLayout.height
                    }
                    is CatalogSubcategoriesFragment -> {
                        binding.toolbarLinearLayout.height
                    }
                    else -> 0.dp(requireContext())
                }*/

            peekHeight =
                binding.root.getLocationOnScreen().y - binding.callContainerLinearLayout.getLocationOnScreen().y +
                        8.dp(requireContext()) + bottomSheetTopPadding

            binding.bottomContainer.setPadding(0, bottomSheetTopPadding, 0, 0)

            beforeBottomSheetInit()

            binding.toolbarLinearLayout.setOnDebouncedClickListener {
                bottomSheetBehavior?.state = STATE_COLLAPSED
            }

            if (bottomSheetBehavior?.state == STATE_EXPANDED) {
                afterBottomSheetInit()
            } else {
                bottomSheetBehavior?.state = STATE_EXPANDED
            }

        }
    }

    private fun initAnimations() {
        var fadeOut = AlphaAnimation(1f, 0f).apply {
            duration = 300
            interpolator = LinearInterpolator()
            fillAfter = true
        }

        val slideUp = TranslateAnimation(
            0f, 0f,
            0f, -binding.swipeMoreTextView.height.toFloat()
        ).apply {
            duration = 300
            interpolator = LinearInterpolator()
            fillAfter = true
        }

        animationSet.addAnimation(fadeOut)
        animationSet.addAnimation(slideUp)

        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.swipeMoreTextView.invisible()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
    }

    private fun onSlideStateChanged(newState: SlideState) {
        when (newState) {
            SlideState.TOP -> {
                if (canTransitReverse) {
                    transitionBackground.reverseTransition(100)
                    canTransitReverse = false
                }
                binding.swipeMoreTextView.visible()
                canTransit = true
            }
            SlideState.MOVING_BOTTOM -> {
                if (canTransit) {
                    binding.swipeMoreTextView.startAnimation(animationSet)
                    transitionBackground.startTransition(100)
                    canTransit = false
                    canTransitReverse = true
                }
            }
            SlideState.BOTTOM -> {
                canTransitReverse = true
            }
        }
    }

    private fun beforeBottomSheetInit() {
        binding.bottomContainer.invisible()
        binding.fakeBottomContainer.visible()
        bottomSheetInited = false
        binding.callContainerLinearLayout.gone()
        binding.swipeMoreTextView.gone()
        bottomSheetBehavior?.peekHeight = binding.root.getLocationOnScreen().y
    }

    private fun afterBottomSheetInit() {
        binding.bottomContainer.visible()
        binding.fakeBottomContainer.gone()
        bottomSheetInited = true
        binding.callContainerLinearLayout.visible()
        binding.swipeMoreTextView.visible()

        peekHeight?.let {
            bottomSheetBehavior?.peekHeight = it
        }

    }

    private fun updateNavigationView(){
        bottomSheetMainFragment?.let { fragment->
            binding.bottomNavigationView.visibleIf(fragment.isNavigationViewVisible())
            fragment.getNavigationScope()?.let { scope->
                binding.bottomNavigationView.selectNavigationScope(scope)
            }
        }
    }

    enum class SlideState { TOP, MOVING_BOTTOM, BOTTOM }

}