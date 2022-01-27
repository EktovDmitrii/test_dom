package com.custom.rgs_android_dom.ui.property.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemPropertyDownloadedDocumentBinding
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.gone
import com.custom.rgs_android_dom.utils.visible

class PropertyDocumentsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var propertyUploadDocumentsItems = mutableListOf<PropertyDocument>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PropertyDocumentsViewHolder).bind(propertyUploadDocumentsItems[position])
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
}
