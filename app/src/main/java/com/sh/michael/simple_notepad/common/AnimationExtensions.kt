package com.sh.michael.simple_notepad.common

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

fun View.rotateView(turn: Float = 135f) {
    animate().setDuration(200)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }
        })
        .rotation(rotation + turn)
}

fun View.showInAnimation() {
    visibility = View.VISIBLE
    alpha = 0f
    translationY = height.toFloat()
    animate()
        .setDuration(200)
        .translationY(0f)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }
        })
        .alpha(1f)
        .start()
}

fun View.showOutAnimation() {
    visibility = View.VISIBLE
    alpha = 1f
    translationY = 0f
    animate()
        .setDuration(200)
        .translationY(height.toFloat())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
                super.onAnimationEnd(animation)
            }
        }).alpha(0f)
        .start()
}