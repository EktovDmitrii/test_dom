package com.custom.rgs_android_dom.ui.policies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemPolicyBinding
import com.custom.rgs_android_dom.domain.policies.models.PolicyModel
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class PoliciesAdapter(private val onPolicyClick: (String) -> Unit) :
    RecyclerView.Adapter<PoliciesAdapter.PoliciesViewHolder>() {

    private val policies = mutableListOf<PolicyModel>()

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

    fun setItems(policies: List<PolicyModel>) {
        this.policies.addAll(policies)
        notifyDataSetChanged()
    }

    inner class PoliciesViewHolder(
        val binding: ItemPolicyBinding,
        val onPolicyClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: PolicyModel) {
            binding.titleTextView.text = model.name
            binding.durationTextView.text = "Действует с ${model.startsAt} по ${model.expiresAt}"

            binding.root.setOnDebouncedClickListener {
                onPolicyClick(model.id)
            }
        }

    }

}
