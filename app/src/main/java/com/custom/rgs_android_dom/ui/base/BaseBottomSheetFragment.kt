package com.custom.rgs_android_dom.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.utils.hideSoftwareKeyboard
import com.custom.rgs_android_dom.utils.subscribe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.emptyParametersHolder
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseBottomSheetFragment<VM : BaseViewModel, VB: ViewBinding>(
    private val peekHeight: Int = 0,
    private val topMargin: Int = 0,
    private val maxExpandedHalfRatio: Float = 0f,
    private val minExpandedHalfRatio: Float = 0f,
    private val isFullScreen: Boolean = true
) : BottomSheetDialogFragment() {

    protected val viewModel: VM by viewModel(clazz = getViewModelKClass(), parameters = getParameters())
    protected val binding: VB by viewBinding(viewBindingClass = getViewBindingJavaClass(), createMethod = CreateMethod.INFLATE)

    abstract val TAG: String

    var slideStateListener: (SlideState) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.BottomSheet
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setWindowAnimations(-1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(false)
        binding.root.setBackgroundResource(R.drawable.card_filled_white_radius_24dp)

        initBottomSheet()

        subscribe(viewModel.loadingStateObserver){
            handleState(it)
        }
        subscribe(viewModel.closeObserver) {
            onClose()
        }
    }

    open fun getParameters(): ParametersDefinition = {
        emptyParametersHolder()
    }


    open fun onLoading(){}
    open fun onContent(){}
    open fun onError(){}

    protected abstract fun getSwipeAnchor(): View?

    open fun onClose(){
        hideSoftwareKeyboard()
        dismissAllowingStateLoss()
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

    private fun initBottomSheet(){

        val bottomSheet = binding.root.parent as View
        val behavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(bottomSheet)
        behavior.isDraggable = false

        behavior.peekHeight = peekHeight

        val layoutParams: CoordinatorLayout.LayoutParams = bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.height = CoordinatorLayout.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams

        behavior.isFitToContents = false
        behavior.expandedOffset = topMargin
        behavior.halfExpandedRatio = maxExpandedHalfRatio

        if (isFullScreen){
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }


//        getSwipeAnchor()?.let { swipeAnchor->
//
//            swipeAnchor.setOnTouchListener { view, motionEvent ->
//                view.performClick()
//
//                when (motionEvent.action){
//                    MotionEvent.ACTION_DOWN -> {
//                        behavior.isDraggable = true
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        behavior.isDraggable = false
//
//                    }
//                    MotionEvent.ACTION_CANCEL -> {
//                        behavior.isDraggable = false
//                    }
//                }
//                // We need to set at first screen coords and then pass them to our bottomsheet behavior
//                motionEvent.setLocation(motionEvent.rawX, motionEvent.rawY)
//                behavior.onTouchEvent(bottomSheet.parent as CoordinatorLayout, swipeAnchor, motionEvent)
//                true
//            }
//        }



        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED || newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
                    behavior.halfExpandedRatio = maxExpandedHalfRatio
                    slideStateListener(SlideState.TOP)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset <= 0.5f){
                    behavior.halfExpandedRatio = minExpandedHalfRatio
                }

                if (slideOffset < 0.49f && slideOffset > 0.0f){
                    slideStateListener(SlideState.MOVING_BOTTOM)
                } else if (slideOffset == 0.0f) {
                    slideStateListener(SlideState.BOTTOM)
                }
            }

        })

    }

    private fun setBottomSheetTopMargin(topMargin: Int, bottomSheet: View){
        val layoutParams: CoordinatorLayout.LayoutParams = bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.topMargin = topMargin
        bottomSheet.layoutParams = layoutParams
    }

    enum class SlideState {TOP, MOVING_BOTTOM, BOTTOM}
}