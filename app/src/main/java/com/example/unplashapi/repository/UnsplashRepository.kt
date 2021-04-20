package com.example.unplashapi.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.unplashapi.data.JsonApi
import javax.inject.Inject
import javax.inject.Singleton


// paging3 repository di from json api
@Singleton
class UnsplashRepository @Inject constructor(private val jsonApi: JsonApi) {

    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UnsplashPagingSource(jsonApi, query) }
        ).liveData
}