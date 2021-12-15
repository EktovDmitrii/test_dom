package com.custom.rgs_android_dom.ui.catalog

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.ui.catalog.tabs.availableservices.TabAvailableServicesFragment
import com.custom.rgs_android_dom.ui.catalog.tabs.catalog.TabCatalogFragment
import com.custom.rgs_android_dom.ui.catalog.tabs.favoriteservices.TabFavoriteServicesFragment
import com.custom.rgs_android_dom.ui.catalog.tabs.products.TabProductsFragment
import android.widget.TextView

import android.view.LayoutInflater
import android.view.View


class MainCatalogPagerAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {

    val icons = listOf(R.drawable.arrow, R.drawable.arrow, R.drawable.arrow, R.drawable.arrow)
    val titles = listOf("Каталог", "Мои продукты", "Доступные услуги", "Избранные услуги")

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> TabCatalogFragment()
            1 -> TabProductsFragment()
            2 -> TabAvailableServicesFragment()
            3 -> TabFavoriteServicesFragment()
            else -> TabCatalogFragment()
        }
        return fragment
    }

    /*fun getTabView(position: Int): View? {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        val binding = LayoutInflater.from(fragment.requireContext()).inflate(R.layout.tab_catalog, null)
        val tv = v.findViewById(R.id.textView) as TextView
        tv.setText(tabTitles.get(position))
        val img: ImageView = v.findViewById(R.id.imgView) as ImageView
        img.setImageResource(imageResId.get(position))
        return v
    }*/
}