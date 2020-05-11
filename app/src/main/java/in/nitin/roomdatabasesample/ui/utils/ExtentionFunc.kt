package `in`.nitin.roomdatabasesample.ui.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**
 * to show snackbar with specified color
 * */
fun String.snack(color: Int, view: View) {
    val snackbar = Snackbar.make(view, this, Snackbar.LENGTH_LONG)
    snackbar.view.setBackgroundColor(color)
    snackbar.setTextColor(Color.WHITE)
    snackbar.show()
}

/**
 * to show Toast
 * */
fun String.showToast(context: Context) {

    Toast.makeText(context, this, Toast.LENGTH_LONG).show()
}

/*for shared element transition in fragment*/
fun Fragment.waitForTransition(targetView: View) {
    postponeEnterTransition()
    targetView.doOnPreDraw { startPostponedEnterTransition() }
}

/*used for shared element transition*/
fun View.toTransitionGroup() = this to transitionName