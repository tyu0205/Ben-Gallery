package com.karglobal.bengallery.ui.activity


import android.app.SharedElementCallback
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.karglobal.bengallery.common.NameSpace.Companion.CURRENT_PHOTO_POSITION
import com.karglobal.bengallery.common.NameSpace.Companion.PHOTO_POSITION_REQUEST_CODE
import com.karglobal.bengallery.common.NameSpace.Companion.POSITION_STATE
import com.karglobal.bengallery.databinding.ActivityPhotoDetailLayoutBinding
import com.karglobal.bengallery.repository.RemoteRepository
import com.karglobal.bengallery.ui.adapters.PhotoDetailPagerAdapter
import org.koin.android.ext.android.inject


class PhotoDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoDetailLayoutBinding
    private val remoteRepository by inject<RemoteRepository>()

    //Custom shared element callback to help transition go to the correct location
    private val mFinishSharedElementCallback: SharedElementCallback =
        object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String?>,
                sharedElements: MutableMap<String?, View?>
            ) {
                val selectedView: View = getSelectedView() ?: return

                // Clear all current shared views and names
                names.clear()
                sharedElements.clear()

                // Store new selected view and name
                val transitionName: String? = ViewCompat.getTransitionName(selectedView)
                names.add(transitionName)
                sharedElements[transitionName] = selectedView
                setExitSharedElementCallback(null as SharedElementCallback?)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        //go to the right location in the pager
        Handler(Looper.getMainLooper()).post {
            binding.viewPager.setCurrentItem(intent.getIntExtra(CURRENT_PHOTO_POSITION, 0), false)
        }

    }

    /**
     * using data binding to connect views
     */
    private fun initViews() {
        binding = ActivityPhotoDetailLayoutBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@PhotoDetailActivity
            adapter = PhotoDetailPagerAdapter(remoteRepository.getListPhotos())

        }
        setContentView(binding.root)
    }

    //return current location to the caller
    override fun finishAfterTransition() {
        setEnterSharedElementCallback(mFinishSharedElementCallback)
        val intent = Intent()
        intent.putExtra(
            CURRENT_PHOTO_POSITION,
            binding.viewPager.currentItem
        )
        setResult(
            PHOTO_POSITION_REQUEST_CODE,
            intent
        )
        super.finishAfterTransition()
    }

    //saving position state for handling orientation
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(POSITION_STATE, binding.viewPager.currentItem)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Handler(Looper.getMainLooper()).post {
            binding.viewPager.setCurrentItem(savedInstanceState.getInt(POSITION_STATE, 0), false)
        }
    }

    private fun getSelectedView(): View? {
        return try {
            binding.viewPager.findViewWithTag("PhotoItem" + binding.viewPager.currentItem)
        } catch (ex: IndexOutOfBoundsException) {
            null
        } catch (ex: NullPointerException) {
            null
        }
    }
}