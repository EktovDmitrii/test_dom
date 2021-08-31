package com.custom.rgs_android_dom.ui.main

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.*
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentMainBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.client.ClientFragment
import com.custom.rgs_android_dom.ui.splash.SplashViewModel
import com.custom.rgs_android_dom.utils.*

class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>(R.layout.fragment_main), BaseBottomSheetFragment.BottomSheetEventsListener {

    private lateinit var transitionBackground: TransitionDrawable

    private var canTransit = true
    private var canTransitReverse = false

    private val animationSet = AnimationSet(true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transitionBackground = TransitionDrawable(arrayOf(
            ColorDrawable(requireContext().getColor(R.color.primary400)),
            ColorDrawable(requireContext().getColor(R.color.primary700))
        ))

        binding.root.background = transitionBackground

        measureAndShowFragment()
        Log.d("MAIN", "TRANSLATION TEST ${TranslationHelper.getTranslation("test.9pqfp11z4ibbubacxb7s61eiqo")}")
    }

    override fun onSlideStateChanged(newState: BaseBottomSheetFragment.SlideState){
        when (newState){
            BaseBottomSheetFragment.SlideState.TOP-> {
                if (canTransitReverse){
                    transitionBackground.reverseTransition(100)
                    canTransitReverse = false
                }
                binding.swipeMoreTextView.visible()
                canTransit = true
            }
            BaseBottomSheetFragment.SlideState.MOVING_BOTTOM -> {
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

    override fun onBottomSheetInit() {
        binding.fakeBottomSheetView.gone()
    }

    private fun measureAndShowFragment(){
        binding.root.afterMeasured {

            initAnimations()

            val maxExpandedPercents = ((binding.toolbarLinearLayout.height.toFloat() + binding.swipeMoreTextView.height.toFloat() + 10.dp(requireContext())) * 100) / binding.root.height
            val maxHalfExpandedRatio = 1f - (maxExpandedPercents / 100f)

            val minExpandedPercents = (binding.toolbarLinearLayout.height.toFloat() * 100) / binding.root.height
            val minHalfExpandedRatio = 1f - (minExpandedPercents / 100f)

            val clientFragment = ClientFragment(
                peekHeight = binding.root.getLocationOnScreen().y - binding.callContainerLinearLayout.getLocationOnScreen().y + 8.dp(requireContext()),
                topMargin = binding.toolbarLinearLayout.height,
                maxHalfExpandedRatio = maxHalfExpandedRatio,
                minHalfExpandedRatio = minHalfExpandedRatio
            )
            clientFragment.show(childFragmentManager, clientFragment.TAG)

            binding.toolbarLinearLayout.setOnDebouncedClickListener {
                clientFragment.setHalfExpanded()
            }
        }
    }

    private fun initAnimations(){
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
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.primary400)
    }

    override fun onVisibleToUser() {
        super.onVisibleToUser()
        binding.fakeBottomSheetView.visible()
        measureAndShowFragment()
    }

}