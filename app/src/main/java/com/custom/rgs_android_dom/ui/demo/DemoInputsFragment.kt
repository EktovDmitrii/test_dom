package com.custom.rgs_android_dom.ui.demo

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentDemoInputsBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.formatTo
import com.custom.rgs_android_dom.utils.showDatePicker
import com.custom.rgs_android_dom.views.edit_text.MSDEditText
import com.custom.rgs_android_dom.views.edit_text.MSDLabelEditText
import org.joda.time.LocalDate
import java.util.*

class DemoInputsFragment: BaseFragment<DemoViewModel, FragmentDemoInputsBinding>(R.layout.fragment_demo_inputs) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.labelTextView.setValue("Иванов Иван Русланович")

        binding.labelIconTextView.setIcon(R.drawable.ic_mastercard)
        binding.labelIconTextView.setValue("•••• 3084")

        binding.firstEt.setState(MSDEditText.State.SUCCESS)
        binding.firstEt.setText("Text")
        binding.secondEt.setState(MSDEditText.State.ERROR)
        binding.secondEt.setText("Text")
        binding.thirdEt.setState(MSDEditText.State.DISABLED)
        binding.thirdEt.setText("Text")

        binding.fivthEt.setState(MSDLabelEditText.State.SUCCESS, "Позитивный текст")
        binding.fivthEt.setText("Text")
        binding.sixthEt.setState(MSDLabelEditText.State.ERROR, "Текст ошибки")
        binding.sixthEt.setText("Text")

        binding.iconEditText.setOnIconClickListener {
            showDatePicker(
                minDate = getMinDate(),
                maxDate = Date()
            ){
                val date = it.formatTo()
                binding.iconEditText.setText(date)
            }
        }
    }

    private fun getMinDate(): Date{
        return LocalDate.now().minusYears(18).plusDays(1).toDate()
    }

}