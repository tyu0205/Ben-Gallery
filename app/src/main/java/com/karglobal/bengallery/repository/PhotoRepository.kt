package com.karglobal.bengallery.repository

import com.karglobal.bengallery.model.PhotoModel

interface PhotoRepository {
    suspend fun getPhotos():List<PhotoModel>
}