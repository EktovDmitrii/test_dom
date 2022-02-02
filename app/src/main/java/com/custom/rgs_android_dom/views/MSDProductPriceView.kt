package com.custom.rgs_android_dom.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdProductPriceBinding
import com.custom.rgs_android_dom.utils.visibleIf

class MSDProductPriceView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private val binding: ViewMsdProductPriceBinding = ViewMsdProductPriceBinding.inflate(
        LayoutInflater.from(context), this
    )

    var type: PriceType = PriceType.Fixed
        set(value) {
            field = value

            binding.priceValue.visibleIf(type == PriceType.Fixed)
            binding.priceUnfixedGroup.visibleIf(type == PriceType.Unfixed)
            binding.pricePurchasedGroup.visibleIf(type == PriceType.Purchased)
            binding.priceIncludedGroup.visibleIf(type == PriceType.Included)
        }

    init {
        val attrs = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.MSDProductPriceView,
            0,
            0
        )
        type = PriceType.values()[attrs.getInt(R.styleable.MSDProductPriceView_priceType, 0)]
    }

    fun setPrice(price: Int) {
        val priceStr = "$price ₽"
        when (type) {
            PriceType.Fixed  -> binding.priceValue.text = priceStr
            PriceType.Unfixed -> binding.priceUnfixedValue.text = "от $priceStr"
            PriceType.Purchased -> binding.pricePurchasedValue.text = priceStr
            else -> throw IllegalArgumentException("Invalid price type: $type")
        }
    }

    fun setPurchasedDate(date: String) {
        binding.pricePurchasedDate.text = "Оплачено $date"
    }

    enum class PriceType { Fixed, Unfixed, Purchased, Included}
}