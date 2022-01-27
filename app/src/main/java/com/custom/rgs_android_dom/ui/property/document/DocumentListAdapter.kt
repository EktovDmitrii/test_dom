package com.custom.rgs_android_dom.ui.property.document

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemPropertyDetailDocumentBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.gone
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visible

class DocumentListAdapter(
    private val onDocumentClick: (PropertyDocument) -> Unit,
    private val onDeleteClick: (PropertyDocument) -> Unit
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
        return DocumentsViewHolder(binding, onDocumentClick, onDeleteClick)
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

    fun showDeleteButton(isDeleteButtonVisible: Boolean) {
        this.isDeleteButtonVisible = isDeleteButtonVisible
        notifyDataSetChanged()
    }

    inner class DocumentsViewHolder(
        private val binding: ItemPropertyDetailDocumentBinding,
        private val onDocumentClick: (PropertyDocument) -> Unit,
        private val onDeleteClick: (PropertyDocument) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(propertyDocument: PropertyDocument) {
            binding.documentNameTextView.text = propertyDocument.name
            binding.root.setOnDebouncedClickListener {
                onDocumentClick(propertyDocument)
            }
            binding.deleteDocFrameLayout.isVisible = isDeleteButtonVisible

            binding.deleteDocFrameLayout.setOnDebouncedClickListener {
                onDeleteClick(propertyDocument)
            }

            when (propertyDocument.link.substringAfterLast(".", "missing")) {
                "jpg", "jpeg", "png", "bmp" -> {
                    binding.textFilesPlaceHolderFrameLayout.gone()
                    val documentId = propertyDocument.link.substringAfterLast("/", "missing")
                    val url = "${BuildConfig.BASE_URL}/api/store/${documentId}"

                    GlideApp.with(binding.root.context)
                        .load(GlideUrlProvider.makeHeadersGlideUrl(url))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.previewImageView)
                }
                else -> {
                    binding.previewImageView.gone()
                    binding.textFilesPlaceHolderFrameLayout.visible()
                }
            }

        }

    }
}
