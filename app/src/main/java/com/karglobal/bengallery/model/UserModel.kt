package com.karglobal.bengallery.model

/**
 *  User model from the Unsplash API response
 */
data class UserModel(
    val id: String,
    val username: String,
    val name: String,
    val portfolio_url: String,
    val bio: String,
    val location: String,
    val total_photos: Int
)
