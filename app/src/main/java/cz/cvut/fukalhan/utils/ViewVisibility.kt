package cz.cvut.fukalhan.utils

import android.view.View

object ViewVisibility {
    fun toggleVisibility(view: View) {
        view.visibility = if (view.visibility == View.GONE) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}