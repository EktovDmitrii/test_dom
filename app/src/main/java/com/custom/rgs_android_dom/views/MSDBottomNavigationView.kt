package com.custom.rgs_android_dom.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.ViewMsdBottomNavigationMenuBinding
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.visible
import com.custom.rgs_android_dom.utils.visibleIf

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
        NavigationScope.NAV_CHATS to binding.navChatsConstraintLayout,
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

    fun updateUnreadPostsCount(unreadMessages: Int){
        if (unreadMessages < 100){
            binding.unreadPostsTextView.text = unreadMessages.toString()
            binding.unreadPostsFrameLayout.setBackgroundResource(R.drawable.circle_stroke_1dp_white_solid_primary_500)
        } else {
            binding.unreadPostsTextView.text = "99+"
            binding.unreadPostsFrameLayout.setBackgroundResource(R.drawable.rectangle_stroke_1dp_white_radius_32px_solid_primary_500)
        }
        binding.unreadPostsFrameLayout.visibleIf(unreadMessages > 0)
    }
}

enum class NavigationScope {
    NAV_MAIN,
    NAV_CATALOG,
    NAV_CHATS,
    NAV_LOGIN,
    NAV_PROFILE
}