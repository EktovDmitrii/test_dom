package com.custom.rgs_android_dom.ui.property.info

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemAddPropertyBinding
import com.custom.rgs_android_dom.databinding.ItemPropertyDownloadedDocumentBinding
import com.custom.rgs_android_dom.domain.property.models.AddDocument
import com.custom.rgs_android_dom.domain.property.models.PropertyDocument
import com.custom.rgs_android_dom.utils.*

class PropertyDocumentsAdapter(private val onAddClick: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isConnected = false
    private var documents = mutableListOf<Any>()

    companion object {
        private const val ITEM_TYPE_PROPERTY_DOCUMENT = 1
        private const val ITEM_TYPE_ADD_DOCUMENT = 2

        private const val COUNT_ITEMS_TO_SHOW = 6
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PropertyDocumentsViewHolder -> {
                holder.bind(documents[position] as PropertyDocument)
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
        return if (documents[position] is PropertyDocument) {
            ITEM_TYPE_PROPERTY_DOCUMENT
        } else {
            ITEM_TYPE_ADD_DOCUMENT
        }
    }

    override fun getItemCount(): Int {
        return documents.size
    }

    fun setItems(files: List<Any>) {
        documents.clear()
        documents.addAll(files.take(COUNT_ITEMS_TO_SHOW))
        documents.add(AddDocument)
        notifyDataSetChanged()
    }

    fun onInternetConnectionChanged(it: Boolean) {
        isConnected = it
        notifyDataSetChanged()
    }

    inner class PropertyDocumentsViewHolder(
        private val binding: ItemPropertyDownloadedDocumentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val textFilesExtensions = listOf("pdf", "txt", "doc", "docx", "rtf")
        private val mediaFilesExtensions = listOf("jpeg", "jpg", "png", "bmp")

        fun bind(model: PropertyDocument) {
            val extension = model.link.extensionFromLink()
            val url = model.link
            val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(8.dp(binding.root.context)))

            binding.progressBarContainerFrameLayout.visible()

            if (textFilesExtensions.contains(extension) ||
                !mediaFilesExtensions.contains(extension)
            ) {
                binding.textFilesPlaceHolderFrameLayout.visible()
                binding.previewImageView.gone()
                if(isConnected) {
                    binding.progressBarContainerFrameLayout.gone()
                }
            } else {
                binding.textFilesPlaceHolderFrameLayout.gone()
                binding.previewImageView.visible()
                GlideApp.with(binding.root)
                    .load(GlideUrlProvider.makeHeadersGlideUrl(url))
                    .listener(object : RequestListener<Drawable> {

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            if(isConnected) {
                                binding.progressBarContainerFrameLayout.gone()
                            }
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            if(isConnected) {
                                binding.progressBarContainerFrameLayout.gone()
                            }
                            return false
                        }
                    })
                    .apply(requestOptions)
                    .into(binding.previewImageView)
            }
            binding.progressBarContainerFrameLayout.goneIf(isConnected)
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
