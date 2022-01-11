package com.custom.rgs_android_dom.ui.property.document

import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemPropertyDetailDocumentBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class DocumentsViewHolder(
    private val binding: ItemPropertyDetailDocumentBinding,
    private val onItemClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(propertyDoc: PropertyDocument) {
        binding.documentNameTextView.text = propertyDoc.name
        binding.root.setOnDebouncedClickListener {
            onItemClick(propertyDoc.link)
        }
        binding.deleteDocFrameLayout.setOnDebouncedClickListener {
            onDeleteClick(propertyDoc.link)
        }
    }

    companion object {
        fun create(
            itemBinding: ItemPropertyDetailDocumentBinding,
            onItemClick: (String) -> Unit,
            onDeleteClick: (String) -> Unit
        ) =
            DocumentsViewHolder(
                itemBinding,
                onItemClick,
                onDeleteClick
            )
    }
}
