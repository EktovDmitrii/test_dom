package com.custom.rgs_android_dom.ui.policies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemPolicyBinding
import com.custom.rgs_android_dom.domain.policies.models.PolicyShortModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.insertDate
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class PoliciesAdapter(private val onPolicyClick: (String) -> Unit) :
    RecyclerView.Adapter<PoliciesAdapter.PoliciesViewHolder>() {

    private val policies = mutableListOf<PolicyShortModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoliciesViewHolder {
        val binding = ItemPolicyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PoliciesViewHolder(binding, onPolicyClick)
    }

    override fun onBindViewHolder(holder: PoliciesViewHolder, position: Int) {
        holder.bind(policies[position])
    }

    override fun getItemCount(): Int {
        return policies.size
    }

    fun setItems(policies: List<PolicyShortModel>) {
        this.policies.clear()
        this.policies.addAll(policies)
        notifyDataSetChanged()
    }

    inner class PoliciesViewHolder(
        val binding: ItemPolicyBinding,
        val onPolicyClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        //todo replace with translation value
        private val translationTextKey = binding.durationTextView.text.toString()

        fun bind(model: PolicyShortModel) {
            binding.titleTextView.text = model.name
            binding.durationTextView.text = translationTextKey.insertDate(model.startsAt,model.expiresAt)

            GlideApp.with(binding.root.context)
                .load(model.logo?.let { GlideUrlProvider.makeHeadersGlideUrl(it) })
                .transform(RoundedCorners(10.dp(binding.root.context)))
                .into(binding.iconImageView)

            binding.root.setOnDebouncedClickListener {
                onPolicyClick(model.contractId)
            }
        }

    }

}
