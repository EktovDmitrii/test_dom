package com.custom.rgs_android_dom.ui.root

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.OverScroller
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRootBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.main.MainFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.NavigationScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*

class RootFragment : BaseFragment<RootViewModel, FragmentRootBinding>(R.layout.fragment_root) {

    private var bottomSheetInited = false

    private var bottomSheetMainFragment: BaseBottomSheetFragment<*, *>? = null
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var scroller: OverScroller? = null
    private var peekHeight: Int? = null

    private var canTransit = true
    private var canTransitReverse = false

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
            if (slideOffset >= 0.71f){
                onSlideStateChanged(SlideState.MOVING_TOP)
            } else if (slideOffset < 0.60f && slideOffset > 0.0f) {
                onSlideStateChanged(SlideState.MOVING_BOTTOM)
            } else if (slideOffset == 0.0f) {
                onSlideStateChanged(SlideState.BOTTOM)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ScreenManager.initBottomSheet(R.id.bottomContainer)

        ScreenManager.onBottomSheetChanged = {fragment->
            bottomSheetMainFragment = fragment
            measureAndShowFragment()
            updateNavigationView()
       }

        ScreenManager.showBottomScreen(MainFragment())

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

        binding.actionsChatLinearLayout.setOnDebouncedClickListener {
            //TODO Add navigation to MyChatScreen
        }

        binding.phoneCallLinearLayout.setOnDebouncedClickListener {
            makePhoneCall()
        }

        binding.guestPhoneCallLinearLayout.setOnDebouncedClickListener {
            makePhoneCall()
        }

        binding.chatLinearLayout.setOnDebouncedClickListener {
            viewModel.onChatClick()
        }

        binding.chatCallLinearLayout.setOnDebouncedClickListener {
            viewModel.onChatCallClick()
        }

        binding.chatVideoCallLinearLayout.setOnDebouncedClickListener {
            viewModel.onChatVideoCallClick()
        }

        subscribe(viewModel.navScopesVisibilityObserver){ scopes->
            scopes.forEach {
                binding.bottomNavigationView.setNavigationScopeVisible(it.first, it.second)
            }
        }

        subscribe(viewModel.navScopeEnabledObserver){
            binding.bottomNavigationView.setNavigationScopeEnabled(it.first, it.second)
        }

        subscribe(viewModel.isUserAuthorizedObserver){
            binding.actionsChatsTextView.text = if (it) {
                "Перейти ко всем чатам"
            }
            else {
                "Войдите, чтобы видеть свои чаты"
            }
            binding.guestPhoneCallLinearLayout.goneIf(it)
            binding.phoneCallLinearLayout.visibleIf(it)
        }

        subscribe(viewModel.unreadPostsObserver){
            binding.bottomNavigationView.updateUnreadPostsCount(it)
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
        updateToolbarState()
        measureAndShowFragment()
        updateNavigationView()
    }

    private fun makePhoneCall(){
        val phoneCallIntent = Intent(Intent.ACTION_DIAL)
        phoneCallIntent.data = Uri.parse("tel:88006004358")
        phoneCallIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        requireContext().startActivity(phoneCallIntent)
    }

    private fun measureAndShowFragment() {

        binding.root.afterMeasured {
            bottomSheetBehavior = from<View>(binding.bottomContainer)
            scroller = bottomSheetBehavior?.getViewDragHelper()?.getScroller()

            bottomSheetBehavior?.addBottomSheetCallback(bottomSheetCallback)

            val bottomSheetTopPadding = if (bottomSheetMainFragment is ChatFragment){ 0 } else { binding.toolbarLinearLayout.height }

            peekHeight = binding.root.getLocationOnScreen().y - binding.actionsLinearLayout.getLocationOnScreen().y + bottomSheetTopPadding
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

    private fun onSlideStateChanged(newState: SlideState) {
        when (newState) {
            SlideState.TOP -> {
                updateToolbarState()
            }
            SlideState.MOVING_TOP -> {
                if (canTransitReverse) {
                    canTransitReverse = false
                    binding.toolbarLinearLayout.fadeVisibility(View.VISIBLE, 300)
                    binding.actionsLinearLayout.fadeVisibility(View.INVISIBLE, 300)
                }
                canTransit = true
            }
            SlideState.MOVING_BOTTOM -> {
                if (canTransit) {
                    binding.toolbarLinearLayout.fadeVisibility(View.GONE, 500)
                    binding.actionsLinearLayout.fadeVisibility(View.VISIBLE, 500)
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
        bottomSheetBehavior?.peekHeight = binding.root.getLocationOnScreen().y
    }

    private fun afterBottomSheetInit() {
        binding.bottomContainer.visible()
        binding.fakeBottomContainer.gone()
        bottomSheetInited = true
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

    private fun updateToolbarState(){
        if (canTransitReverse){
            binding.actionsLinearLayout.invisible()
            binding.toolbarLinearLayout.visible()
        }
    }

    enum class SlideState { MOVING_TOP, TOP, MOVING_BOTTOM, BOTTOM }
}