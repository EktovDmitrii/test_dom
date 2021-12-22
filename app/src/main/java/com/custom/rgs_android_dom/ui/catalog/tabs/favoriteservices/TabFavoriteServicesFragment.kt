package com.custom.rgs_android_dom.ui.catalog.tabs.favoriteservices

import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentTabFavoriteServicesBinding
import com.custom.rgs_android_dom.databinding.FragmentTabProductsBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.setStatusBarColor

class TabFavoriteServicesFragment () : BaseFragment<TabFavoriteServicesViewModel, FragmentTabFavoriteServicesBinding>(R.layout.fragment_tab_favorite_services) {

    override fun setStatusBarColor(){
        setStatusBarColor(R.color.primary400)
    }

}