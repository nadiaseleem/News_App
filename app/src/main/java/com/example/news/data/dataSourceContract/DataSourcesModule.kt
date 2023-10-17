package com.example.news.data.dataSourceContract

import com.example.news.data.dataSource.articles.ArticlesOnlineDataSourceImpl
import com.example.news.data.dataSource.sources.SourcesOnlineDataSourceImpl
import com.example.news.data.dataSourceContract.articles.ArticlesDataSource
import com.example.news.data.dataSourceContract.sources.SourcesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataSourcesModule {

    @Binds
    abstract fun bindSourcesDataSource(sourcesOnlineDataSourceImpl: SourcesOnlineDataSourceImpl):
            SourcesDataSource

    @Binds
    abstract fun bindArticlesDataSource(articlesOnlineDataSourceImpl: ArticlesOnlineDataSourceImpl):
            ArticlesDataSource
}