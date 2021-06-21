package com.karglobal.bengallery.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import com.karglobal.bengallery.common.Param
import com.karglobal.bengallery.api.UnsplashAPI
import com.karglobal.bengallery.model.PhotoModel

class RemoteRepository(private val api: UnsplashAPI, private val context: Context) :
    PhotoRepository {
    private val photosCache: MutableList<PhotoModel> = mutableListOf()

    fun getListPhotos(): List<PhotoModel> = photosCache

    /**
     * override the getPhotos method, this method will fetch random photos from Unsplash API and store them into cache
     */
    override suspend fun getPhotos(): List<PhotoModel> {
        if (isOnline(context)) {
            try {
                var listPhotos = api.getPhotos(page = Param.PAGE_NUM, perPage = Param.PER_PAGE)
                photosCache.addAll(listPhotos)
            } catch (e: Exception) {
                Log.e("Remote Repository", e.printStackTrace().toString())
            }
        } else {
            Toast.makeText(context, "No Network Connectivity", Toast.LENGTH_SHORT).show()

        }
        return photosCache
    }

    /**
     * check if device has internet connection
     */
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}