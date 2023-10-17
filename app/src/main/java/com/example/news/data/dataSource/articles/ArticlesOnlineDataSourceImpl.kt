package com.example.news.data.dataSource.articles

import com.example.news.data.api.WebServices
import com.example.news.data.api.articlesModel.Article
import com.example.news.data.dataSourceContract.articles.ArticlesDataSource
import javax.inject.Inject

class ArticlesOnlineDataSourceImpl @Inject constructor(private val webServices: WebServices) :
    ArticlesDataSource {
    override suspend fun getArticles(source: String): List<Article?>? {
        return webServices.getArticles(source = source).articles
    }

    override suspend fun getArticlesThatHas(searcgKeyWord: String): List<Article?>? {
        return webServices.getArticles(searchKeyWord = searcgKeyWord).articles
    }


}