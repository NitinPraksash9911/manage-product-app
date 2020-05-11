package `in`.nitin.roomdatabasesample.ui.adapter

import `in`.nitin.roomdatabasesample.R
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout


/**
 * Uses the [Glide] library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImageUrl(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().build()
        Glide.with(imgView.context)
            .load(imgUri)
            .centerCrop()
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}


/**
 * use for [TextInputLayout] for showing error
 * */
@BindingAdapter("setError")
fun setError(textinput: TextInputLayout, error: String?) {
    textinput.error = error
}

/**
 * to make [TextView] as strikeThrough text
 * */
@BindingAdapter("strikeText")
fun setStrikeText(view: TextView, value: String) {
    view.text = value
    view.paintFlags = STRIKE_THRU_TEXT_FLAG
}

/**
 * used to set background color of [MaterialButton]
 * */
@BindingAdapter("setButtonBgColor")
fun setButtonBgColor(button: MaterialButton, color: String?) {
    button.setBackgroundColor(Color.parseColor(color))
    button.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color));

}





