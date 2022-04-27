package com.custom.rgs_android_dom.ui.chats.call.media_output_chooser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemMediaOutputItemBinding
import com.custom.rgs_android_dom.domain.chat.models.MediaOutputModel
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visibleIf

class MediaOutputsAdapter(
    private val onMediaOutputClick: (MediaOutputModel) -> Unit = {}
) : RecyclerView.Adapter<MediaOutputsAdapter.MediaOutputHolder>() {

    private var mediaOutputs: List<MediaOutputModel> = listOf()

    override fun onBindViewHolder(holder: MediaOutputHolder, position: Int) {
        holder.bind(mediaOutputs[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaOutputHolder {
        val binding = ItemMediaOutputItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaOutputHolder(binding, onMediaOutputClick)
    }

    override fun getItemCount(): Int {
        return mediaOutputs.size
    }

    fun setItems(mediaOutputs: List<MediaOutputModel>){
        this.mediaOutputs = mediaOutputs
        notifyDataSetChanged()
    }

    inner class MediaOutputHolder(
        private val binding: ItemMediaOutputItemBinding,
        private val onMediaOutputClick: (MediaOutputModel) -> Unit = {}) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: MediaOutputModel) {
            binding.mediaOutputTextView.text = model.name
            binding.isActiveImageView.visibleIf(model.isActive)

            binding.root.setOnDebouncedClickListener {
                onMediaOutputClick(model)
            }
        }
    }
}