package com.example.unplashapi.data

import com.example.unplashapi.models.Image

// return image list by jsonApi
data class UnsplashResponse(
    val results : List<Image>
)