package com.custom.rgs_android_dom.ui.base

import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.utils.activity.hideKeyboardForced
import com.custom.rgs_android_dom.utils.activity.hideSoftwareKeyboard
import com.custom.rgs_android_dom.utils.hideSoftwareKeyboard
import com.custom.rgs_android_dom.utils.subscribe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.emptyParametersHolder
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseBottomSheetModalFragment<VM : BaseViewModel, VB : ViewBinding>: BottomSheetDialogFragment() {

    companion object {
        const val KEYBOARD_VISIBLE_HEIGHT_DIFF = 350
    }

    protected val viewModel: VM by viewModel(
        clazz = getViewModelKClass(),
        parameters = getParameters()
    )
    protected val binding: VB by viewBinding(
        viewBindingClass = getViewBindingJavaClass(),
        createMethod = CreateMethod.INFLATE
    )

    protected var isKeyboardOpen: Boolean = false

    private lateinit var behavior: BottomSheetBehavior<View>
    private var hasNotifiedAboutInit = false
    private var isKeyboardListenerAttached = false

    abstract val TAG: String

    override fun getTheme(): Int {
        return R.style.BottomSheet
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
        binding.root.setBackgroundResource(R.drawable.rectangle_filled_white_top_radius_24dp)

        if (isFullScreen()){
            expandViewFullScreen()
        }

        subscribe(viewModel.loadingStateObserver) {
            handleState(it)
        }

        subscribe(viewModel.closeObserver) {
            onClose()
        }

        subscribe(viewModel.isKeyboardOpenObserver) {
            isKeyboardOpen = it
        }

    }

    open fun getParameters(): ParametersDefinition = {
        emptyParametersHolder()
    }

    open fun onLoading() {}
    open fun onContent() {}
    open fun onError() {}

    open fun isFullScreen(): Boolean {
        return false
    }

    open fun onClose() {
        requireActivity().hideKeyboardForced()
        dismissAllowingStateLoss()
    }

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

    fun initKeyboardListener(@IdRes id: Int = android.R.id.content) {
        if (!isKeyboardListenerAttached) {
            isKeyboardListenerAttached = true
            val rootView = activity?.findViewById<View>(id)
            rootView?.let {
                it.viewTreeObserver.addOnGlobalLayoutListener {
                    val r = Rect()
                    view?.getWindowVisibleDisplayFrame(r)
                    val heightDiff: Int = it.rootView.height - (r.bottom - r.top)
                    if (heightDiff > KEYBOARD_VISIBLE_HEIGHT_DIFF) {
                        viewModel.onKeyboardOpen()
                    } else {
                        viewModel.onKeyboardClose()
                    }
                }
            }
        }
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

    private fun expandViewFullScreen() {
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        val dialog = dialog as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        val outMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = requireActivity().display
            display?.getRealMetrics(outMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = requireActivity().windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
        }

        dialog.behavior.peekHeight = outMetrics.heightPixels

        sheetContainer.requestLayout()
    }


}
