package com.custom.rgs_android_dom.ui.stories

import android.os.Bundle
import android.util.Log
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


class StoriesFragment : BaseFragment<StoriesViewModel, FragmentStoriesBinding>(R.layout.fragment_stories),
    TabActionListener {

    companion object {

        const val TAB_NEW_SERVICE = 0
        const val TAB_GUARANTEE = 1
        const val TAB_SUPPORT = 2

        const val DURATION = 10000L

        private const val KEY_TAB = "tab"

        private var isRightClick = false
        private var isLeftClick = false
        private var isSwipe = false

        fun newInstance(tab: Int = TAB_NEW_SERVICE): StoriesFragment {
            return StoriesFragment().args {
                putInt(KEY_TAB, tab)
            }
        }
    }

    private val animNewService = prepareAnimation()
    private val animGuarantee = prepareAnimation()
    private val animSupport = prepareAnimation()

    private val pagerAdapter: StoriesPagerAdapter
        get() = binding.viewPager.adapter as StoriesPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = StoriesPagerAdapter(this)

        animNewService.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                isRightClick = false
                isSwipe = false
            }

            override fun onAnimationEnd(animation: Animation?) {
                if (!isRightClick && !isSwipe && binding.viewPager.currentItem == TAB_NEW_SERVICE) {
                    binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {}

        })

        animGuarantee.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

                isRightClick = false
                isLeftClick = false
                isSwipe = false
            }

            override fun onAnimationEnd(animation: Animation?) {

                if (!isRightClick && !isLeftClick && binding.viewPager.currentItem == TAB_GUARANTEE) {
                    binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        animSupport.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                isLeftClick = false
            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })



        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                isSwipe = when (state) {
                    ViewPager2.SCROLL_STATE_DRAGGING -> true
                    else -> false
                }

            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    TAB_NEW_SERVICE -> {
                        binding.newServiceProgress.visible()
                        binding.guaranteeProgress.gone()
                        binding.supportProgress.gone()
                        if (animGuarantee.hasStarted()) {
                            animGuarantee.cancel()
                        }
                        binding.newServiceProgress.startAnimation(animNewService)

                    }
                    TAB_GUARANTEE -> {
                        binding.newServiceProgress.visible()
                        binding.guaranteeProgress.visible()
                        binding.supportProgress.gone()
                        if (animNewService.hasStarted()) {
                            animNewService.cancel()
                        }
                        if (animSupport.hasStarted()) {
                            animSupport.cancel()
                        }
                        binding.guaranteeProgress.startAnimation(animGuarantee)
                    }
                    TAB_SUPPORT -> {
                        binding.newServiceProgress.visible()
                        binding.guaranteeProgress.visible()
                        binding.supportProgress.visible()
                        if (animGuarantee.hasStarted()) {
                            animGuarantee.cancel()
                        }
                        binding.supportProgress.startAnimation(animSupport)
                    }
                }
            }
        })

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        subscribe(viewModel.tabObserver) {
            when (it) {
                TAB_NEW_SERVICE -> {
                    binding.viewPager.setCurrentItem(TAB_NEW_SERVICE, false)
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
        isRightClick = true
    }

    override fun onLeftClick() {
        binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true)
        isLeftClick = true
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

}