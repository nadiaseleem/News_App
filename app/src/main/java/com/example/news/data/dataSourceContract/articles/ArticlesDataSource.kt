package com.example.news.data.dataSourceContract.articles

import com.example.news.data.api.articlesModel.Article

interface ArticlesDataSource {

    suspend fun getArticles(source: String): List<Article?>?
    suspend fun getArticlesThatHas(searchKeyWord: String): List<Article?>?

}