package com.custom.rgs_android_dom.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemRatingCommentBinding
import com.custom.rgs_android_dom.domain.main.CommentModel
import com.custom.rgs_android_dom.ui.base.BaseViewHolder
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class RatingCommentsAdapter(
    private val onCommentClick: () -> Unit,
) : RecyclerView.Adapter<BaseViewHolder<CommentModel>>() {

    private var comments: List<CommentModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<CommentModel> {
        val binding = ItemRatingCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemCommentViewHolder(binding, onCommentClick)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<CommentModel>, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    fun setItems(comments: List<CommentModel>){
        this.comments = comments
        notifyDataSetChanged()
    }

    inner class ItemCommentViewHolder(
        private val binding: ItemRatingCommentBinding,
        private val onCommentClick: () -> Unit
    ) : BaseViewHolder<CommentModel>(binding.root) {

        override fun bind(item: CommentModel) {
            binding.nameTextView.text = item.name
            binding.commentTextView.text = item.comment
            binding.starsView.counts = item.rate

            binding.root.setOnDebouncedClickListener { onCommentClick() }
        }

    }

}