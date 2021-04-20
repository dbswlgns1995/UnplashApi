package com.example.unplashapi.data

import com.example.unplashapi.BuildConfig
import com.example.unplashapi.util.Constants
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

// retrofit query
interface JsonApi {

    @Headers("Accept-Version: v1", "Authorization: Client-ID ${Constants.CLIENT_ID}")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): UnsplashResponse
}