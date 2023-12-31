package com.custom.rgs_android_dom.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.hideSoftwareKeyboard
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.views.NavigationScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.emptyParametersHolder
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseBottomSheetFragment<VM : BaseViewModel, VB : ViewBinding>: BottomSheetDialogFragment() {

    var isLocked = false

    protected val viewModel: VM by viewModel(
        clazz = getViewModelKClass(),
        parameters = getParameters()
    )
    protected val binding: VB by viewBinding(
        viewBindingClass = getViewBindingJavaClass(),
        createMethod = CreateMethod.INFLATE
    )

    private lateinit var behavior: BottomSheetBehavior<View>
    private var hasNotifiedAboutInit = false

    abstract val TAG: String

    override fun getTheme(): Int {
        return getThemeResource()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(false)
        binding.root.setBackgroundResource(R.drawable.rectangle_filled_white_top_radius_24dp)

        subscribe(viewModel.loadingStateObserver) {
            handleState(it)
        }
        subscribe(viewModel.closeObserver) {
            onClose()
        }

    }

    open fun getParameters(): ParametersDefinition = {
        emptyParametersHolder()
    }

    open fun onLoading() {}
    open fun onContent() {}
    open fun onError() {}

    open fun onClose() {
        viewModel.disposeAll()
        hideSoftwareKeyboard()
        ScreenManager.closeCurrentBottomFragment()
    }

    fun openSubScreen(fragment: BaseFragment<*, *>) {
        viewModel.close()
        ScreenManager.showScreen(fragment)
    }

    open fun isNavigationViewVisible(): Boolean {
        return true
    }

    open fun getNavigationScope(): NavigationScope? {
        return null
    }

    abstract fun getThemeResource(): Int

    @Suppress("UNCHECKED_CAST")
    protected fun getViewModelKClass(): KClass<VM> {
        val actualClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        return actualClass.kotlin
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getViewBindingJavaClass(): Class<VB> {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VB>
    }

    private fun handleState(state: BaseViewModel.LoadingState) {
        when (state) {
            BaseViewModel.LoadingState.LOADING -> {
                onLoading()
            }
            BaseViewModel.LoadingState.CONTENT -> {
                onContent()
            }
            BaseViewModel.LoadingState.ERROR -> {
                onError()
            }
        }
    }

}
