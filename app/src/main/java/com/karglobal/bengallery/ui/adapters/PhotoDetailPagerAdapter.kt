package com.karglobal.bengallery.ui.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.karglobal.bengallery.R
import com.karglobal.bengallery.databinding.DetailPhotoCardBinding
import com.karglobal.bengallery.model.PhotoModel

class PhotoDetailPagerAdapter(
    val photos: List<PhotoModel>
) : RecyclerView.Adapter<PhotoDetailPagerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: DetailPhotoCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PhotoModel) {
            binding.apply {
                photo = item
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        DataBindingUtil.inflate<DetailPhotoCardBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.detail_photo_card,
            viewGroup,
            false
        ).let { ViewHolder(it) }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(photos[position])


    override fun getItemCount() = photos.size
}