package com.karglobal.bengallery.model

/**
 *  Url model from the Unsplash API response
 */
data class UrlModel(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)
