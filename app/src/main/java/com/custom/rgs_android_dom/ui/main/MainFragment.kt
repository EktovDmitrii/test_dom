package com.custom.rgs_android_dom.ui.main

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.*
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentMainBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.profile.ProfileFragment
import com.custom.rgs_android_dom.utils.*

class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>(R.layout.fragment_main) {

    private lateinit var transitionBackground: TransitionDrawable

    private var canTransit = true
    private var canTransitReverse = false

    val animationSet = AnimationSet(true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStatusBarColor(R.color.primary400)

        transitionBackground = TransitionDrawable(arrayOf(
            ColorDrawable(requireContext().getColor(R.color.primary400)),
            ColorDrawable(requireContext().getColor(R.color.primary700))
        ))

        binding.root.background = transitionBackground

        binding.root.afterMeasured {

            var fadeOut = AlphaAnimation(1f, 0f).apply {
                duration = 300
                interpolator = LinearInterpolator()
                fillAfter = true
            }

            val slideUp = TranslateAnimation(0f, 0f,
                0f, -binding.swipeMoreTextView.height.toFloat()).apply {
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

            val profileFragment = ProfileFragment(
                peekHeight = binding.root.getLocationOnScreen().y - binding.callContainerLinearLayout.getLocationOnScreen().y + 8.dp(requireContext()),
                topMargin = binding.toolbarLinearLayout.height
            )
            profileFragment.slideStateListener = {
                onSlideStateChanged(it)
            }
            profileFragment.show(childFragmentManager, profileFragment.TAG)
        }
    }

    private fun onSlideStateChanged(state: BaseBottomSheetFragment.SlideState){
        when (state){
            BaseBottomSheetFragment.SlideState.TOP-> {
                if (canTransitReverse){
                    transitionBackground.reverseTransition(100)
                    canTransitReverse = false
                }
                binding.swipeMoreTextView.visible()
                canTransit = true
            }
            BaseBottomSheetFragment.SlideState.QUARTER_HEIGHT_BOTTOM -> {
                if (canTransit){
                    binding.swipeMoreTextView.startAnimation(animationSet)
                    transitionBackground.startTransition(100)
                    canTransit = false
                }
            }
            BaseBottomSheetFragment.SlideState.BOTTOM -> {
                canTransitReverse = true
            }

        }
    }

}