package com.custom.rgs_android_dom.ui.property.document.edit_document_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.custom.rgs_android_dom.databinding.FragmentEditDocumentListBottomSheetBinding
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yandex.metrica.YandexMetrica
import java.io.Serializable

class EditDocumentListBottomSheetFragment : BottomSheetDialogFragment() {

    private var editDocumentListListener: EditDocumentListListener? = null

    private val binding: FragmentEditDocumentListBottomSheetBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    companion object {
        const val TAG: String = "EDIT_DOCUMENT_LIST_FRAGMENT"

        fun newInstance(): EditDocumentListBottomSheetFragment =
            EditDocumentListBottomSheetFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is EditDocumentListListener) {
            editDocumentListListener = parentFragment as EditDocumentListListener
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addDocumentTextView.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("profile_docs_action", "{\"object_action\":\"${binding.addDocumentTextView.text}\"}")

            editDocumentListListener?.addDocumentToList()
            dismissAllowingStateLoss()
        }

        binding.editDocumentListTextView.setOnDebouncedClickListener {
            YandexMetrica.reportEvent("profile_docs_action", "{\"object_action\":\"${binding.editDocumentListTextView.text}\"}")

            editDocumentListListener?.changeDeleteButtonVisibility(true)
            dismissAllowingStateLoss()
        }
    }

    interface EditDocumentListListener : Serializable {
        fun changeDeleteButtonVisibility(
            isDeleteButtonVisible: Boolean
        )
        fun addDocumentToList()
    }
}
