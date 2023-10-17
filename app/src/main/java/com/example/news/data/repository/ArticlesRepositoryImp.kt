package com.example.news.data.repository

import com.example.news.data.api.articlesModel.Article
import com.example.news.data.dataSourceContract.articles.ArticlesDataSource
import com.example.news.repository.articles.ArticlesRepository
import javax.inject.Inject

class ArticlesRepositoryImp @Inject constructor(private val dataSource: ArticlesDataSource) :
    ArticlesRepository {
    override suspend fun getArticles(source: String): List<Article?>? {
        return dataSource.getArticles(source = source)
    }

    override suspend fun getArticlesThatHas(searchKeyWord: String): List<Article?>? {
        return dataSource.getArticlesThatHas(searchKeyWord = searchKeyWord)
    }

}