package com.custom.rgs_android_dom.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdRateStarBinding

class MSDRateView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    var counts: Int = 0
        set(value) {
            field = value

            setStars()
        }

    private val binding: ViewMsdRateStarBinding = ViewMsdRateStarBinding.inflate(
        LayoutInflater.from(context), this
    )

    private fun setStars() {
        binding.starImg.setBackgroundResource(
            if (counts == 5) R.drawable.ic_star_filled
            else R.drawable.ic_star_partly_filled
        )
    }
}