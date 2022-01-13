package com.custom.rgs_android_dom.ui.property.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemPropertyDownloadedDocumentBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument

class PropertyDocumentsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var propertyUploadDocumentsItems = mutableListOf<PropertyDocument>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PropertyDocumentsViewHolder).bind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemPropertyDownloadedDocumentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PropertyDocumentsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return propertyUploadDocumentsItems.size
    }

    fun setItems(files: List<PropertyDocument>) {
        propertyUploadDocumentsItems.clear()
        propertyUploadDocumentsItems.addAll(files)
        notifyDataSetChanged()
    }

    inner class PropertyDocumentsViewHolder(
        private val binding: ItemPropertyDownloadedDocumentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() { }
    }
}
