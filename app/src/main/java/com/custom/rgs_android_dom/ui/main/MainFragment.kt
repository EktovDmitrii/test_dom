package com.custom.rgs_android_dom.ui.main

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentMainBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.chat.ChatFragment
import com.custom.rgs_android_dom.ui.client.ClientFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.property.info.PropertyInfoFragment
import com.custom.rgs_android_dom.utils.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*


class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>(R.layout.fragment_main) {

    private lateinit var transitionBackground: TransitionDrawable

    private var canTransit = true
    private var canTransitReverse = false

    private val animationSet = AnimationSet(true)

    private var bottomSheetMainFragment: BaseBottomSheetFragment<*, *>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transitionBackground = TransitionDrawable(
            arrayOf(
                ColorDrawable(requireContext().getColor(R.color.primary400)),
                ColorDrawable(requireContext().getColor(R.color.primary700))
            )
        )

        binding.root.background = transitionBackground

        binding.toolbarChatIcon.setOnDebouncedClickListener {
            ScreenManager.showScreen(ChatFragment())
        }

        ScreenManager.initBottomSheet(R.id.bottomContainer)
        ScreenManager.bottomFragmentsUpdate = {
            bottomSheetMainFragment = it
            measureAndShowFragment()
        }
        ScreenManager.showBottomScreen(ClientFragment())
    }

    override fun onStart() {
        super.onStart()
        viewModel.subscribeLogout()
    }

    override fun onStop() {
        super.onStop()
        viewModel.unsubscribeLogout()
    }

    private fun measureAndShowFragment() {
        binding.root.afterMeasured {

            val bottomSheetBehavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from<View>(binding.bottomContainer)
            bottomSheetBehavior.saveFlags = SAVE_ALL
            bottomSheetBehavior.state = STATE_COLLAPSED
            bottomSheetBehavior.peekHeight =
                binding.root.getLocationOnScreen().y - binding.callContainerLinearLayout.getLocationOnScreen().y + 8.dp(
                    requireContext()
                )
            val layoutParams = binding.bottomContainer.layoutParams as ViewGroup.MarginLayoutParams
            when (bottomSheetMainFragment) {
                is ClientFragment -> {
                    layoutParams.topMargin = 92.dp(requireContext())
                }
                is PropertyInfoFragment -> {
                    layoutParams.topMargin = 12.dp(requireContext())
                }
            }
            binding.bottomContainer.layoutParams = layoutParams
            bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    Log.d("BOOTOM", "onStateChanged")
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    Log.d("BOOTOM", "onSlide")
                }

            })



            binding.toolbarLinearLayout.setOnDebouncedClickListener {
                bottomSheetBehavior.state = STATE_COLLAPSED
            }


            bottomSheetBehavior.state = STATE_EXPANDED

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

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary400)
    }

    override fun onVisibleToUser() {
        super.onVisibleToUser()
//        binding.fakeBottomSheetView.visible()
        measureAndShowFragment()
    }

}