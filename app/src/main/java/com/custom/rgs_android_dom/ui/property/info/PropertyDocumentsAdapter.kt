package com.custom.rgs_android_dom.ui.property.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemAddPropertyBinding
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemPropertyDownloadedDocumentBinding
import com.custom.rgs_android_dom.domain.property.models.AddDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class PropertyDocumentsAdapter(private val onAddClick: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.gone
import com.custom.rgs_android_dom.utils.visible

    private var propertyUploadDocumentsItems = mutableListOf<Any>()

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

    fun setItems(files: List<Any>) {
        propertyUploadDocumentsItems.clear()
        propertyUploadDocumentsItems.addAll(files.take(COUNT_ITEMS_TO_SHOW))
        propertyUploadDocumentsItems.add(AddDocument)
        notifyDataSetChanged()
    }

    inner class PropertyDocumentsViewHolder(
        binding: ItemPropertyDownloadedDocumentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {}
        fun bind(propertyDocument: PropertyDocument) {
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
                    binding.previewImageCardView.gone()
                    binding.textFilesPlaceHolderFrameLayout.visible()
                }
            }
        }
    }

    inner class AddPropertyViewHolder(
        private val binding: ItemAddPropertyBinding,
        private val onAddClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.root.setOnDebouncedClickListener (onAddClick)
        }
    }
}
