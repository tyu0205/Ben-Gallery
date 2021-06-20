package com.karglobal.bengallery.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide



@BindingAdapter("loadImage")
fun bindingImage(photo: ImageView, url: String?) {
    Glide.with(photo.context)
        .load(url)
        .into(photo)
}

@BindingAdapter("loadAdapter")
fun addAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}


