package com.custom.rgs_android_dom.ui.chats

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemCaseItemBinding
import com.custom.rgs_android_dom.domain.chat.models.CaseModel
import com.custom.rgs_android_dom.domain.chat.models.CaseSubStatus
import com.custom.rgs_android_dom.utils.*

class CasesAdapter(private val onCaseClick: (CaseModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var cases: List<CaseModel> = emptyList()

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): RecyclerView.ViewHolder {
        val binding = ItemCaseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CaseViewHolder(binding) {
            onCaseClick(it)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CaseViewHolder).bind(cases[position])
    }

    override fun getItemCount(): Int {
        return cases.size
    }

    fun setItems(cases: List<CaseModel>) {
        this.cases = cases
        notifyDataSetChanged()
    }

    inner class CaseViewHolder(
        private val binding: ItemCaseItemBinding,
        private val onCaseClick: (CaseModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val colorMatrix =  ColorMatrix().apply {
            setSaturation(0.0f)
        }
        private val grayscaleFilter = ColorMatrixColorFilter(colorMatrix)

        fun bind(model: CaseModel) {

            binding.nameTextView.text = model.name

            binding.unreadPostsTextView.visibleIf(model.unreadPosts > 0 && !model.isArchived)

            if (model.unreadPosts > 99){
                binding.unreadPostsTextView.text = "99+"
            } else {
                binding.unreadPostsTextView.text = model.unreadPosts.toString()
            }

            binding.statusTextView.gone()

            if (model.name == "Мастер онлайн"){
                GlideApp.with(binding.root.context)
                    .load(R.drawable.ic_call_consultant)
                    .apply(RequestOptions().transform( CenterCrop(), RoundedCorners(16.dp(binding.root.context))))
                    .into(binding.logoImageView)
                binding.reportedAtTextView.gone()
                binding.statusTextView.gone()
            }
            else if (!model.isArchived) {
                model.subtype?.logo?.let {
                    GlideApp.with(binding.root.context)
                        .load(GlideUrlProvider.makeHeadersGlideUrl(it))
                        .apply(
                            RequestOptions().transform(
                                CenterCrop(),
                                RoundedCorners(16.dp(binding.root.context))
                            )
                        )
                        .error(R.drawable.rectangle_filled_secondary_100_radius_16dp)
                        .into(binding.logoImageView)
                }

                binding.logoImageView.colorFilter = null

                binding.reportedAtTextView.visible()
                binding.reportedAtTextView.text = model.reportedAt.formatTo(DATE_PATTERN_DATE_ONLY)

                binding.statusTextView.gone()

            } else {
                model.subtype?.logo?.let {
                    GlideApp.with(binding.root.context)
                        .load(GlideUrlProvider.makeHeadersGlideUrl(it))
                        .apply(
                            RequestOptions().transform(
                                CenterCrop(),
                                RoundedCorners(16.dp(binding.root.context))
                            )
                        )
                        .error(R.drawable.rectangle_filled_secondary_100_radius_16dp)
                        .into(binding.logoImageView)
                }

                binding.logoImageView.colorFilter = grayscaleFilter
                binding.statusTextView.visible()
                binding.reportedAtTextView.gone()

                binding.statusTextView.text = when (model.subStatus){
                    CaseSubStatus.CANCELLED -> {
                        "Отменен"
                    }
                    CaseSubStatus.SOLVED -> {
                        "Выполнен"
                    }
                    CaseSubStatus.DELETED -> {
                        "Отложен"
                    }
                    else -> {
                        ""
                    }
                }
            }

            binding.root.setOnDebouncedClickListener {
                onCaseClick(model)
            }
        }

    }

}