package com.custom.rgs_android_dom.ui.location.suggestions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemAddressItemBinding
import com.custom.rgs_android_dom.domain.location.models.AddressItemModel
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class AddressItemsAdapter(
    private val onAddressClick: (AddressItemModel) -> Unit = {}
) : RecyclerView.Adapter<AddressItemsAdapter.AddressItemViewHolder>() {

    private var addressItems: List<AddressItemModel> = listOf()

    override fun onBindViewHolder(holder: AddressItemViewHolder, position: Int) {
        holder.bind(addressItems[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressItemViewHolder {
        val binding = ItemAddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressItemViewHolder(binding, onAddressClick)
    }

    override fun getItemCount(): Int {
        return addressItems.size
    }

    fun setItems(addressItems: List<AddressItemModel>){
        this.addressItems = addressItems
        notifyDataSetChanged()
    }

    inner class AddressItemViewHolder(
        private val binding: ItemAddressItemBinding,
        private val onAddressClick: (AddressItemModel) -> Unit = {}) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: AddressItemModel) {
            binding.addressPrimaryTextView.text = model.addressString
            binding.addressSecondaryTextView.text = "${model.cityName} ${model.regionName}"
            binding.root.setOnDebouncedClickListener {
                onAddressClick(model)
            }
        }
    }
}