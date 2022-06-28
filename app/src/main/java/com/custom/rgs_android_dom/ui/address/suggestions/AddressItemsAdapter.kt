package com.custom.rgs_android_dom.ui.address.suggestions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemAddressItemBinding
import com.custom.rgs_android_dom.domain.address.models.AddressItemModel
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visibleIf
import okhttp3.internal.indexOfNonWhitespace

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

            var secondaryText = model.cityName
            if (secondaryText.trim().isNotEmpty()){
                secondaryText = "$secondaryText, "
            }
            secondaryText = "$secondaryText${model.regionName}"

            binding.addressSecondaryTextView.text = secondaryText
            binding.addressSecondaryTextView.visibleIf(secondaryText.trim().isNotEmpty())
            binding.root.setOnDebouncedClickListener {
                onAddressClick(model)
            }
        }
    }
}