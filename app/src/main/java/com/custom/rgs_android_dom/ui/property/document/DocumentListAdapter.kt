package com.custom.rgs_android_dom.ui.property.document

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemPropertyDetailDocumentBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class DocumentListAdapter(
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<DocumentListAdapter.DocumentsViewHolder>() {

    private var documents = mutableListOf<PropertyDocument>()

    private var isDeleteButtonVisible: Boolean = false

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {
        (holder).bind(documents[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {
        val binding = ItemPropertyDetailDocumentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DocumentsViewHolder(binding, onDeleteClick)
    }

    override fun getItemCount(): Int {
        return documents.size
    }

    fun setItems(files: List<PropertyDocument>, isDeleteButtonVisible: Boolean) {
        this.isDeleteButtonVisible = isDeleteButtonVisible
        documents.clear()
        documents.addAll(files)
        notifyDataSetChanged()
    }

    fun showDeleteButton(
        isDeleteButtonVisible: Boolean
    ){
        this.isDeleteButtonVisible = isDeleteButtonVisible
        notifyDataSetChanged()
    }

    inner class DocumentsViewHolder(
        private val binding: ItemPropertyDetailDocumentBinding,
        private val onDeleteClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(propertyDoc: PropertyDocument) {
            binding.documentNameTextView.text = propertyDoc.name
            binding.root.setOnDebouncedClickListener {
            }
            binding.deleteDocFrameLayout.isVisible = isDeleteButtonVisible

            binding.deleteDocFrameLayout.setOnDebouncedClickListener {
                documents.forEachIndexed { index, propertyDocument ->
                    if (propertyDocument==propertyDoc)
                        onDeleteClick(index)
                }
            }
        }

    }
}
