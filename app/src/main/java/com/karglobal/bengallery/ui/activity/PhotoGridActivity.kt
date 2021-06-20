package com.karglobal.bengallery.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.karglobal.bengallery.R
import com.karglobal.bengallery.databinding.ActivityPhotoGridLayoutBinding
import com.karglobal.bengallery.ui.adapters.PhotoGridAdapter
import com.karglobal.bengallery.viewmodel.PhotoViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class PhotoGridActivity : AppCompatActivity() {
    lateinit var binding: ActivityPhotoGridLayoutBinding
    lateinit var photoGridAdapter: PhotoGridAdapter
    private val logTag = PhotoGridActivity::class.java.simpleName
    private val photoViewModel: PhotoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoGridLayoutBinding.inflate(layoutInflater)
        photoGridAdapter =
            PhotoGridAdapter(this)
        binding.apply {
            lifecycleOwner = this@PhotoGridActivity
            adapter = photoGridAdapter
            viewmodel = photoViewModel
        }
        setContentView(binding.root)
        initViewModelObserver();
        photoViewModel.downloadPhotos()
    }

    private fun initViewModelObserver() {
        photoViewModel.getPhotos().observe(this, Observer {
            Log.d(logTag, "Data size: " + it.size.toString())
            photoGridAdapter.setData(it)
            photoGridAdapter.notifyDataSetChanged()
        })


    }
}