package com.custom.rgs_android_dom.ui.catalog.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemGridCatalogInfoBinding
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class ProductInclusionAdapter(
    private val onServiceClick: () -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var inclusions: List<String> = listOf(
        "Поиск утечек тепла, сквозняков.",
        "Проверка теплых полов и радиаторов отопления.",
        "ИК диагностика щитового оборудования на предмет неисправности.",
        "Выявление мостиков холода и зон намокания."
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemGridCatalogInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductFeatureViewHolder(binding, onServiceClick)
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
        private val binding: ItemGridCatalogInfoBinding,
        private val onServiceClick: () -> Unit = {}
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.titleTextView.text = text
            binding.numberTextView.text = (absoluteAdapterPosition + 1).toString()

            binding.layout.setOnDebouncedClickListener {
                onServiceClick()
            }
        }
    }
}