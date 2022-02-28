package com.custom.rgs_android_dom.ui.property.info

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRequestEditPersonalDataBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class RequestPropertyInfoEditFragment() : BaseBottomSheetFragment<RequestPropertyInfoEditViewModel, FragmentRequestEditPersonalDataBinding>() {

    override val TAG: String = "REQUEST_PROPERTY_INFO_EDIT_FRAGMENT"

    companion object {
        private const val ARG_OBJECT_ID = "ARG_OBJECT_ID"

        fun newInstance(objectId: String) = RequestPropertyInfoEditFragment().args {
            putString(ARG_OBJECT_ID, objectId)
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(ARG_OBJECT_ID)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmTextView.setOnDebouncedClickListener {
            viewModel.onConfirmClick()
        }

        binding.cancelTextView.setOnDebouncedClickListener {
            viewModel.onCancelClick()
        }

        binding.successTextView.setOnDebouncedClickListener {
            viewModel.onSuccessClick()
        }

        subscribe(viewModel.isSuccessTextViewVisibleObserver){isVisible->
            binding.successTextView.isVisible = isVisible
            binding.confirmTextView.isVisible = !isVisible
            binding.cancelTextView.isVisible = !isVisible

            if (isVisible){
                binding.titleTextView.text = "Заявка отправлена.\nОператор свяжется с вами"
                binding.iconImageView.setImageResource(R.drawable.ic_success_question)
            } else {
                binding.titleTextView.text = "Вы действительно\nхотите оставить заявку?"
                binding.iconImageView.setImageResource(R.drawable.ic_question)
            }
        }
    }

    override fun onClose() {
        hideSoftwareKeyboard()
        dismissAllowingStateLoss()
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheet
    }

    override fun onLoading() {
        super.onLoading()
        binding.confirmTextView.setLoading(true)
        binding.cancelTextView.isEnabled = false
    }

    override fun onError() {
        super.onError()
        binding.confirmTextView.setLoading(false)
        binding.cancelTextView.isEnabled = true
    }

    override fun onContent() {
        super.onContent()
        binding.confirmTextView.setLoading(false)
        binding.cancelTextView.isEnabled = true
    }
}