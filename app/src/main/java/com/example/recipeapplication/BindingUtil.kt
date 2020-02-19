package com.example.recipeapplication

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.io.File

@BindingAdapter("android:img")
fun setImg(image: ImageView, path: String){
    Glide.with(image.context)
        .load(File(path))
        .error(R.drawable.testimg)
        .into(image)
}