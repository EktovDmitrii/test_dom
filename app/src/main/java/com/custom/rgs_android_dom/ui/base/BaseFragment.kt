package com.custom.rgs_android_dom.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.emptyParametersHolder
import java.lang.reflect.ParameterizedType
import kotlin.random.Random
import kotlin.reflect.KClass

abstract class BaseFragment<VM : BaseViewModel, VB: ViewBinding>(layout: Int) : Fragment(layout) {

    private val navigateId: Int = Random.nextInt()

    fun getNavigateId(): Int{
        return navigateId
    }

    protected val viewModel: VM by viewModel(clazz = getViewModelKClass(), parameters = getParameters())
    protected val binding: VB by viewBinding(viewBindingClass = getViewBindingJavaClass())

    open fun getParameters(): ParametersDefinition = {
        emptyParametersHolder()
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getViewModelKClass(): KClass<VM> {
        val actualClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        return actualClass.kotlin
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getViewBindingJavaClass(): Class<VB> {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VB>
    }
}