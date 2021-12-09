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

class PropertyUploadDocumentsAdapter(private val onRemoveDocument: (Uri) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    fun setItems(files: List<Uri>?) {
        this.propertyUploadDocumentsItems.clear()
        if (files != null) {
            propertyUploadDocumentsItems.addAll(files)
            notifyDataSetChanged()
        }
    }

    inner class PropertyUploadDocumentsViewHolder(
        private val binding: ItemPropertyUploadDocumentBinding,
        private val onRemoveDocument: (Uri) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

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

            GlideApp.with(binding.root)
                .load(file)
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBarContainerFrameLayout.gone()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBarContainerFrameLayout.gone()
                        return false
                    }
                })
                .apply(requestOptions)
                .into(binding.previewImageView)

        }

    }

}