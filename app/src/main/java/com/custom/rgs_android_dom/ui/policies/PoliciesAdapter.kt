package com.custom.rgs_android_dom.ui.policies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.ItemPolicyBinding
import com.custom.rgs_android_dom.databinding.ItemPolicyDividerBinding
import com.custom.rgs_android_dom.domain.policies.models.PolicyDividerModel
import com.custom.rgs_android_dom.domain.policies.models.PolicyShortModel
import com.custom.rgs_android_dom.domain.policies.models.PolicyViewholderModel
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.insertDate
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class PoliciesAdapter(private val onPolicyClick: (String, Boolean) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val policies = mutableListOf<PolicyViewholderModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_ACTIVE_POLICY, ITEM_TYPE_ARCHIVE_POLICY -> {
                val binding = ItemPolicyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PoliciesViewHolder(binding, onPolicyClick)
            }
            ITEM_TYPE_DIVIDER_TEXT -> {
                val binding = ItemPolicyDividerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                DividerViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Wrong view type $viewType")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = policies[position]

        when (holder) {
            is PoliciesViewHolder -> holder.bind(model)
            is DividerViewHolder -> holder.bind(model)
        }
    }

    override fun getItemCount(): Int {
        return policies.size
    }

    fun setItems(policies: List<PolicyViewholderModel>) {
        this.policies.clear()
        this.policies.addAll(policies)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (val policy = policies[position]) {
            is PolicyShortModel -> {
                if (policy.expiresAt?.isAfterNow == true) {
                    ITEM_TYPE_ARCHIVE_POLICY
                } else {
                    ITEM_TYPE_ACTIVE_POLICY
                }
            }
            is PolicyDividerModel -> ITEM_TYPE_DIVIDER_TEXT
            else -> throw IllegalArgumentException("Wrong view type for $policy")
        }
    }

    inner class PoliciesViewHolder(
        val binding: ItemPolicyBinding,
        val onPolicyClick: (String, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        //todo replace with translation value
        private val translationTextKey = binding.durationTextView.text.toString()

        fun bind(model: PolicyViewholderModel) {
            val policyModel = model as PolicyShortModel
            if (policyModel.expiresAt?.isAfterNow == true) {
                binding.iconSignImageView.background = ContextCompat.getDrawable(binding.root.context, R.drawable.policy_active_sign)
                binding.rootLayout.background = ContextCompat.getDrawable(binding.root.context, R.drawable.rectangle_filled_submarine_radius_16dp)
            } else {
                binding.iconSignImageView.background = ContextCompat.getDrawable(binding.root.context, R.drawable.policy_archive_sign)
                binding.rootLayout.background = ContextCompat.getDrawable(binding.root.context, R.drawable.rectangle_filled_secondary_300_radius_16dp)
            }

            binding.titleTextView.text = policyModel.name
            binding.durationTextView.text = translationTextKey.insertDate(policyModel.startsAt,policyModel.expiresAt)

            GlideApp.with(binding.root.context)
                .load(policyModel.logo?.let { GlideUrlProvider.makeHeadersGlideUrl(it) })
                .transform(RoundedCorners(10.dp(binding.root.context)))
                .into(binding.iconImageView)

            binding.root.setOnDebouncedClickListener {
                onPolicyClick(policyModel.contractId, policyModel.expiresAt?.isAfterNow == true)
            }
        }
    }

    inner class DividerViewHolder(
        val binding: ItemPolicyDividerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PolicyViewholderModel) {
            val dividerModel = model as PolicyDividerModel
            binding.dividerText.text = dividerModel.text
        }
    }

    companion object {
        private const val ITEM_TYPE_ACTIVE_POLICY = 1
        private const val ITEM_TYPE_ARCHIVE_POLICY = 2
        private const val ITEM_TYPE_DIVIDER_TEXT = 3
    }

}
