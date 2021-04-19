package com.example.unplashapi.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Image(
    val id: String,
    val description: String?,
    val urls: UnsplashURL,
    val user: UnsplashUser
) : Parcelable {

    @Parcelize
    data class UnsplashURL(
        val full: String,
        val raw: String,
        val regular: String,
        val small: String,
        val thumb: String
    ) : Parcelable

    @Parcelize
    data class UnsplashUser(
        val name: String,
        val username: String
    ) : Parcelable {
        val attributionUrl get() = "https://unsplash.com/$username?utm_source=ImageSearchApp&utm_medium=referral"
    }
}