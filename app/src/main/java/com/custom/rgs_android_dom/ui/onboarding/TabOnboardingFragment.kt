package com.custom.rgs_android_dom.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentTabOnboardingBinding
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setStatusBarColor
import com.custom.rgs_android_dom.utils.visible
import com.custom.rgs_android_dom.utils.visibleIf

class TabOnboardingFragment :
    BaseFragment<TabOnboardingViewModel, FragmentTabOnboardingBinding>(R.layout.fragment_tab_onboarding) {

    companion object {
        private const val ARG_POSITION = "ARG_POSITION"

        fun newInstance(currPosition: Int) = TabOnboardingFragment().args {
            putInt(ARG_POSITION, currPosition)
        }
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.onboarding_background)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val curr = requireArguments().getInt(ARG_POSITION, 0)
        binding.tabTitleTextView.text = when (curr) {
            0 -> TranslationInteractor.getTranslation("app.onboarding.step_one.title_label")
            1 -> TranslationInteractor.getTranslation("app.onboarding.step_two.title_label")
            2 -> TranslationInteractor.getTranslation("app.onboarding.step_three.title_label")
            3 -> TranslationInteractor.getTranslation("app.onboarding.step_four.title_label")
            4 -> TranslationInteractor.getTranslation("app.onboarding.step_five.title_label")
            else -> throw IllegalArgumentException("Wrong argument value: $curr")
        }
        binding.tabGroup.visibleIf(curr == 0)
        binding.tabOneGroup.visibleIf(curr == 1)
        binding.tabTwoGroup.visibleIf(curr == 2)
        binding.tabThreeGroup.visibleIf(curr == 3)
        binding.tabFourImageView.visibleIf(curr == 4)

        val layoutParams =
            (binding.tabTitleTextView.layoutParams as ConstraintLayout.LayoutParams).apply {
                verticalBias = if (curr == 4) 0.2f else 0.5f
            }
        binding.tabTitleTextView.layoutParams = layoutParams
    }

}