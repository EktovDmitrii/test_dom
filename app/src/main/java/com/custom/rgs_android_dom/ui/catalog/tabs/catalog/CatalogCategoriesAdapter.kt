package com.custom.rgs_android_dom.ui.catalog.tabs.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemCatalogCategoryBinding
import com.custom.rgs_android_dom.domain.catalog.models.CatalogCategoryModel
import com.custom.rgs_android_dom.domain.catalog.models.CatalogSubCategoryModel
import com.custom.rgs_android_dom.ui.base.BaseViewHolder
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.recycler_view.HorizontalItemDecoration
import com.custom.rgs_android_dom.utils.recycler_view.VerticalItemDecoration

class CatalogCategoriesAdapter(
    private val onSubCategoryClick: (CatalogSubCategoryModel) -> Unit = {},
    private val onAllProductsClick: (CatalogCategoryModel) -> Unit = {}
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    companion object {
        private const val MAX_VISIBLE_SUB_CATEGORIES = 7
        private const val TYPE_CATEGORY_ITEM = 1
    }

    private var catalogCategories: List<Any> = listOf()

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = catalogCategories[position]
        when (holder) {
            is ItemCatalogCategoryViewHolder -> holder.bind(element as CatalogCategoryModel)
            else -> throw IllegalArgumentException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_CATEGORY_ITEM -> {
                val binding = ItemCatalogCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemCatalogCategoryViewHolder(binding, onSubCategoryClick, onAllProductsClick)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (catalogCategories[position]) {
            is CatalogCategoryModel -> TYPE_CATEGORY_ITEM
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    override fun getItemCount(): Int {
        return catalogCategories.size
    }

    fun setItems(items: List<Any>){
        this.catalogCategories = items
        notifyDataSetChanged()
    }

    inner class ItemCatalogCategoryViewHolder(
        private val binding: ItemCatalogCategoryBinding,
        private val onSubCategoryClick: (CatalogSubCategoryModel) -> Unit = {},
        private val onAllProductsClick: (CatalogCategoryModel) -> Unit = {}) : BaseViewHolder<CatalogCategoryModel>(binding.root) {

        override fun bind(item: CatalogCategoryModel) {
            binding.categoryTitleTextView.text = item.title

            var subCategoriesWithBigImage = listOf<CatalogSubCategoryModel>()
            var subCategoriesWithSmallImage = listOf<CatalogSubCategoryModel>()


            if (item.subCategories.size > 3){
                subCategoriesWithBigImage = item.subCategories.subList(0, 3)
                subCategoriesWithSmallImage = item.subCategories.subList(3, item.subCategories.size)
            } else {
                subCategoriesWithSmallImage = item.subCategories
            }

            binding.allProductsConstraintLayout.goneIf(item.subCategories.isEmpty())

            if (subCategoriesWithBigImage.isNotEmpty()){
                val catalogSubCategoriesWithBigImageAdapter = CatalogSubcategoriesWithBigImageAdapter(
                    onSubCategoryClick = onSubCategoryClick
                )

                binding.subcategoriesWithBigImageRecyclerView.adapter = catalogSubCategoriesWithBigImageAdapter
                catalogSubCategoriesWithBigImageAdapter.setItems(subCategoriesWithBigImage)
                
                binding.subcategoriesWithBigImageRecyclerView.visible()
            } else {
                binding.subcategoriesWithBigImageRecyclerView.gone()
            }

            val catalogSubCategoriesWithSmallImageAdapter = CatalogSubcategoriesWithSmallImageAdapter(
                onSubCategoryClick = onSubCategoryClick
            )

            binding.subcategoriesWithSmallImageRecyclerView.adapter = catalogSubCategoriesWithSmallImageAdapter
            catalogSubCategoriesWithSmallImageAdapter.setItems(subCategoriesWithSmallImage)

            binding.allProductsConstraintLayout.setOnDebouncedClickListener {
                onAllProductsClick(item)
            }

        }
    }

}