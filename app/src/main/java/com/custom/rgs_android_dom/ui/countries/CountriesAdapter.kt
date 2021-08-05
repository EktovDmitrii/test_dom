package com.custom.rgs_android_dom.ui.countries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemCountryBinding
import com.custom.rgs_android_dom.ui.countries.model.CountryPresentationModel
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visibleIf

class CountriesAdapter(
    private val onCountryClick: (CountryPresentationModel) -> Unit = {}
) : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    private var countries: List<CountryPresentationModel> = listOf()

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding, onCountryClick)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    fun setItems(countries: List<CountryPresentationModel>){
        this.countries = countries
        notifyDataSetChanged()
    }

    inner class CountryViewHolder(
        private val binding: ItemCountryBinding,
        private val onCountryClick: (CountryPresentationModel) -> Unit = {}) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CountryPresentationModel) {
            binding.flagImageView.setImageResource(model.image)
            binding.countryTextView.text = "${model.name} (${model.numberCode})"
            binding.isSelectedImageView.visibleIf(model.isSelected)
            binding.countryTextView.isSelected = model.isSelected

            binding.root.setOnDebouncedClickListener {
                onCountryClick(model)
            }
        }

    }
}