package com.example.news.repository

import com.example.news.data.repository.ArticlesRepositoryImp
import com.example.news.data.repository.SourcesRepositoryImpl
import com.example.news.repository.articles.ArticlesRepository
import com.example.news.repository.sources.SourcesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class repositoriesModule {

    @Binds
    abstract fun bindSourcesRepository(sourcesRepositoryImpl: SourcesRepositoryImpl):
            SourcesRepository

    @Binds
    abstract fun bindArticlesRepository(articlesRepositoryImp: ArticlesRepositoryImp):
            ArticlesRepository
}