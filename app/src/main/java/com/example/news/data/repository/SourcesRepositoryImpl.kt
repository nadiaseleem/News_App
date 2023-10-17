package com.example.news.data.repository

import com.example.news.data.api.sourcesModel.Source
import com.example.news.data.dataSourceContract.sources.SourcesDataSource
import com.example.news.repository.sources.SourcesRepository
import javax.inject.Inject

class SourcesRepositoryImpl @Inject constructor(private val onlineDataSource: SourcesDataSource) :
    SourcesRepository {
    override suspend fun getSources(category: String): List<Source?>? {
        return onlineDataSource.getSources(category = category)
    }

}