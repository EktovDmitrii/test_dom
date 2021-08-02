package com.custom.rgs_android_dom.utils

import android.graphics.Rect
import android.view.View
import android.view.animation.*

object CrossfadeAnimator {

    private var secondaryFadeIn = AlphaAnimation(0f, 1f).apply {
        duration = 200
        startOffset = 100
        interpolator = LinearInterpolator()
    }
    private var secondaryFadeOut = AlphaAnimation(1f, 0f).apply {
        duration = 200
        startOffset = 100
        interpolator = LinearInterpolator()
    }

    fun crossfade(primaryView: View, secondaryView: View, scrollBounds: Rect){
        if (primaryView.getLocalVisibleRect(scrollBounds)){
            if (secondaryView.alpha == 1.0f && !secondaryFadeOut.hasStarted() && !secondaryFadeOut.hasEnded()){

                secondaryFadeIn.cancel()
                secondaryView.clearAnimation()

                secondaryView.startAnimation(secondaryFadeOut)

                secondaryFadeOut.setAnimationListener(object: Animation.AnimationListener{
                    override fun onAnimationStart(p0: Animation?) {

                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        secondaryView.alpha = 0.0f
                        recreateFadeOut()
                    }

                    override fun onAnimationRepeat(p0: Animation?) {

                    }

                })

            }
        } else {
            if (secondaryView.alpha == 0.0f && !secondaryFadeIn.hasStarted() && !secondaryFadeIn.hasEnded()){

                secondaryFadeOut.cancel()
                secondaryView.clearAnimation()

                secondaryView.startAnimation(secondaryFadeIn)
                secondaryFadeIn.setAnimationListener(object: Animation.AnimationListener{
                    override fun onAnimationStart(p0: Animation?) {

                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        secondaryView.alpha = 1.0f
                        recreateFadeIn()
                    }

                    override fun onAnimationRepeat(p0: Animation?) {

                    }
                })
            }
        }
    }

    private fun recreateFadeIn(){
        secondaryFadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 200
            interpolator = LinearInterpolator()
        }
    }

    private fun recreateFadeOut(){
        secondaryFadeOut = AlphaAnimation(1f, 0f).apply {
            duration = 200
            startOffset = 100
            interpolator = LinearInterpolator()
        }
    }
}