package com.custom.rgs_android_dom.ui.stories

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import androidx.viewpager2.widget.ViewPager2
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentStoriesBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import java.util.*


class StoriesFragment : BaseFragment<StoriesViewModel, FragmentStoriesBinding>(R.layout.fragment_stories), TabActionListener {

    companion object {

        const val TAB_NEW_SERVICE = 0
        const val TAB_GUARANTEE = 1
        const val TAB_SUPPORT = 2

        const val DURATION = 10000L

        private const val KEY_TAB = "tab"

        fun newInstance(tab: Int = TAB_NEW_SERVICE): StoriesFragment {
            return StoriesFragment().args {
                putInt(KEY_TAB, tab)
            }
        }
    }

    private val animNewService = prepareAnimation()
    private val animGuarantee = prepareAnimation()
    private var timer: Timer? = null

    private val pagerAdapter: StoriesPagerAdapter
        get() = binding.viewPager.adapter as StoriesPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = StoriesPagerAdapter( this )

        animNewService.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                if (animGuarantee.hasStarted()) {
                    animGuarantee.cancel()
                }
            }

            override fun onAnimationEnd(animation: Animation?) {}

            override fun onAnimationRepeat(animation: Animation?) {}

        })

        animGuarantee.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                if (animNewService.hasStarted()) {
                    animNewService.cancel()
                }

            }

            override fun onAnimationEnd(animation: Animation?) {}

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                requireActivity().runOnUiThread {
                    when (position) {
                        TAB_NEW_SERVICE -> {
                            restartTimer()
                            binding.newServiceProgress.visible()
                            binding.guaranteeProgress.gone()
                            binding.supportProgress.gone()
                            binding.newServiceProgress.startAnimation(animNewService)
                        }
                        TAB_GUARANTEE -> {
                            restartTimer()
                            binding.newServiceProgress.visible()
                            binding.guaranteeProgress.visible()
                            binding.supportProgress.gone()
                            binding.guaranteeProgress.startAnimation(animGuarantee)
                        }
                        TAB_SUPPORT -> {
                            cancelTimer()
                            if (animGuarantee.hasStarted()) {
                                animGuarantee.cancel()
                            }
                            binding.newServiceProgress.visible()
                            binding.guaranteeProgress.visible()
                            binding.supportProgress.visible()
                        }
                    }
                }

            }
        })

        binding.backImageView.setOnDebouncedClickListener {
            cancelTimer()
            viewModel.onBackClick()
        }

        subscribe(viewModel.tabObserver) {
            when (it) {
                TAB_NEW_SERVICE -> {
                    binding.viewPager.currentItem = TAB_NEW_SERVICE
                }
                TAB_GUARANTEE -> {
                    binding.viewPager.setCurrentItem(TAB_GUARANTEE, false)
                }
                TAB_SUPPORT -> {
                    binding.viewPager.setCurrentItem(TAB_SUPPORT, false)
                }
            }
        }

    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getInt(KEY_TAB))
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.isabelline)
    }

    override fun onRightClick() {
        binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
    }

    override fun onLeftClick() {
        binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true)
    }

    override fun onUnderstandClick() {
        viewModel.onUnderstandClick()
    }

    private fun prepareAnimation(
        fromXValue: Float = -1f,
        toXValue: Float = 0f,
        fromYValue: Float = 0f,
        toYValue: Float = 0f
    ): Animation {
        val anim = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, fromXValue, Animation.RELATIVE_TO_PARENT, toXValue,
            Animation.RELATIVE_TO_PARENT, fromYValue, Animation.RELATIVE_TO_PARENT, toYValue
        )
        anim.duration = DURATION
        anim.interpolator = LinearInterpolator()
        return anim
    }

    private fun restartTimer(){
        cancelTimer()
        runTimer()
    }

    private fun runTimer() {
        if (timer == null) {
            timer = Timer()
        }
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
            }
        }, DURATION, DURATION)
    }

    private fun cancelTimer() {
        if (timer != null) {
            timer?.cancel()
            timer = null
        }
    }

}