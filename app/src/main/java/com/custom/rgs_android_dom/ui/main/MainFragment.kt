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
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentMainBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.profile.ProfileFragment
import com.custom.rgs_android_dom.utils.*

class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>(R.layout.fragment_main) {

    private lateinit var transitionBackground: TransitionDrawable

    private var canTransit = true
    private var canTransitReverse = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStatusBarColor(R.color.primary400)

        transitionBackground = TransitionDrawable(arrayOf(
            ColorDrawable(requireContext().getColor(R.color.primary400)),
            ColorDrawable(requireContext().getColor(R.color.primary700))
        ))

        binding.root.background = transitionBackground

        binding.root.afterMeasured {
            val profileFragment = ProfileFragment(
                peekHeight = binding.callContainerLinearLayout.getLocationOnScreen().y,
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
            BaseBottomSheetFragment.SlideState.QUARTER_HEIGHT_TOP -> {
                Log.d("MyLog", "QUARTER HEIGHT TOP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                if (canTransitReverse){
                    Log.d("Mylog", "Making reverse transiotion")
                    transitionBackground.reverseTransition(100)
                    canTransitReverse = false
                }
            }
            BaseBottomSheetFragment.SlideState.QUARTER_HEIGHT_BOTTOM -> {
                if (canTransit){
                    transitionBackground.startTransition(100)
                    canTransit = false
                }
            }
            BaseBottomSheetFragment.SlideState.TOP -> {
                canTransit = true
            }
            BaseBottomSheetFragment.SlideState.BOTTOM -> {
                canTransitReverse = true
            }

        }
    }

}