package com.custom.rgs_android_dom.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.addTextChangedListener
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdCodeInputBinding
import com.custom.rgs_android_dom.utils.*

class MSDCodeInput @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attributeSet, defStyleAttr) {

    var code: String
        get() = editTextViews.joinToString("") { it.text.toString() }
        set(value) {
            if (value.length == 4) {
                value.forEachIndexed { i, number ->
                    editTextViews[i].setText(number.toString())
                }
            }
        }

    private val binding: ViewMsdCodeInputBinding = ViewMsdCodeInputBinding.inflate(LayoutInflater.from(context), this, true)
    private var onCodeCompleteListener: (String) -> Unit = {}

    private val editTextViews = listOf(
        binding.firstDigitEditText,
        binding.secondDigitEditText,
        binding.thirdDigitEditText,
        binding.fourthDigitEditText
    )

    init {
        editTextViews.first().focus()

        editTextViews.forEachIndexed { i, _ ->

            editTextViews[i].setOnKeyListener { _: View, _: Int, keyEvent: KeyEvent ->
                if (keyEvent.keyCode == KeyEvent.KEYCODE_DEL && keyEvent.action == KeyEvent.ACTION_UP && i != 0) {
                    editTextViews[i - 1].text?.clear()
                    editTextViews[i - 1].focus()
                    editTextViews[i].unFocus()
                    return@setOnKeyListener true
                }
                false
            }

            editTextViews[i].addTextChangedListener {
                if (editTextViews[i].text.toString() != "") {
                    if (i != editTextViews.size -1){
                        editTextViews[i + 1].focus()
                    } else {
                        onCodeCompleteListener(editTextViews.joinToString(""){it.text.toString()})
                    }
                    editTextViews[i].unFocus()
                }
            }
        }

        binding.codeInputLinearLayout.setOnDebouncedClickListener {
            Log.d("MyLog", "On debounced click")
            if (editTextViews.find { it.text.toString().isEmpty() } != null){
                editTextViews.find { it.text.toString().isEmpty() }?.focus()
            } else {
                editTextViews.last().focus()
            }

        }
    }

    fun setOnCodeCompleteListener(onCodeCompleteListener: (String) -> Unit){
        this.onCodeCompleteListener = onCodeCompleteListener
    }

    fun setErrorState(){
        binding.errorTextView.visible()
        editTextViews.forEach {
            it.setBackgroundResource(R.drawable.code_input_background_error)
        }
    }

    fun removeErrorState(){
        binding.errorTextView.gone()
        editTextViews.forEach {
            it.setBackgroundResource(R.drawable.code_input_selector)
        }
    }
}
