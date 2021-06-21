package com.karglobal.bengallery.ui.adapters

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.karglobal.bengallery.R
import com.karglobal.bengallery.common.NameSpace.Companion.CURRENT_PHOTO_POSITION
import com.karglobal.bengallery.common.NameSpace.Companion.PHOTO_POSITION_REQUEST_CODE
import com.karglobal.bengallery.databinding.GridPhotoCardBinding
import com.karglobal.bengallery.model.PhotoModel
import com.karglobal.bengallery.ui.activity.PhotoDetailActivity
import kotlin.collections.ArrayList

class PhotoGridAdapter(activity: Activity) : RecyclerView.Adapter<PhotoGridAdapter.ViewHolder>() {

    private var dataSet: List<PhotoModel> = ArrayList()
    private var activity: Activity = activity

    fun setData(data: List<PhotoModel>) {
        dataSet = data;

    }

    // Create photo views
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        DataBindingUtil.inflate<GridPhotoCardBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.grid_photo_card,
            viewGroup,
            false
        ).let { ViewHolder(it) }

    // Replace the contents of a view
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(dataSet[position])


    inner class ViewHolder(private val binding: GridPhotoCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val set = ConstraintSet()

        fun bind(item: PhotoModel) {
            if (item != null) {
                with(set) {
                    val posterRatio = kotlin.String.format("%d:%d", item.width, item.height)
                    clone(binding.photoCardParent)
                    setDimensionRatio(binding.image.id, posterRatio)
                    applyTo(binding.photoCardParent)

                }
            }

            binding.apply {
                photo = item
                executePendingBindings()
                binding.gridPhotoCardView.setOnClickListener {
                    var intent = Intent(activity, PhotoDetailActivity::class.java)
                    val options = ActivityOptions
                        .makeSceneTransitionAnimation(
                            activity,
                            root,
                            activity.getResources().getString(R.string.photo_transition))
                    intent.putExtra(CURRENT_PHOTO_POSITION, adapterPosition)

                    // start the detail activity
                    activity.startActivityForResult(intent, PHOTO_POSITION_REQUEST_CODE, options.toBundle())
//                    Toast.makeText(activity, item.urls.small, Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    // Return the size of your dataset
    override fun getItemCount() = dataSet.size
}