package com.custom.rgs_android_dom.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.emptyParametersHolder
import kotlin.random.Random


abstract class BaseFragment<VM : BaseViewModel, VB: ViewBinding>(mViewModelClass: Class<VM>) : Fragment() {

    private val navigateId: Int = Random.nextInt()

    fun getNavigateId(): Int{
        return navigateId
    }

    protected val viewModel: VM by viewModel(clazz = mViewModelClass.kotlin, parameters = getParameters())

    protected lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding(inflater, container)
        return binding.root
    }

    open fun getParameters(): ParametersDefinition = {
        emptyParametersHolder()
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
}