package com.custom.rgs_android_dom.ui.chat.files.upload

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.custom.rgs_android_dom.databinding.FragmentUploadFilesBinding
import com.custom.rgs_android_dom.ui.alert.AlertDialogFragment
import com.custom.rgs_android_dom.ui.alert.OnAlertClickListener
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.convertToFile
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe

class UploadFilesFragment() : BaseBottomSheetModalFragment<UploadFilesViewModel, FragmentUploadFilesBinding>(), OnAlertClickListener {

    override val TAG: String = "ADD_FILES_FRAGMENT"

    private val chooseFilesAction =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {uris->
            val files = uris.mapNotNull { uri ->
                uri.convertToFile(requireActivity())
            }
            if (files.isNotEmpty()){
                viewModel.onFilesSelected(files)
            }

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.uploadMediaTextView.setOnDebouncedClickListener {
            chooseFilesAction.launch("image/* video/*")
        }

        binding.uploadFilesTextView.setOnDebouncedClickListener {
            chooseFilesAction.launch("*/*")
        }

        subscribe(viewModel.showInvalidUploadFileMessageObserver){
            val alertDialogFragment = AlertDialogFragment.newInstance(
                message = "Файл не подходит по требованиям, прикрепите другой файл",
                positiveText = "ОК"
            )
            alertDialogFragment.show(parentFragmentManager, AlertDialogFragment.TAG)
        }

    }

}