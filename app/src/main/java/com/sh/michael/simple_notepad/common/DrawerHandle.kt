package com.sh.michael.simple_notepad.common

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.Display
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener

/**
 * this is a simplified version of https://github.com/NikolaDespotoski/DrawerLayoutEdgeToggle
 * DrawerHandle is used to attach any tag view to a NavigationView layout, so it'd be visible to a user
 * Use static attach function to configure navigation drawer of the screen it is used on.
 * Attach function will create a DrawerHandle instance and configure the layout with the specified handle layout
 * verticalOffset is specified to set position of a handle on the screen.
 */
class DrawerHandle private constructor(
    private val drawerLayout: DrawerLayout,
    private val drawer: View,
    private val handleLayout: Int,
    private val handleVerticalOffset: Float
) : DrawerListener {

    private val rootView: ViewGroup = drawerLayout.rootView as ViewGroup
    private var defaultOffset = 0f
    private val gravity by lazy { getDrawerViewGravity(drawer) }
    private val windowManager: WindowManager = drawerLayout.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val display: Display = windowManager.defaultDisplay
    private val screenDimensions = Point()
    private val clickListener = View.OnClickListener {
        drawerLayout.run {
            if (isDrawerOpen(gravity)) closeDrawer(gravity) else openDrawer(gravity)
        }
    }

    val view: View by lazy {
        val inflater = drawerLayout.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(handleLayout, rootView, false)
    }

    init {
        view.setOnClickListener(clickListener)
        view.setOnTouchListener(TouchListener())
        rootView.addView(view, FrameLayout.LayoutParams(view.layoutParams.width, view.layoutParams.height, gravity))
        setVerticalOffset(handleVerticalOffset)
        drawerLayout.setDrawerListener(this)
    }

    override fun onDrawerClosed(arg0: View) { /* do nothing */ }

    override fun onDrawerOpened(arg0: View) { /* do nothing */ }

    override fun onDrawerStateChanged(arg0: Int) { /* do nothing */ }

    override fun onDrawerSlide(arg0: View, slideOffset: Float) {
        val translationX = getTranslation(slideOffset)
        view.translationX = translationX
    }

    fun setVerticalOffset(offset: Float) {
        updateScreenDimensions()
        defaultOffset = offset
        view.y = defaultOffset * screenDimensions.y
    }

    private fun getDrawerViewGravity(drawerView: View): Int {
        val gravity = (drawerView.layoutParams as DrawerLayout.LayoutParams).gravity
        return GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(drawerView))
    }

    private fun getTranslation(slideOffset: Float): Float {
        return if (gravity == GravityCompat.START || gravity == Gravity.LEFT) slideOffset * drawer.width else -slideOffset * drawer.width
    }

    private fun updateScreenDimensions() = if (Build.VERSION.SDK_INT >= 13) {
        display.getSize(screenDimensions)
    } else {
        screenDimensions.x = display.width
        screenDimensions.y = display.height
    }

    inner class TouchListener : OnTouchListener {
        private val MAX_CLICK_DURATION = 200
        private var startClickTime: Long = 0
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startClickTime = System.currentTimeMillis()
                }
                MotionEvent.ACTION_UP -> {
                    if (System.currentTimeMillis() - startClickTime < MAX_CLICK_DURATION) {
                        v.performClick()
                        return true
                    }
                }
            }
            val copy = MotionEvent.obtain(event)
            val width = if (gravity == Gravity.LEFT || gravity == GravityCompat.START) -view.width / 2 else view.width / 2
            copy.edgeFlags = ViewDragHelper.EDGE_ALL
            copy.setLocation(
                event.rawX + width,
                event.rawY
            )
            drawerLayout.onTouchEvent(copy)
            copy.recycle()
            return true
        }
    }

    companion object {
        @JvmOverloads
        fun attach(drawer: View, handleLayout: Int, verticalOffset: Float = 0f): DrawerHandle {
            require(drawer.parent is DrawerLayout) { "Argument drawer must be direct child of a DrawerLayout" }
            return DrawerHandle(drawer.parent as DrawerLayout, drawer, handleLayout, verticalOffset)
        }
    }
}
