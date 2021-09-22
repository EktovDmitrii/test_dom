package com.custom.rgs_android_dom.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdGenderSelectorBinding
import com.custom.rgs_android_dom.databinding.ViewMsdYesNoSelectorBinding
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.custom.rgs_android_dom.utils.TranslationHelper
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class MSDYesNoSelector @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr) {

    private var selectionListener: (Selection) -> Unit = {}
    private val binding: ViewMsdYesNoSelectorBinding = ViewMsdYesNoSelectorBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDYesNoSelector, 0, 0)
        attrs.getString(R.styleable.MSDYesNoSelector_translationLabelKey)?.let { translationLabelKey ->
            //TODO Add handling translation logic here
            binding.labelTextView.text = TranslationHelper.getTranslation(translationLabelKey)
        }

        binding.yesTextView.setOnDebouncedClickListener {
            if (binding.yesTextView.isSelected){
                unselectYes()
                selectionListener(Selection.NOTHING_SELECTED)
            } else {
                onYesSelected()
                selectionListener(Selection.YES)
            }
        }

        binding.noTextView.setOnDebouncedClickListener {
            if (binding.noTextView.isSelected){
                unselectNo()
                selectionListener(Selection.NOTHING_SELECTED)
            } else {
                onNoSelected()
                selectionListener(Selection.NO)
            }
        }
    }

    fun setLabel(label: String){
        binding.labelTextView.text = label
    }

    fun setSelectionListener(selectionListener: (Selection) -> Unit){
        this.selectionListener = selectionListener
    }

    fun setSelection(selection: Selection){
        if (selection == Selection.YES){
            onYesSelected()
        } else {
            onNoSelected()
        }
    }

    private fun onYesSelected(){
        binding.yesTextView.isSelected = true
        binding.noTextView.isSelected = false
    }

    private fun onNoSelected(){
        binding.yesTextView.isSelected = false
        binding.noTextView.isSelected= true
    }

    private fun unselectYes(){
        binding.yesTextView.isSelected = false
    }

    private fun unselectNo(){
        binding.noTextView.isSelected = false
    }

    enum class Selection(val selectionString: String?) {
        YES("yes"),
        NO("no"),
        NOTHING_SELECTED(null)
    }

}
