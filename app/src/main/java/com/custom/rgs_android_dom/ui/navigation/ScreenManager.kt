package com.custom.rgs_android_dom.ui.navigation

import android.util.Log
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.demo.DemoFragment
import com.custom.rgs_android_dom.ui.demo.DemoRegistrationFlowFragment

object ScreenManager {

    private val scopes: MutableList<ScreenScope> = ArrayList()
    private val fragments: HashMap<NavigationMenu, MutableList<BaseFragment<*, *>>> = HashMap()
    private var menuTag: NavigationMenu = NavigationMenu.HOME
    private var activity: AppCompatActivity? = null

    @IdRes
    private var containerId: Int? = null

    fun init(activity: AppCompatActivity, @IdRes containerId: Int) {
        this.activity = activity
        this.containerId = containerId
    }

    fun setMenu(menu: NavigationMenu) {
        if (fragments[menu].isNullOrEmpty()) {
            showScreen(getFirstScreenForMenuItem(menu))
        } else {
            closeAllFragmentForMenu(menuTag)
            menuTag = menu
            restoreFragmentsForTag(menu)
        }

    }

    fun showScreen(fragment: BaseFragment<*, *>) {
        val container = containerId ?: return
        val transaction = beginTransaction() ?: return
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(container, fragment, menuTag.name)
        transaction.addToBackStack(menuTag.name)
        transaction.commitAllowingStateLoss()
        addFragmentInMap(fragment)
    }

    private fun addFragmentInMap(fragment: BaseFragment<*, *>) {
        if (fragments[menuTag] == null) {
            fragments[menuTag] = ArrayList()
        }
        fragments[menuTag]?.add(fragment)
    }

    private fun addFragmentInScope(fragment: BaseFragment<*, *>, scopeId: Int) {
        scopes.add(ScreenScope(scopeId, fragment))
    }

    fun showScreenScope(fragment: BaseFragment<*, *>, scopeId: Int) {
        val container = containerId ?: return
        val transaction = beginTransaction() ?: return
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(container, fragment)
        transaction.commit()
        addFragmentInScope(fragment, scopeId)
    }

    fun closeScope(scopeId: Int) {
        val scope = scopes.filter { it.id == scopeId }
        if (scope.isEmpty()) {
            return
        } else {
            val transaction = beginTransaction() ?: return
            scope.reversed().forEach {
                transaction.remove(it.fragment)
            }
            transaction.commitNow()
            scopes.removeIf { it.id == scopeId }
        }
        notifyCurrentVisibleFragment()
    }

    //todo не проверен
    fun closeScreenToId(scopeId: Int, fragmentId: Int) {
        val scope = scopes.filter { it.id == scopeId }
        val ids = ArrayList<Int>()
        if (scope.isEmpty()) {
            return
        } else {
            val transaction = beginTransaction() ?: return
            scope.reversed().forEach {
                if (fragmentId == it.fragment.getNavigateId()) {
                    return@forEach
                } else {
                    transaction.remove(it.fragment)
                    ids.add(it.fragment.getNavigateId())
                }
            }
            transaction.commit()
            scopes.removeIf { it.id == scopeId && it.fragment.getNavigateId() in ids }
        }
    }

    fun back(idFragment: Int) {
        val transaction = beginTransaction() ?: return
        scopes.forEach {
            if (it.fragment.getNavigateId() == idFragment) {
                transaction.remove(it.fragment)
            }
        }
        scopes.removeIf { it.fragment.getNavigateId() == idFragment }

        fragments[menuTag]?.forEach {
            if (it.getNavigateId() == idFragment) {
                transaction.remove(it)
            }
        }

        fragments[menuTag]?.removeIf {
            it.getNavigateId() == idFragment
        }

        transaction.commitNow()

        reInit()
        notifyCurrentVisibleFragment()
    }

    private fun reInit() {
        if (fragments[menuTag].isNullOrEmpty()) {
            showScreen(getFirstScreenForMenuItem(menuTag))
        }
    }

    private fun beginTransaction(): FragmentTransaction? {
        return activity?.supportFragmentManager?.beginTransaction()
    }

    private fun getFirstScreenForMenuItem(menu: NavigationMenu): BaseFragment<*, *> {
        return when (menu) {
            NavigationMenu.HOME -> DemoRegistrationFlowFragment()
            NavigationMenu.CATALOG -> DemoFragment()
            NavigationMenu.PROFILE -> DemoFragment()
            NavigationMenu.MENU -> DemoFragment()
        }
    }

    private fun restoreFragmentsForTag(menu: NavigationMenu) {
        val transaction = beginTransaction() ?: return
        val fragmentsStack = fragments[menu]
        if (fragmentsStack.isNullOrEmpty()) {
            val fragment = getFirstScreenForMenuItem(menu)
            transaction.add(fragment, menu.name)
            transaction.addToBackStack(menu.name)
        } else {
            fragmentsStack.forEach { fragment ->
                transaction.add(fragment, menu.name)
            }
        }
        transaction.commit()
    }

    private fun closeAllFragmentForMenu(menu: NavigationMenu) {
        val transaction = beginTransaction() ?: return
        val oldFragmentsStack = fragments[menu]
        if (!oldFragmentsStack.isNullOrEmpty()) {
            oldFragmentsStack.forEach {
                transaction.remove(it)
            }
        }
        transaction.commit()
    }

    private fun notifyCurrentVisibleFragment(){
        Log.d("MyLog", "Notify current visible fragment")
        if (activity?.supportFragmentManager?.fragments?.isNotEmpty() == true){
            Log.d("MyLog", "IS NOT EMPTY")
            activity?.supportFragmentManager?.fragments?.last()?.let {
                Log.d("MyLog", "Class name " + it::class.java.canonicalName)
                (it as BaseFragment<*,*>).onVisibleToUser()
            }
        }
    }

}