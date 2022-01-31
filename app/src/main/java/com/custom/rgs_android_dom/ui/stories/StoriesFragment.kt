package com.custom.rgs_android_dom.ui.stories

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentStoriesBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.setStatusBarColor
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import java.util.*

class StoriesFragment : BaseFragment<StoriesViewModel, FragmentStoriesBinding>(R.layout.fragment_stories) {

    companion object {

        const val TAB_NEW_SERVICE = 0
        const val TAB_GUARANTEE = 1
        const val TAB_SUPPORT = 2

        private const val KEY_TAB = "tab"

        fun newInstance(tab: Int = TAB_NEW_SERVICE): StoriesFragment {
            return StoriesFragment().args {
                putInt(KEY_TAB, tab)
            }
        }
    }

    private val pagerAdapter: StoriesPagerAdapter
        get() = binding.viewPager.adapter as StoriesPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = StoriesPagerAdapter(
            this,
            { binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true) },
            { binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true) },
            { viewModel.onUnderstandClick() })


        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    TAB_SUPPORT -> cancelTimer()
                    else -> {
                        cancelTimer()
                        runTimer()
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

    private var timer: Timer? = null

    private fun runTimer() {
        if (timer == null) {
            timer = Timer()
        }
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
            }
        }, 10000, 10000)
    }

    private fun cancelTimer() {
        if (timer != null) {
            timer?.cancel()
            timer = null
        }
    }

}