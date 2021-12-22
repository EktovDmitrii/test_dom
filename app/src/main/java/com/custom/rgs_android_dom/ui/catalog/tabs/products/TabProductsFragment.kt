package com.custom.rgs_android_dom.ui.catalog.tabs.products

import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentTabCatalogBinding
import com.custom.rgs_android_dom.databinding.FragmentTabProductsBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.TabCatalogViewModel
import com.custom.rgs_android_dom.utils.setStatusBarColor

class TabProductsFragment () : BaseFragment<TabProductsViewModel, FragmentTabProductsBinding>(R.layout.fragment_tab_products) {

    override fun setStatusBarColor(){
        setStatusBarColor(R.color.primary400)
    }

}