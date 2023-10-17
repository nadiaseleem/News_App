package com.example.news.data.api

import com.example.news.data.api.articlesModel.ArticlesResponse
import com.example.news.data.api.sourcesModel.SourcesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {
    @GET("/v2/top-headlines/sources")
    suspend fun getSources(
        @Query("apiKey") apiKey: String = ApiConstants.API_KEY,
        @Query("category") category: String
    ): SourcesResponse

    @GET("/v2/everything")
    suspend fun getArticles(
        @Query("apiKey") apiKey: String = ApiConstants.API_KEY,
        @Query("q") searchKeyWord: String? = null,
        @Query("sources") source: String? = null
    ): ArticlesResponse
}