package com.karglobal.bengallery.api

import com.karglobal.bengallery.BuildConfig
import com.karglobal.bengallery.model.PhotoModel
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashAPI {

    //get random photo sorted by popularity
    //Note that the argument "order_by" is optional. I set it to pouplar as the default value
    @GET("photos")
    suspend fun getPhotos(
        @Query("client_id") ID: String = BuildConfig.UNSPLASH_APIKEY,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String = "popular"
    ): List<PhotoModel>

}