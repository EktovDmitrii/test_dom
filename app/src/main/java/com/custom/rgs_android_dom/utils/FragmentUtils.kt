package com.custom.rgs_android_dom.utils

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.custom.rgs_android_dom.databinding.ItemPopupBinding
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

fun Fragment.openUrl(url: String){
    try{
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(requireActivity(), intent, null)
    } catch (e: Exception){
        logException(this, e)
    }
}

fun Fragment.notification(message: String, duration: Long = 2000){
    val popupBinding = ItemPopupBinding.inflate(LayoutInflater.from(requireContext()), null, false)
    popupBinding.messageTextView.text = message

    val myToast = Toast(requireContext())
    myToast.duration = Toast.LENGTH_LONG
    myToast.view = popupBinding.root

    myToast.show()

//    val popupWindow = PopupWindow(popupBinding.root, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
//    popupWindow.animationStyle = R.style.PopupWindowAnimation
//
//    popupWindow.showAtLocation(window.decorView.rootView, Gravity.CENTER_VERTICAL or Gravity.BOTTOM, 0, 400)
//
//    popupWindow.contentView.bringToFront()
//
//    Handler(Looper.getMainLooper()).postDelayed({
//        popupWindow.dismiss()
//    }, duration)
}

fun Fragment.buildActivityResultRequest(function: (ActivityResult) -> Unit): ActivityResultLauncher<Intent> {
    return this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        function(it)
    }
}