package com.example.news.data.dataSource.sources

import com.example.news.data.api.WebServices
import com.example.news.data.api.sourcesModel.Source
import com.example.news.data.dataSourceContract.sources.SourcesDataSource
import javax.inject.Inject

class SourcesOnlineDataSourceImpl @Inject constructor(private val webServices: WebServices) :
    SourcesDataSource {
    override suspend fun getSources(category: String): List<Source?>? {
        return webServices.getSources(category = category).sources
    }
}