package com.karglobal.bengallery.ui.activity

import android.app.SharedElementCallback
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.karglobal.bengallery.common.NameSpace.Companion.CURRENT_PHOTO_POSITION
import com.karglobal.bengallery.common.NameSpace.Companion.PHOTO_POSITION_REQUEST_CODE
import com.karglobal.bengallery.databinding.ActivityPhotoGridLayoutBinding
import com.karglobal.bengallery.ui.adapters.PhotoGridAdapter
import com.karglobal.bengallery.viewmodel.PhotoViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * The main activity show grid of photos
 */
class PhotoGridActivity : AppCompatActivity() {
    lateinit var binding: ActivityPhotoGridLayoutBinding
    lateinit var photoGridAdapter: PhotoGridAdapter
    private val logTag = PhotoGridActivity::class.java.simpleName
    private val photoViewModel: PhotoViewModel by viewModel()
    private var currentPositionIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initViewModelObserver();
        loadDataFromUnsPlashAPI(null)
    }

    /**
     * using data binding to connect views
     */
    private fun initViews() {
        binding = ActivityPhotoGridLayoutBinding.inflate(layoutInflater)
        photoGridAdapter =
            PhotoGridAdapter(this)
        binding.apply {
            lifecycleOwner = this@PhotoGridActivity
            adapter = photoGridAdapter
            viewmodel = photoViewModel
        }
        setContentView(binding.root)
    }

    /**
     * Observe viewmodel here
     */
    private fun initViewModelObserver() {
        photoViewModel.getPhotos().observe(this, Observer {
//            Toast.makeText(this, it.size.toString(), Toast.LENGTH_LONG).show()
            photoGridAdapter.setData(it)
            photoGridAdapter.notifyDataSetChanged()
        })

        photoViewModel.errorMessage().observe(this, Observer {
            binding.photoLoadingError.setText("$it")
            binding.photoLoadingError.isVisible = true
            binding.btnRetry.isVisible = true
            Log.e(logTag, "error message $it")
        })

        photoViewModel.isLoading().observe(this, Observer {
            binding.photoLoadingProgress.isVisible = it
        })
    }

    /**
     * Function to trigger the loading data API
     */
    fun loadDataFromUnsPlashAPI(view: View?) {
        binding.photoLoadingError.isVisible = false
        binding.btnRetry.isVisible = false
        binding.photoLoadingProgress.isVisible = true
        photoViewModel.downloadPhotos()
    }

    /**
     * function to keep tracking the new selected position from the detail activity.
     */
    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        if (resultCode != PHOTO_POSITION_REQUEST_CODE || data == null) return
        currentPositionIndex = data.getIntExtra(CURRENT_PHOTO_POSITION, -1)
        if (currentPositionIndex == -1) return

        // Scroll to the new selected view in case it's not currently visible on the screen
        binding.photoGridRecycler.scrollToPosition(currentPositionIndex)
        val callback = CustomSharedElementCallback()
        this.setExitSharedElementCallback(callback)

        // Listen for the transition end and clear all registered callback
        this.getWindow().getSharedElementExitTransition()
            .addListener(object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition?) {}
                override fun onTransitionPause(transition: Transition?) {}
                override fun onTransitionResume(transition: Transition?) {}
                override fun onTransitionEnd(transition: Transition?) {
                    removeCallback()
                }

                override fun onTransitionCancel(transition: Transition?) {
                    removeCallback()
                }

                private fun removeCallback() {
                    if (this != null) {
                        getWindow().getSharedElementExitTransition()
                            .removeListener(this)
                        setExitSharedElementCallback(null as SharedElementCallback?)
                    }
                }
            })

        // Pause transition until the selected view is fully drawn
        supportPostponeEnterTransition()

        // Listen for the RecyclerView pre draw to make sure the selected view is visible,
        //  and findViewHolderForAdapterPosition will return a non null ViewHolder
        binding.photoGridRecycler.getViewTreeObserver()
            .addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    binding.photoGridRecycler.getViewTreeObserver().removeOnPreDrawListener(this)
                    val holder: RecyclerView.ViewHolder? =
                        binding.photoGridRecycler.findViewHolderForAdapterPosition(
                            currentPositionIndex
                        )
                    if (holder is PhotoGridAdapter.ViewHolder) {
                        callback.setView((holder as PhotoGridAdapter.ViewHolder).itemView)
                    }

                    // Continue the transition
                    supportStartPostponedEnterTransition()
                    return true
                }
            })

        //getters go here
        fun getCurrentPosition(): Int = currentPositionIndex
    }




    /**
     * Custom shared element callback to help transition go to the correct location
     */
    private class CustomSharedElementCallback : SharedElementCallback() {
        private var mView: View? = null

        /**
         * Set the transtion View to the callback, this should be called before starting the transition so the View is not null
         */
        fun setView(view: View?) {
            mView = view
        }

        override fun onMapSharedElements(
            names: MutableList<String>,
            sharedElements: MutableMap<String, View>
        ) {
            // Clear all current shared views and names
            names.clear()
            sharedElements.clear()

            // Store new selected view and name
            val transitionName: String = mView?.let { ViewCompat.getTransitionName(it) }!!
            names.add(transitionName)
            sharedElements[transitionName] = mView!!
        }
    }
}