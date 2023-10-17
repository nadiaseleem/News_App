package com.example.news.data.dataSourceContract.sources

import com.example.news.data.api.sourcesModel.Source

interface SourcesDataSource {
    suspend fun getSources(category: String): List<Source?>?
}