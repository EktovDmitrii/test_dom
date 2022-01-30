package com.custom.rgs_android_dom.ui.property.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemAddPropertyBinding
import com.custom.rgs_android_dom.databinding.ItemPropertyDownloadedDocumentBinding
import com.custom.rgs_android_dom.domain.property.models.AddDocument
import com.custom.rgs_android_dom.domain.property.models.IPropertyDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class PropertyDocumentsAdapter(private val onAddClick: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var propertyUploadDocumentsItems = mutableListOf<IPropertyDocument>()

    companion object {
        private const val ITEM_TYPE_PROPERTY_DOCUMENT = 1
        private const val ITEM_TYPE_ADD_DOCUMENT = 2

        private const val COUNT_ITEMS_TO_SHOW = 6
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PropertyDocumentsViewHolder -> {
                holder.bind()
            }
            is AddPropertyViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == ITEM_TYPE_PROPERTY_DOCUMENT) {
            val binding = ItemPropertyDownloadedDocumentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false )
            PropertyDocumentsViewHolder(binding)
        } else {
            val binding = ItemAddPropertyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false )
            AddPropertyViewHolder(binding, onAddClick)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (propertyUploadDocumentsItems[position] is PropertyDocument) {
            ITEM_TYPE_PROPERTY_DOCUMENT
        } else {
            ITEM_TYPE_ADD_DOCUMENT
        }
    }

    override fun getItemCount(): Int {
        return propertyUploadDocumentsItems.size
    }

    fun setItems(files: List<PropertyDocument>) {
        propertyUploadDocumentsItems.clear()
        propertyUploadDocumentsItems.addAll(files.take(COUNT_ITEMS_TO_SHOW))
        propertyUploadDocumentsItems.add(AddDocument)
        notifyDataSetChanged()
    }

    inner class PropertyDocumentsViewHolder(
        private val binding: ItemPropertyDownloadedDocumentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {}
    }

    inner class AddPropertyViewHolder(
        private val binding: ItemAddPropertyBinding,
        private val onAddClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.root.setOnDebouncedClickListener { onAddClick() }
        }
    }
}
