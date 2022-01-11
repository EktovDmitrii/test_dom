package com.custom.rgs_android_dom.ui.property.document

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.custom.rgs_android_dom.databinding.ItemPropertyDetailDocumentBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument

class DocumentListAdapter(
    private val onItemClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : androidx.recyclerview.widget.ListAdapter<PropertyDocument, DocumentsViewHolder>(
    object :
        DiffUtil.ItemCallback<PropertyDocument>() {

        override fun areItemsTheSame(
            oldItem: PropertyDocument,
            newItem: PropertyDocument
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: PropertyDocument,
            newItem: PropertyDocument
        ): Boolean = oldItem == newItem
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {
        val binding = ItemPropertyDetailDocumentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DocumentsViewHolder.create(binding, onItemClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
    }
}
