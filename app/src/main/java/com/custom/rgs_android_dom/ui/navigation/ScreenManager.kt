package com.custom.rgs_android_dom.ui.navigation

import android.content.Intent
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.catalog.MainCatalogFragment
import com.custom.rgs_android_dom.ui.demo.DemoFragment
import com.custom.rgs_android_dom.ui.main.MainFragment
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.ui.root.RootFragment
import com.custom.rgs_android_dom.utils.activity.hideSoftwareKeyboard
import com.custom.rgs_android_dom.utils.logException
import java.lang.Exception

object ScreenManager {

    var onBottomSheetChanged: (BaseBottomSheetFragment<*, *>) -> Unit = {}

    private val scopes: MutableList<ScreenScope> = ArrayList()
    private val fragments: HashMap<NavigationMenu, MutableList<BaseFragment<*, *>>> = HashMap()
    private val bottomFragments = ArrayList<BaseBottomSheetFragment<*, *>>()
    private var menuTag: NavigationMenu = NavigationMenu.HOME
    private var activity: AppCompatActivity? = null

    @IdRes
    private var containerId: Int? = null

    @IdRes
    private var bottomContainerId: Int? = null

    private var fragmentsToRestore = mutableListOf<BaseFragment<*,*>>()

    fun init(activity: AppCompatActivity, @IdRes containerId: Int) {
        this.activity = activity
        this.containerId = containerId
    }

    fun initBottomSheet(@IdRes bottomContainerId: Int) {
        this.bottomContainerId = bottomContainerId
    }

    fun resetStackAndShowScreen(
        screen: BaseFragment<*,*>
    ) {
        activity?.let{
            it.hideSoftwareKeyboard()
            it.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            scopes.clear()
            fragments.clear()
            bottomFragments.clear()
            showScreen(screen)
        }
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
        getLastFragmentWithout(emptyList())?.let { transaction.hide(it) }
        transaction.add(container, fragment, menuTag.name)
        transaction.addToBackStack(menuTag.name)
        transaction.commitAllowingStateLoss()
        addFragmentInMap(fragment)
    }

    fun showBottomScreen(fragment: BaseBottomSheetFragment<*, *>) {
        val stack = activity?.supportFragmentManager?.fragments as List<Fragment>
        if (stack.last() is BaseFragment<*,*> && fragments[NavigationMenu.HOME]?.size!! > 1) {
            saveAndClearBaseFragmentsStack(stack)
        }

        val container = bottomContainerId ?: return
        val transaction = beginTransaction() ?: return

        onBottomSheetChanged(fragment)

        transaction.add(container, fragment, menuTag.name)
        transaction.addToBackStack(menuTag.name)
        transaction.commitAllowingStateLoss()
        bottomFragments.add(fragment)
    }

    fun closeCurrentBottomFragment() {
        val transaction = beginTransaction() ?: return
        if (bottomFragments.isNotEmpty()){
            transaction.remove(bottomFragments.last())
            transaction.commitAllowingStateLoss()
            bottomFragments.removeLast()
            if (bottomFragments.isNotEmpty()){
                val currentBottomSheetFragment = bottomFragments.last()
                onBottomSheetChanged(currentBottomSheetFragment)
                if (currentBottomSheetFragment is MainFragment || currentBottomSheetFragment is MainCatalogFragment ) {
                    if (fragmentsToRestore.isNotEmpty()) {
                        restoreBaseFragmentsStack()
                    }
                }
            }
        }
    }

    fun showScreenScope(fragment: BaseFragment<*, *>, scopeId: Int) {
        val container = containerId ?: return
        val transaction = beginTransaction() ?: return
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        getLastFragmentWithout(emptyList())?.let { transaction.hide(it) }
        transaction.add(container, fragment)
        transaction.commitNow()
        addFragmentInScope(fragment, scopeId)
    }

    fun closeScope(scopeId: Int) {
        val scope = scopes.filter { it.id == scopeId }
        if (scope.isEmpty()) {
            return
        } else {
            val transaction = beginTransaction() ?: return
            val removed = ArrayList<Fragment>()
            scope.reversed().forEach {
                transaction.remove(it.fragment)
                removed.add(it.fragment)
            }
            getLastFragmentWithout(removed)?.let {
                transaction.show(it)
            }
            transaction.commitNow()
            scopes.removeIf { it.id == scopeId }
        }
        reInit()
        notifyCurrentVisibleFragment()
    }

    //todo не проверен
    fun closeScreenToId(scopeId: Int, fragmentId: Int) {
        val scope = scopes.filter { it.id == scopeId }
        if (scope.isEmpty()) {
            return
        } else {
            val transaction = beginTransaction() ?: return
            val removed = ArrayList<BaseFragment<*, *>>()
            scope.reversed().forEach {
                if (fragmentId == it.fragment.getNavigateId()) {
                    return@forEach
                } else {
                    transaction.remove(it.fragment)
                    removed.add(it.fragment)
                }
            }
            getLastFragmentWithout(removed)?.let {
                transaction.show(it)
            }
            transaction.commit()
            scopes.removeIf { it.id == scopeId && it.fragment in removed }
        }
    }

    fun back(idFragment: Int) {
        val transaction = beginTransaction() ?: return
        val removed = ArrayList<Fragment>()
        scopes.forEach {
            if (it.fragment.getNavigateId() == idFragment) {
                transaction.remove(it.fragment)
                removed.add(it.fragment)
            }
        }
        scopes.removeIf { it.fragment.getNavigateId() == idFragment }

        fragments[menuTag]?.forEach {
            if (it.getNavigateId() == idFragment) {
                transaction.remove(it)
                removed.add(it)
            }
        }

        fragments[menuTag]?.removeIf {
            it.getNavigateId() == idFragment
        }

        getLastFragmentWithout(removed)?.let {
            transaction.show(it)
        }

        // TODO Find out why manager is executing transaction
        try {
            transaction.commitNowAllowingStateLoss()
        } catch (e: Exception){
            logException(this, e)
        }

        reInit()
        notifyCurrentVisibleFragment()
    }

    fun launchIntent(intent: Intent){
        try{
            activity?.startActivity(intent)
        } catch (e: Exception){
            logException(this, e)
        }
    }

    fun containsScreen(fragment: Fragment) : Boolean{
        return fragments[menuTag]?.find { it::class.java.canonicalName == fragment::class.java.canonicalName } != null
    }

    fun containsScreen(screenClass: String): Boolean {
        return fragments[menuTag]?.find { it::class.java.canonicalName == screenClass } != null
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

    private fun reInit() {
        if (fragments[menuTag].isNullOrEmpty() && scopes.isEmpty()) {
            showScreen(RootFragment())
            //todo на время дэмо
            //showScreen(getFirstScreenForMenuItem(menuTag))
        }
    }

    private fun beginTransaction(): FragmentTransaction? {
        return activity?.supportFragmentManager?.beginTransaction()
    }

    private fun getFirstScreenForMenuItem(menu: NavigationMenu): BaseFragment<*, *> {
        return when (menu) {
            NavigationMenu.HOME -> RegistrationPhoneFragment()
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

    private fun getLastFragmentWithout(removed: List<Fragment>): Fragment? {
        return activity?.supportFragmentManager?.fragments?.lastOrNull {
            it is BaseFragment<*, *> && it !in removed
        }
    }

    private fun notifyCurrentVisibleFragment() {
        if (activity?.supportFragmentManager?.fragments?.isNotEmpty() == true) {
            activity?.supportFragmentManager?.fragments?.lastOrNull { it is BaseFragment<*, *> }
                ?.let {
                    (it as BaseFragment<*, *>).onVisibleToUser()
                }
        }
    }

    private fun saveAndClearBaseFragmentsStack(stack: List<Fragment>) {
        var size = stack.size
        stack.reversed().forEach {
            if (it is BaseBottomSheetFragment<*,*>) { return }
            if (it is BaseFragment<*,*>){
                if (size-- > 1) {
                    fragmentsToRestore.add(it)
                    back(it.getNavigateId())
                } else { return }
            }
        }
    }

    private fun restoreBaseFragmentsStack() {
        if (fragmentsToRestore.isNotEmpty()) {
            fragmentsToRestore.reversed().forEach {
                showScreen(it)
            }
            fragmentsToRestore.clear()
        }
    }
}
