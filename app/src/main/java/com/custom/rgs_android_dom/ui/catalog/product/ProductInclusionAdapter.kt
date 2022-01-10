package com.custom.rgs_android_dom.ui.catalog.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemGridCatalogInfoBinding

class ProductInclusionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var inclusions: List<String> = listOf(
        "Поиск утечек тепла, сквозняков.",
        "Проверка теплых полов и радиато-ров отопления.",
        "ИК диагностика щитового обору-дования на предмет неисправности.",
        "Выявление мостиков холода и зон намокания."
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemGridCatalogInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductFeatureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProductFeatureViewHolder).bind(inclusions[position])
    }

    override fun getItemCount(): Int = inclusions.size

    fun setItems(inclusions: List<String>){
        this.inclusions = inclusions
        notifyDataSetChanged()
    }

    inner class ProductFeatureViewHolder(
        private val binding: ItemGridCatalogInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.title.text = text
            binding.number.text = (absoluteAdapterPosition + 1).toString()
        }
    }
}