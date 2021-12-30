package com.custom.rgs_android_dom.ui.property.document

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemPropertyDetailDocumentBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class DocumentListAdapter(
    private val onItemClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : androidx.recyclerview.widget.ListAdapter<PropertyDocument, DocumentListAdapter.DocumentsViewHolder>(
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

    private var propertyUploadDocumentsItems = mutableListOf<PropertyDocument>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {
        val binding = ItemPropertyDetailDocumentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DocumentsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return propertyUploadDocumentsItems.size
    }

    inner class DocumentsViewHolder(
        private val binding: ItemPropertyDetailDocumentBinding
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
    }

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {
        val model = propertyUploadDocumentsItems[position]
        holder.bind(model)
    }
}
