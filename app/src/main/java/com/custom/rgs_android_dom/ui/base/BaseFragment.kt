package com.custom.rgs_android_dom.ui.base

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.activity.setLightStatusBar
import com.custom.rgs_android_dom.utils.hideSoftwareKeyboard
import com.custom.rgs_android_dom.utils.setStatusBarColor
import com.custom.rgs_android_dom.utils.subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.emptyParametersHolder
import java.lang.reflect.ParameterizedType
import kotlin.random.Random
import kotlin.reflect.KClass

abstract class BaseFragment<VM : BaseViewModel, VB: ViewBinding>(layout: Int) : Fragment(layout) {

    private val navigateId: Int = Random.nextInt()
    protected val viewModel: VM by viewModel(clazz = getViewModelKClass(), parameters = getParameters())
    protected val binding: VB by viewBinding(viewBindingClass = getViewBindingJavaClass())

    protected val mediaPlayer = MediaPlayer().apply {
        setVolume(1f, 1f)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor()

        subscribe(viewModel.loadingStateObserver){
            handleState(it)
        }
        subscribe(viewModel.closeObserver) {
            onClose()
            viewModel.disposeAll()
        }
    }

    fun getNavigateId(): Int{
        return navigateId
    }

    open fun getParameters(): ParametersDefinition = {
        emptyParametersHolder()
    }

    open fun onLoading(){}
    open fun onContent(){}
    open fun onError(){}

    open fun onClose(){
        hideSoftwareKeyboard()
        mediaPlayer.stop()
        ScreenManager.back(getNavigateId())
    }

    open fun onVisibleToUser(){
        setStatusBarColor()
    }

    open fun setStatusBarColor(){
        setStatusBarColor(R.color.white)
        requireActivity().setLightStatusBar()
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

    protected fun playTune(name: String, isLoop: Boolean = false) {
        try {
            mediaPlayer.reset()
            val descriptor = requireContext().assets.openFd(name)
            mediaPlayer.setDataSource(
                descriptor.fileDescriptor,
                descriptor.startOffset,
                descriptor.length
            )
            descriptor.close()
            mediaPlayer.prepare()
            mediaPlayer.isLooping = isLoop
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleState(state: BaseViewModel.LoadingState){
        when(state){
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
