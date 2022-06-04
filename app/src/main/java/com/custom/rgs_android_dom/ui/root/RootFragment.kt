package com.custom.rgs_android_dom.ui.root

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.OverScroller
import androidx.core.content.ContextCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRootBinding
import com.custom.rgs_android_dom.domain.chat.models.CallInfoModel
import com.custom.rgs_android_dom.domain.chat.models.CallState
import com.custom.rgs_android_dom.domain.chat.models.MediaOutputType
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.chats.call.media_output_chooser.MediaOutputChooserFragment
import com.custom.rgs_android_dom.ui.main.MainFragment
import com.custom.rgs_android_dom.ui.managers.MSDNotificationManager
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.NavigationScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.yandex.metrica.YandexMetrica


class RootFragment : BaseFragment<RootViewModel, FragmentRootBinding>(R.layout.fragment_root) {

    private var bottomSheetInited = false

    private var bottomSheetMainFragment: BaseBottomSheetFragment<*, *>? = null
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var scroller: OverScroller? = null
    private var peekHeight: Int? = null

    private var canTransit = true
    private var canTransitReverse = false
    private var isChatEventLogged = false

    private var callInfo: CallInfoModel? = null

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
                    YandexMetrica.reportEvent("mp_menu", "{\"menu_item\":\"Главная\"}")

                    viewModel.onMainClick()
                }
                NavigationScope.NAV_CATALOG -> {
                    YandexMetrica.reportEvent("mp_menu", "{\"menu_item\":\"Каталог\"}")

                    viewModel.onCatalogueClick()
                }
                NavigationScope.NAV_CHATS -> {
                    YandexMetrica.reportEvent("mp_menu", "{\"menu_item\":\"Чат\"}")

                    viewModel.onChatsClick()
                }
                NavigationScope.NAV_LOGIN -> {
                    YandexMetrica.reportEvent("mp_menu", "{\"menu_item\":\"Войти\"}")

                    viewModel.onLoginClick()
                }
                NavigationScope.NAV_PROFILE -> {
                    YandexMetrica.reportEvent("mp_menu", "{\"menu_item\":\"Профиль\"}")

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
            YandexMetrica.reportEvent("master_online_connection", "{\"connection\":\"Чат\"}")
            viewModel.onChatClick()
        }

        binding.chatCallLinearLayout.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("master_online_connection", "{\"connection\":\"Звонок\"}")
            viewModel.onChatCallClick()
        }

        binding.chatVideoCallLinearLayout.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("master_online_connection", "{\"connection\":\"Видео\"}")
            viewModel.onChatVideoCallClick()
        }

        binding.actionsChatsTextView.setOnDebouncedClickListener {
            viewModel.onChatsClick()
        }

        binding.maximizeImageView.setOnDebouncedClickListener {
            if (callInfo != null && callInfo?.state != CallState.ENDED ){
                viewModel.onMaximizeCallClick()
            }
        }

        binding.callInfoFrameLayout.setOnDebouncedClickListener {
            if (callInfo != null && callInfo?.state != CallState.ENDED ){
                viewModel.onMaximizeCallClick()
            }
        }

        binding.mediaOutputImageView.setOnDebouncedClickListener {
            if (callInfo != null && callInfo?.state != CallState.ENDED ){
                val mediaOutputChooserFragment = MediaOutputChooserFragment()
                mediaOutputChooserFragment.show(childFragmentManager, mediaOutputChooserFragment.TAG)
            }
        }

        subscribe(viewModel.navScopesVisibilityObserver){ scopes->
            scopes.forEach {
                binding.bottomNavigationView.setNavigationScopeVisible(it.first, it.second)
            }
        }

        subscribe(viewModel.isUserAuthorizedObserver){
            binding.actionsChatsTextView.text = if (it) {
                TranslationInteractor.getTranslation("app.menu.background_page.bottom_button_client.title")
            }
            else {
                TranslationInteractor.getTranslation("app.menu.background_page.bottom_button_quest.description")
            }
            binding.guestPhoneCallLinearLayout.goneIf(it)
            binding.phoneCallLinearLayout.visibleIf(it)
        }

        subscribe(viewModel.unreadPostsObserver){
            binding.bottomNavigationView.updateUnreadPostsCount(it)
        }

        subscribe(viewModel.callInfoObserver) { callInfo ->
            updateCallInfoUI(callInfo)
        }

        subscribe(viewModel.mediaOutputObserver){
            when (it){
                MediaOutputType.PHONE,
                MediaOutputType.WIRED_HEADPHONE -> {
                    binding.mediaOutputImageView.setImageResource(R.drawable.ic_phone_call_24px)
                }
                MediaOutputType.SPEAKERPHONE -> {
                    binding.mediaOutputImageView.setImageResource(R.drawable.ic_speaker_24px)
                }
                MediaOutputType.BLUETOOTH -> {
                    binding.mediaOutputImageView.setImageResource(R.drawable.ic_bluetooth_24px)
                }
            }
        }

    }

    override fun setStatusBarColor() {
        val statusBarColor = if (callInfo != null){
            when (callInfo?.state){
                CallState.CONNECTING -> {
                    R.color.secondary900
                }
                CallState.ACTIVE -> {
                    R.color.success500
                }
                CallState.ENDED -> {
                    R.color.error500
                }
                CallState.ERROR -> {
                    R.color.secondary900
                }
                else -> {
                    R.color.primary400
                }
            }
        } else {
            R.color.primary400
        }
        setStatusBarColor(statusBarColor)
    }

    override fun onVisibleToUser() {
        super.onVisibleToUser()
        updateToolbarState()
        measureAndShowFragment()
        updateNavigationView()
    }

    private fun makePhoneCall(){
        YandexMetrica.reportEvent("master_online_connection", "{\"connection\":\"8-800\"}")

        val phoneCallIntent = Intent(Intent.ACTION_DIAL)
        phoneCallIntent.data = Uri.parse("tel:88006004358")
        phoneCallIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        requireContext().startActivity(phoneCallIntent)
    }

    private fun measureAndShowFragment() {
        try {
            binding.root.afterMeasured {
                bottomSheetBehavior = from<View>(binding.bottomContainer)

                if (bottomSheetMainFragment is ChatFragment) {
                    bottomSheetBehavior?.isDraggable = false
                } else {
                    bottomSheetBehavior?.isDraggable = callInfo == null || callInfo?.state == CallState.IDLE
                }

                scroller = bottomSheetBehavior?.getViewDragHelper()?.getScroller()

                bottomSheetBehavior?.addBottomSheetCallback(bottomSheetCallback)

                val bottomSheetTopPadding = if (callInfo != null){
                    binding.toolbarLinearLayout.height
                } else {
                    if (bottomSheetMainFragment is ChatFragment){ 8.dp(requireContext()) } else { binding.toolbarLinearLayout.height }
                }

                if (peekHeight == null){
                    peekHeight = binding.root.getLocationOnScreen().y - binding.actionsLinearLayout.getLocationOnScreen().y + bottomSheetTopPadding
                }

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
        } catch (e: Exception){
            logException(this, e)
        }

    }

    private fun onSlideStateChanged(newState: SlideState) {
        if (bottomSheetBehavior?.isDraggable == false){
            return
        }
        when (newState) {
            SlideState.TOP -> {
                updateToolbarState()
                isChatEventLogged = false
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
                if (!isChatEventLogged) {
                    YandexMetrica.reportEvent("master_online_open")
                    isChatEventLogged = true
                }
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
            binding.toolbarLinearLayout.visibleIf(callInfo == null)
            binding.callInfoFrameLayout.visibleIf(callInfo != null)
        }
    }

    private fun updateCallInfoUI(callInfo: CallInfoModel){
        if (callInfo.state == CallState.IDLE){
            this.callInfo = null
        } else {
            this.callInfo = callInfo
        }

        binding.actionsLinearLayout.gone()
        binding.toolbarLinearLayout.gone()
        binding.callInfoFrameLayout.visible()
        binding.signalImageView.gone()

        when (callInfo.state){
            CallState.CONNECTING -> {
                binding.contentFrameLauout.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.secondary900)
                )
                if (isVisible){
                    setStatusBarColor(R.color.secondary900)
                }
                binding.callSubtitleTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary400))
                binding.callTitleTextView.text = TranslationInteractor.getTranslation("app.chats.chat.call.connecting")
                binding.callSubtitleTextView.text = TranslationInteractor.getTranslation("app.chats.chat.call.waiting_operator")
            }
            CallState.ACTIVE -> {
                binding.contentFrameLauout.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.success500)
                )
                if (isVisible){
                    setStatusBarColor(R.color.success500)
                }

                binding.callSubtitleTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_alpha80))

                binding.callTitleTextView.text = TranslationInteractor.getTranslation("app.call.outgoing.default_consultant_name")
                binding.callSubtitleTextView.text = callInfo.duration?.toReadableTime()
            }
            CallState.ENDED -> {
                binding.contentFrameLauout.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.error500)
                )
                if (isVisible){
                    setStatusBarColor(R.color.error500)
                }

                binding.callSubtitleTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_alpha80))

                binding.callTitleTextView.text = TranslationInteractor.getTranslation("app.call.outgoing.default_consultant_name")
                binding.callSubtitleTextView.text = TranslationInteractor.getTranslation("app.chats.chat.call.call_ended")
                playTune()
            }
            CallState.ERROR -> {
                binding.contentFrameLauout.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.secondary900)
                )
                if (isVisible){
                    setStatusBarColor(R.color.secondary900)
                }

                binding.callSubtitleTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary400))

                binding.callTitleTextView.text = TranslationInteractor.getTranslation("app.chats.chat.call.connecting")
                binding.callSubtitleTextView.text = TranslationInteractor.getTranslation("app.chats.chat.call.connection_error")
                binding.signalImageView.visible()
            }
            CallState.IDLE -> {
                binding.toolbarLinearLayout.visible()
                binding.callInfoFrameLayout.gone()

                binding.contentFrameLauout.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.primary400)
                )
                if (isVisible){
                    setStatusBarColor(R.color.primary400)
                }
                measureAndShowFragment()
            }
        }
    }

    private fun playTune() {
        try {
            val mediaPlayer = MediaPlayer()
            val descriptor = requireContext().assets.openFd("track5.mp3")
            mediaPlayer.setDataSource(
                descriptor.fileDescriptor,
                descriptor.startOffset,
                descriptor.length
            )
            descriptor.close()
            mediaPlayer.prepare()
            mediaPlayer.setVolume(1f, 1f)
            mediaPlayer.isLooping = false
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    enum class SlideState { MOVING_TOP, TOP, MOVING_BOTTOM, BOTTOM }
}