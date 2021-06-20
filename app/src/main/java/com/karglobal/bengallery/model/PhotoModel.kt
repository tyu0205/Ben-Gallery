package com.karglobal.bengallery.model

/**
 *  Photo model from the Unsplash API response
 */
data class PhotoModel(
    val id: String,
    val created_at: String,
    val updated_at: String,
    val width: Int,
    val height: Int,
    val color: String,
    val blur_hash: String,
    val likes: Int,
    val liked_by_user: Boolean,
    val description: String,
    val user: UserModel,
    val urls: UrlModel
)
