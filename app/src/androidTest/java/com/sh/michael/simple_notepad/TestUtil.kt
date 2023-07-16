package com.sh.michael.simple_notepad

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * custom interaction to perform instrumental tests on item views of recycler
 */
fun onRecyclerViewItem(
    recyclerViewId: Int,
    position: Int,
    targetViewId: Int? = null
): ViewInteraction = onView(object : TypeSafeMatcher<View>() {

    private var resources: Resources? = null
    private var recyclerView: RecyclerView? = null
    private var holder: RecyclerView.ViewHolder? = null
    private var targetView: View? = null

    override fun describeTo(description: Description) {
        fun Int.name(): String = try {
            "R.id.${resources?.getResourceEntryName(this)}"
        } catch (e: Resources.NotFoundException) {
            "unknown id $this"
        }

        val text = when {
            recyclerView == null -> "RecyclerView (${recyclerViewId.name()})"
            holder == null || targetViewId == null -> "in RecyclerView (${recyclerViewId.name()}) at position $position"
            else -> "in RecyclerView (${recyclerViewId.name()}) at position $position and with ${targetViewId.name()}"
        }
        description.appendText(text)
    }

    override fun matchesSafely(view: View): Boolean {
        // matchesSafely will be called for each view in the hierarchy (until found),
        // it makes no sense to perform lookup over and over again
        if (recyclerView == null) {
            resources = view.resources
            recyclerView = view.rootView.findViewById(recyclerViewId) ?: return false
            holder = recyclerView?.findViewHolderForAdapterPosition(position) ?: return false
            targetView = holder?.itemView?.let {
                if (targetViewId != null) it.findViewById(targetViewId) else it
            }
        }
        return view === targetView
    }
})