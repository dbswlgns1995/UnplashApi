package com.example.unplashapi.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unplashapi.data.JsonApi
import com.example.unplashapi.models.Image
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UnsplashPagingSource @Inject constructor(
    private val unsplashApi: JsonApi,
    private val query: String
) : PagingSource<Int, Image>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {

        val position = params.key ?: 1

        return try {

            val response = unsplashApi.searchPhotos(query, position, params.loadSize)

            LoadResult.Page(
                data = response.results,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.results.isEmpty()) null else position + 1
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        }catch (exception: HttpException) {
            LoadResult.Error(exception)
        }


    }

    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        TODO("Not yet implemented")
    }


}