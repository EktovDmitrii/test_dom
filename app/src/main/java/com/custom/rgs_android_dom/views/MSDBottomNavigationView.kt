package com.custom.rgs_android_dom.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import com.custom.rgs_android_dom.databinding.ViewMsdBottomNavigationMenuBinding
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class MSDBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attributeSet, defStyleAttr) {

    private val binding: ViewMsdBottomNavigationMenuBinding = ViewMsdBottomNavigationMenuBinding.inflate(LayoutInflater.from(context), this, true)
    private var navigationChangedListener: (NavigationScope) -> Unit = {}

    private val navigationViews = mapOf(
        NavigationScope.NAV_MAIN to binding.navMainLinearLayout,
        NavigationScope.NAV_CATALOG to binding.navCatalogLinearLayout,
        NavigationScope.NAV_CHAT to binding.navChatLinearLayout,
        NavigationScope.NAV_LOGIN to binding.navLoginLinearLayout,
        NavigationScope.NAV_PROFILE to binding.navProfileLinearLayout
    )

    init {
        navigationViews.forEach{
            it.value.setOnDebouncedClickListener {
                navigationChangedListener(it.key)
                selectNavigationScope(it.key)
            }
        }
    }

    fun selectNavigationScope(navigationScope: NavigationScope){
        navigationViews.values.forEach {navigationView ->
            val isActivated = navigationView  == navigationViews[navigationScope]
            navigationView.children.forEach {
                it.isActivated = isActivated
            }
        }
    }

    fun setNavigationScopeVisible(navigationScope: NavigationScope, isVisible: Boolean){
        navigationViews[navigationScope]?.isVisible = isVisible
    }

    fun setNavigationScopeEnabled(navigationScope: NavigationScope, isEnabled: Boolean){
        navigationViews[navigationScope]?.isEnabled = isEnabled
    }

    fun setNavigationChangedListener(navigationChangedListener: (NavigationScope) -> Unit = {}){
        this.navigationChangedListener = navigationChangedListener
    }
}

enum class NavigationScope {
    NAV_MAIN,
    NAV_CATALOG,
    NAV_CHAT,
    NAV_LOGIN,
    NAV_PROFILE
}