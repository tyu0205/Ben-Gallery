package com.karglobal.bengallery.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.karglobal.bengallery.ui.adapters.PhotoDetailPagerAdapter

/**
 * Here are the data binding configurations
 */
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

@BindingAdapter("loadPageAdapter")
fun addPageAdapter(view: ViewPager2, adapter: RecyclerView.Adapter<PhotoDetailPagerAdapter.ViewHolder>) {
    view.adapter = adapter
}

