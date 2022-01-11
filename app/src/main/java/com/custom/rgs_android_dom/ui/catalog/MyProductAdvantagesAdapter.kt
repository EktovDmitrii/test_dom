package com.custom.rgs_android_dom.ui.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemMyProductAdvantageWithNumberBinding

class MyProductAdvantagesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var advantages: List<String> = listOf(
        "310 мастеров готовы к работе",
        "Все работы застрахованы",
        "310 мастеров готовы к работе",
        "Все работы застрахованы"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemMyProductAdvantageWithNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductAdvantageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProductAdvantageViewHolder).bind(advantages[position])
    }

    override fun getItemCount(): Int = advantages.size

    fun setItems(advantages: List<String>){
        this.advantages = advantages
        notifyDataSetChanged()
    }

    inner class ProductAdvantageViewHolder(
        private val binding: ItemMyProductAdvantageWithNumberBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(text: String) {
            binding.iconImageView.text = (absoluteAdapterPosition + 1).toString()
            binding.descriptionTextView.text = text

        }
    }
}