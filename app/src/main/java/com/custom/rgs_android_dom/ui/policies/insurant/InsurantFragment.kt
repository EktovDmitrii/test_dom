package com.custom.rgs_android_dom.ui.policies.insurant

import android.os.Bundle
import android.util.Log
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentInsurantBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.policies.insurant.dialogs.PolicyDialogsFragment
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.views.edit_text.MSDLabelIconEditText
import org.joda.time.LocalDateTime

class InsurantFragment : BaseFragment<InsurantViewModel, FragmentInsurantBinding>(R.layout.fragment_insurant) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.nextTextView.setOnDebouncedClickListener {
            val dialog = PolicyDialogsFragment()
            dialog.show(childFragmentManager, dialog.TAG)
        }

        binding.firstNameEditText.addTextWatcher {
            viewModel.firstNameChanged(it)
        }

        binding.lastNameEditText.addTextWatcher {
            viewModel.lastNameChanged(it)
        }

        binding.middleNameEditText.addTextWatcher {
            viewModel.middleNameChanged(it)
        }

        binding.birthdayEditText.addTextWatcher {
            viewModel.birthdayChanged(it)
        }

        binding.birthdayEditText.setOnIconClickListener {
            hideSoftwareKeyboard()
            showDatePicker(
                maxDate = LocalDateTime.now().minusYears(16).plusDays(-1).toDate(),
                minDate = LocalDateTime.parse("1900-01-01").toDate()
            ) {
                val date = it.formatTo()
                binding.birthdayEditText.setState(MSDLabelIconEditText.State.NORMAL)
                binding.birthdayEditText.setTextFromUser(date)
            }
        }

        subscribe(viewModel.insurantViewStateObserver) {
            Log.d("Syrgashev", ": ")
            binding.nextTextView.isEnabled = it.isNextEnabled
        }

    }
}