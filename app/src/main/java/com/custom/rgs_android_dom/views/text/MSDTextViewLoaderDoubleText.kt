package com.custom.rgs_android_dom.views.text

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdTextViewLoaderBinding
import com.custom.rgs_android_dom.databinding.ViewMsdTextViewLoaderDoubleTextBinding
import com.custom.rgs_android_dom.utils.gone
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visible

class MSDTextViewLoaderDoubleText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private val binding: ViewMsdTextViewLoaderDoubleTextBinding = ViewMsdTextViewLoaderDoubleTextBinding.inflate(LayoutInflater.from(context), this, true)
    private var isComponentEnabled = isEnabled

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDTextViewLoaderDoubleText, 0, 0)

        attrs.getString(R.styleable.MSDTextViewLoaderDoubleText_translationPrimaryTextKey)?.let { primaryTextKey ->
            //TODO Add handling translation logic here
            binding.primaryTextView.text = primaryTextKey
        }

        attrs.getString(R.styleable.MSDTextViewLoaderDoubleText_translationSecondaryTextKey)?.let { secondaryTextKey ->
            //TODO Add handling translation logic here
            binding.secondaryTextView.text = secondaryTextKey
        }
        val enabled = attrs.getBoolean(R.styleable.MSDTextViewLoaderDoubleText_android_enabled, true)
        isEnabled = enabled

    }

    fun setLoading(isLoading: Boolean){
        if (isLoading){
            isEnabled = false
            binding.loadingProgressBar.visible()
            binding.primaryTextView.setTextColor(context.getColor(R.color.secondary200))
            binding.secondaryTextView.setTextColor(context.getColor(R.color.secondary200))

        } else {
            isEnabled = isComponentEnabled
            binding.loadingProgressBar.gone()
            binding.primaryTextView.setTextColor(ContextCompat.getColorStateList(context, R.color.button_filled_primary_double_text_color))
            binding.secondaryTextView.setTextColor(ContextCompat.getColorStateList(context, R.color.button_filled_primary_double_text_color))
        }
    }

    override fun setEnabled(enabled: Boolean) {
        binding.primaryTextView.isEnabled = enabled
        binding.secondaryTextView.isEnabled = enabled
        binding.containerConstraintLayout.isEnabled = enabled
        isComponentEnabled = enabled
    }

    fun setOnDebouncedClickListener(action: () -> Unit) {
        binding.containerConstraintLayout.setOnDebouncedClickListener {
            action.invoke()
        }
    }

    fun setPrimaryText(primaryText: String){
        binding.primaryTextView.text = primaryText
    }

    fun setSecondaryText(secondaryText: String){
        binding.secondaryTextView.text = secondaryText
    }

}
