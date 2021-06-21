package com.karglobal.bengallery.repository

import com.karglobal.bengallery.model.PhotoModel

/**
 * interface for all type of repository
 */
interface PhotoRepository {
    suspend fun getPhotos():List<PhotoModel>
}