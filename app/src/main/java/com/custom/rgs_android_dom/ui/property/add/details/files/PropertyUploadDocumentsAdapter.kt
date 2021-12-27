package com.custom.rgs_android_dom.ui.property.add.details.files

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.custom.rgs_android_dom.databinding.ItemPropertyUploadDocumentBinding
import com.custom.rgs_android_dom.utils.*
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions

class PropertyUploadDocumentsAdapter(
    private val onRemoveDocument: (Uri) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isConnected = false
    private var propertyUploadDocumentsItems = mutableListOf<Uri>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = propertyUploadDocumentsItems[position]
        (holder as PropertyUploadDocumentsViewHolder).bind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemPropertyUploadDocumentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PropertyUploadDocumentsViewHolder(binding, onRemoveDocument)
    }

    override fun getItemCount(): Int {
        return propertyUploadDocumentsItems.size
    }

    fun setItems(files: List<Uri>) {
        propertyUploadDocumentsItems.clear()
        propertyUploadDocumentsItems.addAll(files)
        notifyDataSetChanged()
    }

    fun onInternetConnectionChanged(it: Boolean) {
        isConnected = it
        notifyDataSetChanged()
    }

    inner class PropertyUploadDocumentsViewHolder(
        private val binding: ItemPropertyUploadDocumentBinding,
        private val onRemoveDocument: (Uri) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val textFilesExtensions = listOf("pdf", "txt", "doc", "docx", "rtf")
        private val mediaFilesExtensions = listOf("jpeg", "jpg", "png", "bmp")

        fun bind(file: Uri) {

            binding.removeImageView.setOnDebouncedClickListener {
                onRemoveDocument(file)
            }

            binding.progressBarContainerFrameLayout.visible()

            val requestOptions = RequestOptions().run {
                transform(
                    CenterCrop(), RoundedCorners(8.dp(binding.root.context))
                )
            }

            val extension = file.convertToFile(binding.root.context)?.extension

            if (textFilesExtensions.contains(extension) ||
                !mediaFilesExtensions.contains(extension)){
                binding.textFilesPlaceHolderFrameLayout.visible()
                binding.previewImageView.gone()
                if(isConnected) {
                    binding.progressBarContainerFrameLayout.gone()
                }
            } else {
                binding.textFilesPlaceHolderFrameLayout.gone()
                binding.previewImageView.visible()
                GlideApp.with(binding.root)
                    .load(file)
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
            if(isConnected){
                binding.progressBarContainerFrameLayout.gone()
            } else {
                binding.progressBarContainerFrameLayout.visible()
            }
        }
    }
}