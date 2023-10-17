package com.example.news.repository.articles

import com.example.news.data.api.articlesModel.Article

interface ArticlesRepository {
    suspend fun getArticles(source: String): List<Article?>?
    suspend fun getArticlesThatHas(searchKeyWord: String): List<Article?>?

}