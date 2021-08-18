package com.custom.rgs_android_dom.utils

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.joda.time.LocalDate
import java.util.*

fun Fragment.showSoftwareKeyboard(view: View) {
    activity?.showKeyboard(view)
}

fun Fragment.showSoftwareKeyboardForced() {
    requireActivity().showKeyboardForced()
}

fun Fragment.hideSoftwareKeyboard() {
    activity?.hideSoftwareKeyboard()
}

fun Fragment.hideSoftwareKeyboard(removeCurrentFocus: Boolean) {
    if (removeCurrentFocus){
        requireActivity().currentFocus?.clearFocus()
    }
    activity?.hideSoftwareKeyboard()
}

fun Fragment.hideSoftwareKeyboard(view: View) = activity?.hideSoftwareKeyboard(view)

fun Fragment.hideSoftwareKeyboard(delay: Long = 300L, action: (() -> Unit)? = null) {
    hideSoftwareKeyboard()

    action?.let { view?.postDelayed(it, delay) }
}

inline fun <T : Fragment> T.args(builder: Bundle.() -> Unit): T {
    arguments = arguments ?: Bundle()
        .apply(builder)
    return this
}

fun Fragment.toast(text: String, length: Int = Toast.LENGTH_SHORT) =
    run { requireActivity().toast(text, length) }

fun Fragment.showDatePicker(
    title: String? = null,
    date: Date? = null,
    minDate: Date? = null,
    maxDate: Date? = null,
    onDateSelect: (LocalDate) -> Unit = {},
) {
    val calendar = Calendar.getInstance()
    if (date != null){
        calendar.time = date
    }
    val dialog = DatePickerDialog(
        requireContext(),
        { _, year, month, day ->

            val newCalendar = Calendar.getInstance()
            newCalendar.set(year, month, day)
            onDateSelect(LocalDate(newCalendar.time))

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )

    if (title != null){
        dialog.setTitle(title)
    }
    if (minDate != null){
        dialog.datePicker.minDate = minDate.time
    }
    if (maxDate != null){
        dialog.datePicker.maxDate = maxDate.time
    }

    dialog.show()
}

fun Fragment.setStatusBarColor(colorRes: Int){
    requireActivity().window.statusBarColor = resources.getColor(colorRes, requireActivity().theme)
}